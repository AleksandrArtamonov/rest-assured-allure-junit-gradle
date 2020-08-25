package functionaltests;

import core.Dictionaries;
import dto.*;
import helpers.UserHelper;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

public class UserTests extends UserHelper {

    @Nested
    public class NeedUserForTest {

        @BeforeEach
        public void testSetup() {
            createUserAndSetId(testUser);
        }

        @Nested
        public class NeedToDeleteUserAfterTest {

            @AfterEach
            public void tearDown() {
                deleteUser(testUser);
            }

            @Test
            public void createUser() {
                checkUser(testUser);
            }

            @Test
            public void updateUserTest() {
                User updatedUser = testUser.toBuilder().name("updated testname").email("udatedtestemail@mail.com").gender(Gender.Female).status(Status.Inactive).build();
                Response response = updateUser(updatedUser);
                response.then().statusCode(HttpStatus.SC_OK);
                checkUser(updatedUser);
            }

            @Test
            public void getUser() {
                Response response = getOneUser(testUser);
                response.then().statusCode(HttpStatus.SC_OK);
                User actual = response.jsonPath().getObject("data", User.class);
                assertThat(testUser, samePropertyValuesAs(actual, "createdAt", "updatedAt"));
            }
        }

        @Test
        public void deleteUserTest() {
            Response response = deleteUser(testUser);
            response.then().statusCode(HttpStatus.SC_OK);
            Assertions.assertEquals(HttpStatus.SC_NO_CONTENT, response.jsonPath().getInt("code"));
            response = getOneUser(testUser);
            Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, response.jsonPath().getInt("code"));
            Assertions.assertEquals(Dictionaries.resourceNotFound, response.jsonPath().getString("data.message"));
        }
    }

    @Test
    public void getAllUsersTest() {
        Response response = getAllUsers();
        UserPage userPage = response.as(UserPage.class);
        Assertions.assertEquals(userPage.getMeta().getPagination().getPage(), 1);
        Assertions.assertEquals(userPage.getData().length, userPage.getMeta().getPagination().getLimit());
        Assertions.assertEquals(userPage.getMeta().getPagination().getTotal() / userPage.getMeta().getPagination().getLimit() + 1, userPage.getMeta().getPagination().getPages());
    }

    @Test
    public void userPaginationTest() {
        Response response = getFilteredUsers(new HashMap<String, Integer>() {{
            put("page", 2);
        }});
        response.then().statusCode(HttpStatus.SC_OK);
        UserPage userPage = response.as(UserPage.class);
        Assertions.assertEquals(userPage.getMeta().getPagination().getPage(), 2);
    }

    @ParameterizedTest
    @MethodSource("userProvider")
    public void createUserWithoutField(User user) {
        Response response = createUser(user);
        response.then().statusCode(HttpStatus.SC_OK);
        BlankFieldPage page = response.as(BlankFieldPage.class);
        Assertions.assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, page.code);
        Assertions.assertEquals(1, page.data.length);
        Assertions.assertEquals(Dictionaries.cantBeBlank, page.data[0].getMessage());
    }

    static Stream<User> userProvider() {
        return Stream.of(User.builder().email("testemail@mail.com").gender(Gender.Male).status(Status.Active).build(),
                User.builder().name("testname").gender(Gender.Male).status(Status.Active).build(),
                User.builder().name("testname").email("testemail@mail.com").status(Status.Active).build(),
                User.builder().name("testname").email("testemail@mail.com").gender(Gender.Male).build());
    }
}

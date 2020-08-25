package helpers;

import core.BaseTestClass;
import core.endpoints.UserRequests;
import dto.Gender;
import dto.Status;
import dto.User;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

public class UserHelper extends BaseTestClass implements UserRequests {
    public User testUser = User.builder().name("testname").email("testemail@mail.com").gender(Gender.Male).status(Status.Active).build();

    public void createUserAndSetId(User user) {
        Response response = createUser(user);
        response.then().statusCode(HttpStatus.SC_OK);
        user.setId(response.jsonPath().getInt("data.id"));
    }

    public void checkUser(User expected) {
        Response response = getOneUser(expected);
        User actual = response.jsonPath().getObject("data", User.class);
        assertThat(expected, samePropertyValuesAs(actual, "createdAt", "updatedAt"));
    }
}

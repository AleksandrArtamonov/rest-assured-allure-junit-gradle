package functionaltests;

import core.Dictionaries;
import core.Matchers;
import dto.Pagination;
import dto.Todo;
import dto.TodoPage;
import helpers.TodoHelper;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

public class TodoTests extends TodoHelper {

    @Nested
    public class NeedOneTodoForTest {

        @BeforeEach
        public void testSetup() {
            createTodoAndSetId(testTodo);
        }

        @Nested
        public class NeedToDeleteTodoAfterTest {

            @AfterEach
            public void tearDown() {
                deleteTodoAndUser(testTodo);
            }

            @Test
            public void createTodoTest() {
                checkTodo(testTodo);
            }

            @Test
            public void updateTodoTest() {
                Todo updatedTodo = testTodo.toBuilder().title("updated todo title").completed(true).build();
                Response response = updateTodo(updatedTodo);
                response.then().statusCode(HttpStatus.SC_OK);
                checkTodo(updatedTodo);
            }

            @Test
            public void getTodo() {
                Response response = getOneTodo(testTodo);
                response.then().statusCode(HttpStatus.SC_OK);
                Todo actual = response.jsonPath().getObject("data", Todo.class);
                assertThat(testTodo, samePropertyValuesAs(actual, "createdAt", "updatedAt"));
            }

            @Nested
            public class GetTodosForUser {
                Todo secondTestTodo;

                @AfterEach
                public void tearDown() {
                    deleteTodo(secondTestTodo);
                }

                @Test
                public void getTodosForUserTest() {
                    secondTestTodo = testTodo.toBuilder().title("second title").build();
                    secondTestTodo.setId(createTodo(secondTestTodo).jsonPath().getInt("data.id"));
                    Response response = getTodosForUser(testUser);
                    response.then().statusCode(HttpStatus.SC_OK);
                    TodoPage todoPage = response.as(TodoPage.class);
                    assertThat(new Pagination(2, 1, 1, 20), samePropertyValuesAs(todoPage.meta.getPagination()));
                    Matchers.checkLists(Arrays.asList(testTodo, secondTestTodo), Arrays.asList(todoPage.data));
                }
            }

        }

        @Nested
        public class DontNeedToDeleteTodoAfterTest {

            @AfterEach
            public void tearDown() {
                deleteUser(testUser);
            }

            @Test
            public void deleteTodoTest() {
                Response response = deleteTodo(testTodo);
                response.then().statusCode(HttpStatus.SC_OK);
                Assertions.assertEquals(HttpStatus.SC_NO_CONTENT, response.jsonPath().getInt("code"));
                response = getOneTodo(testTodo);
                Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, response.jsonPath().getInt("code"));
                Assertions.assertEquals(Dictionaries.resourceNotFound, response.jsonPath().getString("data.message"));
            }
        }
    }

    @Test
    public void getAllTodosTest() {
        Response response = getAllTodos();
        TodoPage todoPage = response.as(TodoPage.class);
        Assertions.assertEquals(todoPage.getMeta().getPagination().getPage(), 1);
        Assertions.assertEquals(todoPage.getData().length, todoPage.getMeta().getPagination().getLimit());
        Assertions.assertEquals(todoPage.getMeta().getPagination().getTotal()/todoPage.getMeta().getPagination().getLimit() + 1, todoPage.getMeta().getPagination().getPages());
    }

    @Test
    public void todoPaginationTest() {
        Response response = getFilteredTodos(new HashMap<String, Integer>() {{
            put("page", 2);
        }});
        response.then().statusCode(HttpStatus.SC_OK);
        TodoPage todoPage = response.as(TodoPage.class);
        Assertions.assertEquals(todoPage.getMeta().getPagination().getPage(), 2);
    }
}

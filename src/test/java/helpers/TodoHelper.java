package helpers;

import core.endpoints.TodoRequests;
import dto.Todo;
import dto.User;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

public class TodoHelper extends UserHelper implements TodoRequests {

    public Todo testTodo = Todo.builder().title("todo title").completed(false).build();

    public void createTodoAndSetId(Todo todo) {
        createUserAndSetId(testUser);
        todo.setUserId(testUser.getId());
        Response response = createTodo(todo);
        response.then().statusCode(HttpStatus.SC_OK);
        todo.setId(response.jsonPath().getInt("data.id"));
    }

    public void deleteTodoAndUser(Todo todo) {
        deleteTodo(todo);
        deleteUser(User.builder().id(todo.getUserId()).build());
    }

    public void checkTodo(Todo expected) {
        Response response = getOneTodo(expected);
        Todo actual = response.jsonPath().getObject("data", Todo.class);
        assertThat(expected, samePropertyValuesAs(actual, "createdAt", "updatedAt"));
    }
}

package core.endpoints;

import core.Endpoints;
import core.Requests;
import dto.Todo;
import dto.User;
import io.restassured.response.Response;

import java.util.HashMap;

public interface TodoRequests extends Requests {
    default Response createTodo(Todo todo) {
        return postRequestWithToken(Endpoints.todoPath, todo);
    }

    default Response updateTodo(Todo todo) {
        return putRequestWithToken(Endpoints.todoPath + "/" + todo.getId(), todo);
    }

    default Response getOneTodo(Todo todo) {
        return getRequest(Endpoints.todoPath + "/" + todo.getId());
    }

    default Response getAllTodos() {
        return getRequest(Endpoints.todoPath);
    }

    default Response getTodosForUser(User user) {
        return getRequest(Endpoints.userPath + "/" + user.getId() + Endpoints.todoPath );
    }

    default Response getFilteredTodos(HashMap params) {
        return getRequestWithReqParams(Endpoints.todoPath, params);
    }

    default Response deleteTodo(Todo todo) {
        return deleteRequestWithToken(Endpoints.todoPath + "/"+ todo.getId());
    }
}

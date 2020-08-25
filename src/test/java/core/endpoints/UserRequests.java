package core.endpoints;

import core.Endpoints;
import core.Requests;
import dto.User;
import io.restassured.response.Response;

import java.util.HashMap;

public interface UserRequests extends Requests {

    default Response createUser(User user) {
        return postRequestWithToken(Endpoints.userPath, user);
    }

    default Response updateUser(User user) {
        return putRequestWithToken(Endpoints.userPath + "/" + user.getId(), user);
    }

    default Response getOneUser(User user) {
        return getRequest(Endpoints.userPath + "/" + user.getId());
    }

    default Response getAllUsers() {
        return getRequest(Endpoints.userPath);
    }

    default Response getFilteredUsers(HashMap params) {
        return getRequestWithReqParams(Endpoints.userPath, params);
    }

    default Response deleteUser(User user) {
        return deleteRequestWithToken(Endpoints.userPath + "/"+ user.getId());
    }
}

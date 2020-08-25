package core.endpoints;

import core.Endpoints;
import core.Requests;
import dto.Creds;
import io.restassured.response.Response;

public interface LoginRequests extends Requests {
    default Response login(Creds creds) {
        return postRequest(Endpoints.loginPath, creds);
    }
}

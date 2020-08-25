package core;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;

public class CheckErrors {
    public final static String springSecurityError = "org.springframework.security.access.AccessDeniedException: Access is denied";

    public static void checkSpringSecError(Response response) {
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
        response.jsonPath().getString("items[0]").equals(springSecurityError);
    }

}

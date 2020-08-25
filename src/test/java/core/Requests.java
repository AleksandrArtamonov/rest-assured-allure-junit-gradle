package core;

import dto.Creds;
import io.qameta.allure.Attachment;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface Requests extends TestsConfiguration {

    String baseUri = TestsConfiguration.getBaseUrl();
    String token = TestsConfiguration.getToken();

    @Attachment(value = "request")
    default byte[] logRequest(ByteArrayOutputStream stream) {
        return attach(stream);
    }

    @Attachment(value = "response")
    default byte[] logResponse(ByteArrayOutputStream stream) {
        return attach(stream);
    }

    default byte[] attach(ByteArrayOutputStream log) {
        byte[] array = log.toByteArray();
        log.reset();
        return array;
    }

    default Response baseRequest(Function<RequestSpecification, Response> spec) {
        ByteArrayOutputStream request = new ByteArrayOutputStream();
        ByteArrayOutputStream response = new ByteArrayOutputStream();

        PrintStream requestVar = new PrintStream(request, true);
        PrintStream responseVar = new PrintStream(response, true);

        RestAssured.filters(new ResponseLoggingFilter(LogDetail.ALL, responseVar),
                new RequestLoggingFilter(LogDetail.ALL, requestVar));

        RestAssured.useRelaxedHTTPSValidation();
        RequestSpecification baseSpec = RestAssured.given()
                .baseUri(baseUri)
                .headers("Accept", "*/*", "Cache-Control", "no-cache",
                        "Accept-Encoding", "gzip, deflate, br", "accept-language", "en-US,en;q=0.9", "Connection", "keep-alive")
                .when().log().all();

        Response restResponse = spec.apply(baseSpec).prettyPeek();
        logRequest(request);
        logResponse(response);
        return restResponse;
    }

    default Response postRequest(String path, Object body) {
        return baseRequest((spec)->spec.header("Content-Type", "application/json").body(body).post(path));
    }

    default Response putRequest(String path, Object body) {
        return baseRequest((spec)->spec.header("Content-Type", "application/json").body(body).put(path));
    }

    default Response getRequestWithReqParams(String path, HashMap params) {
        return baseRequest((spec)->spec.queryParams(params).get(path));
    }

    default Response getRequest(String path) {
        return baseRequest((spec)->spec.get(path));
    }

    default Response deleteRequest(String path) {
        return baseRequest((spec)->spec.delete(path));
    }


    // Requests with token
    default Response postRequestWithToken(String path, Object body) {
        return baseRequest((spec)->spec.headers("Content-Type", "application/json", "Authorization", "Bearer " + token)
                .body(body).post(path));
    }

    default Response putRequestWithToken(String path, Object body) {
        return baseRequest((spec)->spec.headers("Content-Type", "application/json", "Authorization", "Bearer " + token)
                .body(body).put(path));
    }

    default Response getRequestWithToken(String path) {
        return baseRequest((spec)->spec.headers( "Authorization", "Bearer " + token).get(path));
    }

    default Response deleteRequestWithToken(String path) {
        return baseRequest((spec)->spec.headers("Content-Type", "application/json", "Authorization", "Bearer " + token).delete(path));
    }

//
    default String loginGetToken(Creds creds) {
        Response response = postRequest(Endpoints.loginPath, creds);
        response.then().statusCode(HttpStatus.SC_OK);
        return response.jsonPath().getString("items[0].token");
    }

}

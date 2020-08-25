package core;

public interface TestsConfiguration {

    static String getBaseUrl() {
        return System.getProperty("baseUrl") == null ? "https://gorest.co.in/public-api": System.getProperty("baseUrl");
    }

    static String getToken() {
        return System.getProperty("token") == null ? "please input token": System.getProperty("token");
    }
}

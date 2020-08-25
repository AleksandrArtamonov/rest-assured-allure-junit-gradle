# Rest Assured + Allure 2 + JUnit 5 + Gradle API testing example

### Features
- Core for API testing
- Tests for API https://gorest.co.in/
- Logging without @Step annotation
- Adding http request/response in report attachments

### To run tests and generate Allure report:
Please generate token on https://gorest.co.in/ with your Github or Google account
```sh
gradlew clean test -Ptoken=token
// or set token in TestsConfiguration
 
gradlew allureReport
```

or run tests from Intellij IDEA

### To see a report:
open index.html in report directory (you can find report directory in results of task allureReport)

// Path: src/test/java/base/BaseApiTest.java
package base;

import config.Config;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {
    @BeforeClass
    public void apiSetup() {
        RestAssured.baseURI = Config.apiBaseUrl();
    }
}

// Path: src/test/java/base/BaseUiTest.java
package base;

import config.Config;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

public class BaseUiTest {
    public WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        String base = Config.uiBaseUrl();
        waitUntilUp(base, 60);         // <-- wait up to 60s for dev server to start
        driver.get(base);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (!result.isSuccess()) screenshot();
        if (driver != null) driver.quit();
    }

    private void waitUntilUp(String baseUrl, int timeoutSeconds) {
        long end = System.currentTimeMillis() + timeoutSeconds * 1000L;
        while (System.currentTimeMillis() < end) {
            try {
                HttpURLConnection c = (HttpURLConnection) new URL(baseUrl).openConnection();
                c.setConnectTimeout(2000);
                c.setReadTimeout(2000);
                c.setRequestMethod("GET");
                int code = c.getResponseCode();
                if (code >= 200 && code < 500) return; // server is there
            } catch (Exception ignored) {}
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        }
        throw new RuntimeException("UI not reachable at " + baseUrl + " after " + timeoutSeconds + "s. " +
                "Override with -Dui.baseUrl=http://localhost:5173 (or your port).");
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] screenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}

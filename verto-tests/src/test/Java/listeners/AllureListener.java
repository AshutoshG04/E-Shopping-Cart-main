// Path: src/test/java/listeners/AllureListener.java
package listeners;

import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AllureListener implements ITestListener {
    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] capture(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
    @Override
    public void onTestFailure(ITestResult result) {
        try {
            Object instance = result.getInstance();
            WebDriver driver = (WebDriver) instance.getClass().getField("driver").get(instance);
            capture(driver);
        } catch (Exception ignored) {}
    }
}

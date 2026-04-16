// Path: src/test/java/tests/ui/CartAndCheckoutUiTests.java
package tests.ui;

import base.BaseUiTest;
import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement; // <-- THIS IS THE FIX
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;

@Epic("Verto Frontend")
@Feature("Cart & Checkout (Guest)")
@Listeners({listeners.AllureListener.class})
public class CartAndCheckoutUiTests extends BaseUiTest {

    // Selectors from HomeUiSmokeTests
    private final By addToCart   = By.cssSelector(".add-to-cart-btn");
    private final By cartToggle  = By.cssSelector(".cart-toggle-btn");

    // CORRECTED: Selectors for the actual cart overlay and checkout flow
    private final By cartView      = By.cssSelector(".cart-view");
    private final By checkoutBtn   = By.cssSelector(".checkout-btn"); // Button in the overlay
    private final By successModal  = By.xpath("//*[contains(text(),'Order Confirmed')]");
    private final By orderIdText   = By.xpath("//*[contains(text(),'Order ID:')]");

    @Severity(SeverityLevel.CRITICAL)
    @Description("Add item, open cart overlay, submit, and see confirmation modal")
    @Story("Guest can checkout")
    @Test
    public void cartToGuestCheckout_SuccessMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 1. ADD ITEM: (Added this step for a self-contained test)
        wait.until(ExpectedConditions.elementToBeClickable(addToCart)).click();

        // 2. OPEN CART:
        wait.until(ExpectedConditions.elementToBeClickable(cartToggle)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartView));

        // 3. PROCEED TO CHECKOUT:
        // The app has no form, just a checkout button.
        WebElement checkoutButton = wait.until(ExpectedConditions.elementToBeClickable(checkoutBtn));
        Assert.assertTrue(checkoutButton.isDisplayed(), "Checkout button should be visible in the cart");
        checkoutButton.click();

        // 4. ASSERT CONFIRMATION:
        // Wait for the "Order Confirmed" modal
        wait.until(ExpectedConditions.visibilityOfElementLocated(successModal));
        boolean confirmationOk = driver.findElement(successModal).isDisplayed()
                && driver.findElement(orderIdText).isDisplayed();

        Assert.assertTrue(confirmationOk, "Should see order success modal with an Order ID");
    }
}
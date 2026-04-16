// Path: src/test/java/tests/ui/HomeUiSmokeTests.java
package tests.ui;

import base.BaseUiTest;
import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomeUiSmokeTests extends BaseUiTest {

    By productCard = By.cssSelector(".product-card");
    By addToCart   = By.cssSelector(".add-to-cart-btn");
    By cartToggle  = By.cssSelector(".cart-toggle-btn");
    By cartBadge   = By.cssSelector(".cart-badge");
    By cartView    = By.cssSelector(".cart-view");

    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify a guest user can view products and add one item to cart")
    public void userCanAddItemToCart() {

        // Verify products loaded
        Assert.assertTrue(driver.findElements(productCard).size() > 0, "Product grid should appear");

        // Add first product
        driver.findElements(addToCart).get(0).click();

        // Verify badge count updates
        Assert.assertEquals(driver.findElement(cartBadge).getText().trim(), "1");

        // Open cart
        driver.findElement(cartToggle).click();
        Assert.assertTrue(driver.findElement(cartView).isDisplayed(), "Cart sidebar should open");
    }
}

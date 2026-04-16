const { Builder, By, until } = require('selenium-webdriver');
require('chromedriver');
const assert = require('assert');

describe('Verto Shop UI Test', function () {
  this.timeout(30000);

  let driver;

  before(async function () {
    driver = await new Builder().forBrowser('chrome').build();
  });

  after(async function () {
    if (driver) {
      await driver.quit();
    }
  });

  it('should load products successfully', async function () {
    await driver.get('http://localhost:5173');

    await driver.wait(until.elementLocated(By.css('.product-card')), 10000);

    const products = await driver.findElements(By.css('.product-card'));

    assert(products.length > 0, 'No products found on page');
  });

  it('should add product to cart', async function () {
    await driver.get('http://localhost:5173');

    await driver.wait(until.elementLocated(By.css('.product-card')), 10000);

    const addButtons = await driver.findElements(By.css('.add-to-cart-btn'));
    await addButtons[0].click();

    const cartButton = await driver.findElement(By.css('.cart-toggle-btn'));
    await cartButton.click();

    await driver.wait(until.elementLocated(By.css('.cart-item')), 10000);

    const cartItems = await driver.findElements(By.css('.cart-item'));

    assert(cartItems.length > 0, 'Cart is empty after adding product');
  });

  it('should increase quantity in cart', async function () {
    await driver.get('http://localhost:5173');

    await driver.wait(until.elementLocated(By.css('.product-card')), 10000);

    const addButtons = await driver.findElements(By.css('.add-to-cart-btn'));
    await addButtons[0].click();

    const cartButton = await driver.findElement(By.css('.cart-toggle-btn'));
    await cartButton.click();

    await driver.wait(until.elementLocated(By.css('.qty-btn')), 10000);

    const qtyButtons = await driver.findElements(By.css('.qty-btn'));
    await qtyButtons[1].click();

    const qtyText = await driver.findElement(By.css('.qty-count')).getText();

    assert.strictEqual(qtyText, '2');
  });

  it('should checkout successfully', async function () {
    await driver.get('http://localhost:5173');

    await driver.wait(until.elementLocated(By.css('.product-card')), 10000);

    const addButtons = await driver.findElements(By.css('.add-to-cart-btn'));
    await addButtons[0].click();

    const cartButton = await driver.findElement(By.css('.cart-toggle-btn'));
    await cartButton.click();

    await driver.wait(until.elementLocated(By.css('.checkout-btn')), 10000);

    const checkoutButton = await driver.findElement(By.css('.checkout-btn'));
    await checkoutButton.click();

    await driver.wait(
      until.elementLocated(By.xpath("//*[contains(text(),'Order Confirmed')]")),
      10000
    );

    const confirmation = await driver.findElement(
      By.xpath("//*[contains(text(),'Order Confirmed')]")
    );

    assert(await confirmation.isDisplayed());
  });
});
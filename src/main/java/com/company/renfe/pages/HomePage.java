package com.company.renfe.pages;

import com.company.renfe.util.Waits;
import org.openqa.selenium.*;

public class HomePage {
    private final WebDriver driver;

    // Adjust after inspecting Renfe's live DOM:
    private final By acceptCookiesBtn = By.cssSelector("#onetrust-accept-btn-handler");
    private final By dateOption = By.xpath("//*[@id='#daterangev2']/div[1]/div/label");
    private final By onewayLabel = By.cssSelector("#trip-option > div.lightpick__trip-label.trip > label");
    private final By originInput = By.xpath("//*[@id='origin']");
    private final By destinationInput = By.xpath("//*[@id='destination']");
    private final By autocompleteOptionOrig = By.xpath("//*[@id='awesomplete_list_1_item_0']");
    private final By autocompleteOptionDest = By.xpath("//*[@id='awesomplete_list_2_item_0']");
    private final By searchButton = By.xpath("//*[@id='ticketSearchBt']/div/div/button");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void open(String baseUrl) {
        driver.get(baseUrl);
    }

    public void acceptCookiesIfPresent() {
        try {
            // Step 1: Wait for the button to be clickable using centralized Waits utility
            WebElement btn = Waits.clickable(driver, acceptCookiesBtn);
            btn.click();

            // Step 2: Optional – Wait for the banner to disappear (recommended)
            By banner = By.id("onetrust-banner-sdk"); // ID común en Renfe
            try {
                Waits.invisibility(driver, banner);
            } catch (TimeoutException ignored) {
                // Banner might fade slowly; no need to fail
            }

        } catch (NoSuchElementException | TimeoutException ignored) {
            // Cookie banner not present, continue test
        } catch (ElementClickInterceptedException e) {
            // Step 3: Fallback – try clicking via JavaScript if element is overlapped
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
                    driver.findElement(acceptCookiesBtn));
        }
    }

    public void ensureOneWay() {
        try {
            // Step 1: Ensure that the date section or trip selector is ready
            WebElement dateSection = Waits.clickable(driver, dateOption);
            dateSection.click();

            // Step 2: Locate the radio button for "Viaje solo ida"
            By oneWayInput = By.id("trip-go");
            WebElement radioButton = driver.findElement(oneWayInput);

            // Step 3: Locate the associated label (the clickable element)
            WebElement label = driver.findElement(onewayLabel);

            // Step 4: Check if already selected to avoid unnecessary clicks
            if (!radioButton.isSelected()) {
                Waits.clickable(driver, onewayLabel).click();
            }

        } catch (TimeoutException | NoSuchElementException ignored) {
            // One-Way might already be the default mode — safe to continue
        } catch (ElementClickInterceptedException e) {
            // Fallback – try clicking with JavaScript if blocked by overlay
            WebElement label = driver.findElement(onewayLabel);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", label);
        }
    }

    /**
     * Step: Set the origin station (e.g., "Madrid-Atocha") and select the first suggestion.
     * Uses explicit waits to ensure the autocomplete dropdown is available before clicking.
     */
    public void setOrigin(String origin) {
        try {
            // Wait until the origin input is visible and ready to interact
            WebElement input = Waits.visible(driver, originInput);
            input.click();
            input.clear();
            input.sendKeys(origin);

            // Wait for the autocomplete option to appear and click the first suggestion
            WebElement option = Waits.visible(driver, autocompleteOptionOrig);
            option.click();

        } catch (TimeoutException e) {
            // Fallback: press ENTER if no suggestion list appears
            WebElement input = driver.findElement(originInput);
            input.sendKeys(Keys.ENTER);
        } catch (ElementClickInterceptedException e) {
            // Fallback: use JavaScript click if an overlay blocks interaction
            WebElement option = driver.findElement(autocompleteOptionOrig);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        }
    }

    /**
     * Step: Set the destination station (e.g., "Barcelona-Sants") and select the first suggestion.
     * Mirrors the behavior of setOrigin() to keep consistency and stability.
     */
    public void setDestination(String destination) {
        try {
            // Wait until the destination input is visible and ready
            WebElement input = Waits.visible(driver, destinationInput);
            input.click();
            input.clear();
            input.sendKeys(destination);

            // Wait for the first autocomplete option and click
            WebElement option = Waits.visible(driver, autocompleteOptionDest);
            option.click();

        } catch (TimeoutException e) {
            // Fallback: press ENTER if no suggestion list appears
            WebElement input = driver.findElement(destinationInput);
            input.sendKeys(Keys.ENTER);
        } catch (ElementClickInterceptedException e) {
            // Fallback: click via JavaScript if overlay blocks
            WebElement option = driver.findElement(autocompleteOptionDest);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        }
    }

    public void search() {
        WebElement btn = Waits.clickable(driver, searchButton);
        btn.click();
    }
}

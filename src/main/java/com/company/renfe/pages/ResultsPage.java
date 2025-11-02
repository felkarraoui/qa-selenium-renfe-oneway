package com.company.renfe.pages;

import com.company.renfe.util.PriceParser;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Page Object representing the search results for available Renfe train tickets.
 * Provides methods to validate availability, select trips, and continue purchase flow.
 */
public class ResultsPage {
    private final WebDriver driver;

    // --- Locators ---
    private final By resultsContainer = By.id("listaTrenesTBodyIda");
    private final By resultCard = By.cssSelector("div[id^='tren_i_']");
    private final By noAvailabilityMsg = By.id("noDispoIda");
    private final By weekDayButtons = By.xpath("//*[@id='day_button']");
    private final By selectBasicButton = By.id("btnSeleccionar");

    public ResultsPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Waits until the "no availability" message is displayed (if any).
     */
    public boolean waitUntilNoAvailabilityDisplayed(Duration timeout) {
        try {
            new WebDriverWait(driver, timeout)
                    .until(ExpectedConditions.visibilityOfElementLocated(noAvailabilityMsg));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Returns all visible result cards currently displayed on the page.
     */
    public List<WebElement> getResultCards() {
        return driver.findElements(resultCard);
    }

    /**
     * Checks if the weekday button is enabled (not disabled or hidden).
     */
    private boolean isDayButtonEnabled(WebElement btn) {
        if (btn == null) return false;
        String ariaDisabled = btn.getAttribute("aria-disabled");
        String disabledAttr = btn.getAttribute("disabled");
        String classAttr = btn.getAttribute("class");
        boolean hasDisabledClass = classAttr != null && classAttr.toLowerCase().contains("disabled");
        boolean attrDisabled = (disabledAttr != null) || ("true".equalsIgnoreCase(ariaDisabled));
        return btn.isDisplayed() && btn.isEnabled() && !attrDisabled && !hasDisabledClass;
    }

    /**
     * Iterates through the weekday buttons, selecting only enabled ones.
     * Returns the first list of result cards found with availability.
     */
    public List<WebElement> findAvailableCardsInEnabledWeekDays(Duration perStepTimeout) {
        WebDriverWait wait = new WebDriverWait(driver, perStepTimeout);
        List<WebElement> buttons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(weekDayButtons));

        if (buttons.isEmpty()) return Collections.emptyList();

        for (int i = 1; i <= buttons.size(); i++) {
            By buttonAtIndex = By.xpath("(//*[@id='day_button'])[" + i + "]");
            try {
                WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(buttonAtIndex));
                if (!isDayButtonEnabled(btn)) continue;

                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
                btn.click();

                wait.until(d -> !d.findElements(noAvailabilityMsg).isEmpty()
                        || !d.findElements(resultCard).isEmpty());

                boolean noAvailability = !driver.findElements(noAvailabilityMsg).isEmpty();
                List<WebElement> cards = driver.findElements(resultCard);
                if (!noAvailability && !cards.isEmpty()) return cards;

            } catch (TimeoutException | StaleElementReferenceException ignored) {
            }
        }

        return Collections.emptyList();
    }

    /**
     * Extracts dynamic index (n) from a train card's HTML id (e.g., "tren_i_2" -> "2").
     */
    private String extractCardIndex(WebElement card) {
        String id = card.getAttribute("id");
        if (id == null) return null;
        Matcher m = Pattern.compile("tren_i_(\\d+)").matcher(id);
        return m.find() ? m.group(1) : null;
    }

    private By priceLocatorFor(String index) {
        return By.xpath("//*[@id='precio-viaje_tren_i_" + index + "_item2']/span");
    }

    private By timeLocatorFor(String index) {
        return By.xpath("//*[@id='tren_" + index + "_item1']/span[6]/span");
    }

    /**
     * Verifies that each result card displays both journey time and price.
     */
    public boolean allResultsShowTimeAndPrice(List<WebElement> cards) {
        if (cards == null || cards.isEmpty()) return false;

        for (WebElement card : cards) {
            String idx = extractCardIndex(card);
            if (idx == null) return false;

            boolean hasPrice = !driver.findElements(priceLocatorFor(idx)).isEmpty();
            boolean hasTime = !driver.findElements(timeLocatorFor(idx)).isEmpty();

            if (!hasPrice || !hasTime) return false;
        }
        return true;
    }

    private By plansOptionsLocatorFor(String index) {
        return By.xpath("//*[@id='planes-opciones_i_" + index + "']/div[1]");
    }

    /**
     * Clicks on the first train card whose price is within the given range [min, max].
     */
    public boolean clickCardWithinPriceRange(List<WebElement> cards, double min, double max, Duration timeout) {
        if (cards == null || cards.isEmpty()) return false;

        WebDriverWait wait = new WebDriverWait(driver, timeout);

        for (WebElement card : cards) {
            String idx = extractCardIndex(card);
            if (idx == null) continue;

            try {
                WebElement priceEl = wait.until(ExpectedConditions.presenceOfElementLocated(priceLocatorFor(idx)));
                String title = priceEl.getAttribute("title");
                if (title == null || title.isEmpty()) title = priceEl.getText();

                double price = PriceParser.parseEuro(title);

                System.out.println("el precio parceado: " + price);
                if (Double.isNaN(price)) continue;

                if (price >= min && price <= max) {
                    ((JavascriptExecutor) driver)
                            .executeScript("arguments[0].scrollIntoView({block:'center'});", card);

                    try {
                        wait.until(ExpectedConditions.elementToBeClickable(card)).click();
                    } catch (Exception e) {
                        wait.until(ExpectedConditions.elementToBeClickable(priceEl)).click();
                    }

                    WebElement optionEl = wait.until(ExpectedConditions.elementToBeClickable(plansOptionsLocatorFor(idx)));
                    ((JavascriptExecutor) driver)
                            .executeScript("arguments[0].scrollIntoView({block:'center'});", optionEl);
                    optionEl.click();
                    return true;
                }
            } catch (Exception ignored) {
            }
        }

        return false;
    }

    /**
     * Clicks the "Seleccionar" (Basic) button once it becomes clickable.
     */
    public boolean clickSelectBasicButton(Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        try {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(selectBasicButton));
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
            btn.click();
            return true;
        } catch (TimeoutException e) {
            return false;
        } catch (ElementClickInterceptedException e) {
            try {
                WebElement btn = driver.findElement(selectBasicButton);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                return true;
            } catch (Exception ignore) {
                return false;
            }
        }
    }
}

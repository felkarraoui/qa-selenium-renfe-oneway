package com.company.renfe.pages;

import com.company.renfe.util.Waits;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FareSelectionPage {
    private final WebDriver driver;

    private final By basicContinue = By.xpath("//*[@id='aceptarConfirmacionFareUpgrade']");

    public FareSelectionPage(WebDriver driver) {
        this.driver = driver;
    }


    public void continueWithBasicFare() {
        continueWithBasicFare(Duration.ofSeconds(10));
    }

    // Wait until the button is visible and clickable, then click it.
    public void continueWithBasicFare(Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        try {
            // Wait for visibility and clickability.
            WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(basicContinue));
            btn = wait.until(ExpectedConditions.elementToBeClickable(basicContinue));

            // Ensure it is within the viewport.
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
            } catch (Exception ignored) {}

            // Standard click.
            btn.click();
        } catch (TimeoutException e) {
            throw new TimeoutException("El botón 'Continuar con Básica' no estuvo visible/clicable a tiempo.", e);
        } catch (ElementClickInterceptedException e) {
            // Retry with JS click as a last resort.
            WebElement btn = driver.findElement(basicContinue);
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            } catch (Exception ex) {
                throw new RuntimeException("No se pudo hacer click en 'Continuar con Básica'.", ex);
            }
        }
    }
}

package com.company.renfe.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PassengerDetailsPage {
    private final WebDriver driver;

    // Localizador del formulario de datos del pasajero
    private final By passengerForm = By.xpath("//*[@id='formBean']");

    public PassengerDetailsPage(WebDriver driver) {
        this.driver = driver;
    }


    public boolean isDisplayed() {
        return isDisplayed(Duration.ofSeconds(10));
    }

    // Wait until the form is visible and return true/false.
    public boolean isDisplayed(Duration timeout) {
        try {
            new WebDriverWait(driver, timeout)
                    .until(ExpectedConditions.visibilityOfElementLocated(passengerForm));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
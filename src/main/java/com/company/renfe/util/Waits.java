package com.company.renfe.util;

import com.company.renfe.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Waits {

    public static WebElement visible(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Config.EXPLICIT_WAIT_SECONDS));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement clickable(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Config.EXPLICIT_WAIT_SECONDS));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void invisibility(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Config.EXPLICIT_WAIT_SECONDS));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

}

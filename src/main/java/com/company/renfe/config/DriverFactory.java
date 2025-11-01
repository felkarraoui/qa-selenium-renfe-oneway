package com.company.renfe.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public class DriverFactory {
    public static WebDriver createDriver() {
        String browser = Config.BROWSER.toLowerCase();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        WebDriver driver;
        switch (browser) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions fx = new FirefoxOptions();
                if (headless) fx.addArguments("-headless");
                driver = new FirefoxDriver(fx);
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions ch = new ChromeOptions();
                ch.addArguments("--start-maximized");
                if (headless) ch.addArguments("--headless=new");
                driver = new ChromeDriver(ch);
            }
        }

        // Low implicit wait: affects ONLY findElement(s)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Config.IMPLICIT_WAIT_SECONDS));

        // Control of navigation and script execution times
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(
                Integer.parseInt(System.getProperty("pageLoadTimeout", "30"))));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(
                Integer.parseInt(System.getProperty("scriptTimeout", "15"))));

        return driver;
    }
}

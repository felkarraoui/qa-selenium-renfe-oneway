package com.company.renfe.tests;

import com.company.renfe.config.Config;
import com.company.renfe.config.DriverFactory;
import com.company.renfe.pages.HomePage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PurchaseOneWayTicketTest {
    private WebDriver driver;
    private HomePage home;

    @BeforeClass
    public void setup() {
        driver = DriverFactory.createDriver();
        home = new HomePage(driver);
    }


    @Test(description = "Business scenario: Purchase a One-Way Train Ticket from Madrid to Barcelona")
    public void purchaseOneWayBasicFareBetween50And60() {

        // 1. Open https://www.renfe.com/es/es in a desktop browser.
        home.open(Config.BASE_URL);

    }
}

package com.company.renfe.tests;

import com.company.renfe.config.Config;
import com.company.renfe.config.DriverFactory;
import com.company.renfe.pages.HomePage;
import com.company.renfe.tests.data.TestData;
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

        // 2. Accept cookies and any initial pop-ups to enable interaction.
        home.acceptCookiesIfPresent();

        // 3. Ensure trip type is One-Way (Ida).
        home.ensureOneWay();

        // 4. In Origin, enter “Madrid-Atocha” and select the correct station from autocomplete.
        home.setOrigin(TestData.ORIGIN);

        // 5. In Destination, enter “Barcelona-Sants” and select the correct station from autocomplete.
        home.setDestination(TestData.DESTINATION);

        // 7. Click Search to load available trains.
        home.search();

    }
}

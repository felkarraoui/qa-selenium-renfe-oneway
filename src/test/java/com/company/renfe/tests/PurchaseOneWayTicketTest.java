package com.company.renfe.tests;

import com.company.renfe.config.Config;
import com.company.renfe.config.DriverFactory;
import com.company.renfe.pages.FareSelectionPage;
import com.company.renfe.pages.HomePage;
import com.company.renfe.pages.PassengerDetailsPage;
import com.company.renfe.pages.ResultsPage;
import com.company.renfe.tests.data.TestData;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class PurchaseOneWayTicketTest {
    private WebDriver driver;
    private HomePage home;
    private ResultsPage results;
    private FareSelectionPage fare;
    private PassengerDetailsPage passenger;

    @BeforeClass
    public void setup() {
        driver = DriverFactory.createDriver();
        home = new HomePage(driver);
        results = new ResultsPage(driver);
        fare = new FareSelectionPage(driver);
        passenger = new PassengerDetailsPage(driver);
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

        // 8. On the results page, verify that each displayed result shows both journey time and price.

        List<WebElement> cards = results.findAvailableCardsInEnabledWeekDays(Config.DEFAULT_EXPLICIT_WAIT);
        if (cards.isEmpty()) {
            throw new SkipException("No availability.");
        }

        Assert.assertTrue(results.allResultsShowTimeAndPrice(cards), "Each result must show journey time and price.");

        // Click on the first card whose price is within the range [MIN_PRICE, MAX_PRICE]
        boolean clicked = results.clickCardWithinPriceRange(
                cards,
                TestData.MIN_PRICE,
                TestData.MAX_PRICE,
                Config.DEFAULT_EXPLICIT_WAIT
        );

        Assert.assertTrue(
                clicked,
                String.format("No card found with a price between %.2f y %.2f", TestData.MIN_PRICE, TestData.MAX_PRICE)
        );

        // Click on the "Select" (Basic) button when it is visible and clickable
        boolean basicSelected = results.clickSelectBasicButton(Config.DEFAULT_EXPLICIT_WAIT);
        Assert.assertTrue(basicSelected, "The 'Select' (Basic) button was not visible/clickable in time.");

        // Use FareSelectionPage to continue with the Basic option (wait and click).
        fare.continueWithBasicFare(Config.DEFAULT_EXPLICIT_WAIT);

        //Final assert: verify that the passenger details page is displayed.
        Assert.assertTrue(passenger.isDisplayed(), "Passenger details page should be displayed.");


    }
}

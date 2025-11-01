# Renfe One-Way Ticket Automation (Selenium + TestNG, Java)

Business scenario: Purchase a One-Way Train Ticket from Madrid to Barcelona

Homepage: https://www.renfe.com/es/es

Test case steps (English):
1. Open https://www.renfe.com/es/es in a desktop browser.
2. Accept cookies and any initial pop-ups to enable interaction.
3. Ensure trip type is One-Way (Ida).
4. In Origin, enter “Madrid-Atocha” and select the correct station from autocomplete.
5. In Destination, enter “Barcelona-Sants” and select the correct station from autocomplete.
6. Select any future date with availability (date is not relevant).
7. Click Search to load available trains.
8. On the results page, verify that each displayed result shows both journey time and price.
9. From the list of results, select a one-way ticket priced between 50 and 60 euros, Basic fare.
10. In the next pop up select that user wants to continue with basic fare.
11. The test ends when user advance to the next page (Passenger details).
12. Assert that the Passenger details page is displayed.

Local run:
- Java 17+
- mvn clean test -Dtest=PurchaseOneWayTicketTest
- Headless: add -DHEADLESS=true

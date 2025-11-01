package com.company.renfe.config;

public class Config {
    public static final String BASE_URL =
            System.getProperty("baseUrl", "https://www.renfe.com/es/es");

    public static final int IMPLICIT_WAIT_SECONDS =
            Integer.parseInt(System.getProperty("implicitWait", "2"));

    public static final int EXPLICIT_WAIT_SECONDS =
            Integer.parseInt(System.getProperty("explicitWait", "12"));

    // This line defines the BROWSER and allows changing it without modifying the code
    public static final String BROWSER =
            System.getProperty("browser", "chrome"); // possible values: chrome | firefox

}

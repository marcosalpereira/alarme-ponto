package org.marcosoft.alarm.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Selenium Support.
 */
public class SeleniumSupport {

    public static WebDriver initSelenium(final String browserUrl) {
    	final WebDriver driver = new FirefoxDriver();
    	driver.get(browserUrl);
    	return driver;
    }

}


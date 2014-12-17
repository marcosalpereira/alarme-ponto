package org.marcosoft.alarm.po;

import org.marcosoft.util.WaitWindow;
import org.marcosoft.util.WaitWindow.WaitCondition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PageObject {
    protected WebDriver webDriver;

    public PageObject(WebDriver webDriver) {
	    this.webDriver = webDriver;
    }

	/**
     * @param id id que deve esperar ficar presente
     * @param message mensagem de espera
     */
    public void waitForElementPresent(final String id, final String message) {
        final WaitCondition condition = new WaitCondition() {
            @Override
            public boolean satisfied() {
            	return webDriver.findElement(By.id(id)).isDisplayed();
            }
        };
        final boolean conditionSatisfied = WaitWindow.waitForCondition(condition, message);
        if (!conditionSatisfied) {
            throw new RuntimeException(
                "Esperava pelo elemento " + id + " mas ele não apareceu!");
        }
    }
}

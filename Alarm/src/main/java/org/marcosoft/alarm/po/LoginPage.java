package org.marcosoft.alarm.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends PageObject {

	public LoginPage(WebDriver webDriver) {
		super(webDriver);
	}

	public void login(String cpf) {
		webDriver.findElement(By.name("tx_cpf")).sendKeys(cpf);
	}

}

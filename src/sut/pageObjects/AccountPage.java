package sut.pageObjects;

import webdriver.ExtendedWebDriver;
import webdriver.controls.Button;

/*
 * Created by demidovskiy-r on 01.06.2015.
 */
public class AccountPage extends BasePage<AccountPage> {

    private Button loginDetailsBtn;
    private String userName;

    public AccountPage(ExtendedWebDriver web) {
        super(web);
    }

    @Override
    public void init() {
        loginDetailsBtn = new Button(this, generateControlLocator(".dropdown > a[href*='members']"));
        userName = loginDetailsBtn.getWebElement().getText().trim();
    }

    public String getUserName() {
        return userName;
    }
}
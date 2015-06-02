package sut.factoryComponents;

import sut.pageObjects.BasePage;
import sut.pageObjects.AccountPage;
import sut.pageObjects.LoginForm;
import sut.pageObjects.SignUpPage;
import webdriver.ExtendedWebDriver;

/*
 * Created by demidovskiy-r on 01.06.2015.
 */
public class PageCreator extends PageFactory {

    public PageCreator(ExtendedWebDriver webDriver) {
        super(webDriver);
    }

    public BasePage getBasePage() {
        return getPage(BasePage.class);
    }

    public SignUpPage getSignUpPage() {
        return getPage(SignUpPage.class);
    }

    public AccountPage getAccountPage() {
        return getPage(AccountPage.class);
    }

    public LoginForm getLoginForm() {
        return getPage(LoginForm.class);
    }
}
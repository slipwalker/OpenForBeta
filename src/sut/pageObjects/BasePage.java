package sut.pageObjects;

import org.openqa.selenium.By;
import sut.factoryComponents.Page;
import webdriver.ExtendedWebDriver;
import webdriver.controls.Button;
import static com.thoughtworks.selenium.SeleneseTestBase.assertTrue;

/*
 * Created by demidovskiy-r on 01.06.2015.
 */
public class BasePage<T> extends Page<T> {
    private final static String BASE_ROOT_NODE = ".wrap";
    private Button signUpBtn;
    private Button loginBtn;

    public BasePage(ExtendedWebDriver web) {
        super(web);
    }

    @Override
    public void load() {
        web.waitUntilElementAppearVisible(getPageLocator());
    }

    @Override
    public void unload() {
        web.waitUntilElementDisappear(getPageLocator());
    }

    @Override
    public void isLoaded() throws Error {
        assertTrue(web.isElementVisible(getPageLocator()));
    }

    @Override
    public void init() {
        signUpBtn = new Button(this, generateLoginLinkLocator("signup"));
        loginBtn = new Button(this, generateLoginLinkLocator("login"));
    }

    protected String getRootNode() {
        return BASE_ROOT_NODE;
    }

    public SignUpPage getSignUpPage() {
        if (!web.isElementVisible(By.cssSelector(SignUpPage.ROOT_NODE)))
            clickSignUpLink();
        return new SignUpPage(web);
    }

    public LoginForm getLoginForm() {
        if (!web.isElementVisible(By.cssSelector(LoginForm.ROOT_NODE)))
            clickLoginLink();
        return new LoginForm(web);
    }

    private void clickSignUpLink() {
        signUpBtn.click();
    }

    private void clickLoginLink() {
        loginBtn.click();
    }

    private By generateLoginLinkLocator(String partialLocator) {
        return By.cssSelector(".loginlink a[href*='" + partialLocator + "']");
    }

    protected By generateControlLocator(String partialLocator) {
        return By.cssSelector(String.format("%s %s", getRootNode(), partialLocator));
    }

    protected String getNodeByName(String partialLocator) {
        return "[name='" + partialLocator + "']";
    }

    private By getPageLocator() {
        return By.cssSelector(BASE_ROOT_NODE);
    }
}
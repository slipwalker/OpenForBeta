package sut.pageObjects;

import org.openqa.selenium.By;
import webdriver.ExtendedWebDriver;
import webdriver.controls.Button;
import webdriver.controls.Edit;
import java.util.Map;
import static com.thoughtworks.selenium.SeleneseTestBase.assertTrue;

/*
 * Created by demidovskiy-r on 01.06.2015.
 */
public class LoginForm extends BasePage<LoginForm> {
    public final static String ROOT_NODE = "[id='login-form']:not([style='display:none'])";
    private Edit userName;
    private Edit password;
    private Button loginBtn;
    private Button closeBtn;

    public LoginForm(ExtendedWebDriver web) {
        super(web);
    }

    @Override
    public void load() {
        super.load();
        web.waitUntilElementAppearVisible(getFormLocator());
    }

    @Override
    public void unload() {
        web.waitUntilElementDisappear(getFormLocator());
    }

    @Override
    public void isLoaded() throws Error {
        super.isLoaded();
        assertTrue(web.isElementVisible(getFormLocator()));
    }

    @Override
    public void init() {
        super.init();
        userName = new Edit(this, generateControlLocator(getNodeByName("lusername")));
        password = new Edit(this, generateControlLocator(getNodeByName("lpassword")));
        loginBtn = new Button(this, generateControlLocator("button[type='submit']"));
        closeBtn = new Button(this, generateControlLocator(".close-block"));
    }

    @Override
    protected String getRootNode() {
        return ROOT_NODE;
    }

    public void fill(Map<String, String> data) {
        for (Map.Entry<String, String> entry: data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "User Name": userName.type(value); break;
                case "Password": password.type(value); break;
                default: throw new IllegalArgumentException("Unknown field: " + key);
            }
        }
    }

    public AccountPage login() {
        clickLoginBtn();
        unload();
        return new AccountPage(web);
    }

    private void clickLoginBtn() {
        loginBtn.click();
    }

    private void clickCloseBtn() {
        closeBtn.click();
    }

    private By getFormLocator() {
        return By.cssSelector(ROOT_NODE);
    }
}
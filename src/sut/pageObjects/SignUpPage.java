package sut.pageObjects;

import org.openqa.selenium.By;
import webdriver.ExtendedWebDriver;
import webdriver.controls.Button;
import webdriver.controls.Dropdown;
import webdriver.controls.Edit;
import java.util.Map;
import static com.thoughtworks.selenium.SeleneseTestBase.assertTrue;

/*
 * Created by demidovskiy-r on 01.06.2015.
 */
public class SignUpPage extends BasePage<SignUpPage> {
    public final static String ROOT_NODE = "[id='signupform']";
    private Edit userName;
    private Edit password;
    private Edit confirmPassword;
    private Edit email;
    private Dropdown gender;
    private Edit captcha;
    private Button createYourAccountBtn;

    public SignUpPage(ExtendedWebDriver web) {
        super(web);
    }

    @Override
    public void load() {
        super.load();
        web.waitUntilElementAppearVisible(getPageLocator());
    }

    @Override
    public void unload() {
        web.waitUntilElementDisappear(getPageLocator());
    }

    @Override
    public void isLoaded() throws Error {
        super.isLoaded();
        assertTrue(web.isElementVisible(getPageLocator()));
    }

    @Override
    public void init() {
        super.init();
        userName = new Edit(this, generateControlLocator(getNodeByName("username")));
        password = new Edit(this, generateControlLocator(getNodeByName("password")));
        confirmPassword = new Edit(this, generateControlLocator(getNodeByName("confirmpassword")));
        email = new Edit(this, generateControlLocator(getNodeByName("email")));
        gender = new Dropdown(this, generateControlLocator(getNodeByName("gender")));
        captcha = new Edit(this, generateControlLocator(getNodeByName("captcha")));
        createYourAccountBtn = new Button(this, generateControlLocator("input[type='submit']"));
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
                case "Confirm Password": confirmPassword.type(value); break;
                case "Email": email.type(value); break;
                case "Gender": gender.selectValue(value); break;
                case "Captcha": captcha.type(value); break;
                default: throw new IllegalArgumentException("Unknown field: " + key);
            }
        }
    }

    public AccountPage createYourAccount() {
        clickCreateYourAccountBtn();
        unload();
        return new AccountPage(web);
    }

    public void clickCreateYourAccountBtn() {
        createYourAccountBtn.click();
    }

    public String getErrorMessage() {
        return web.findElement(getErrorLocator()).getText().trim();
    }

    private By getErrorLocator() {
        return generateControlLocator(".pasgood");
    }

    private By getPageLocator() {
        return By.cssSelector(ROOT_NODE);
    }
}
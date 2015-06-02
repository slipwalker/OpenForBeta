package sut.factoryComponents;

import org.apache.log4j.Logger;
import sut.pageObjects.BasePage;
import sut.pageObjects.AccountPage;
import sut.pageObjects.SignUpPage;
import webdriver.ExtendedWebDriver;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/*
 * Created by demidovskiy-r on 01.06.2015.
 */
public class PageNavigator extends PageCreator {
    private final Map<Class, String> pageLinksMap = new HashMap<>();
    private final static Logger log = Logger.getLogger(PageNavigator.class);

    public PageNavigator(ExtendedWebDriver webDriver, URL baseUrl) {
        super(webDriver);
        initPageLinksMap(baseUrl);
    }

    private void initPageLinksMap(URL baseUrl) {
        pageLinksMap.put(BasePage.class, baseUrl.toString());
        pageLinksMap.put(SignUpPage.class, baseUrl + "/signup");
        pageLinksMap.put(AccountPage.class, baseUrl + "/account");
    }

    public BasePage getBasePage() {
        navigateTo(BasePage.class);
        return super.getBasePage();
    }

    public SignUpPage getSignUpPage() {
        navigateTo(SignUpPage.class);
        return super.getSignUpPage();
    }

    public AccountPage getAccountPage() {
        navigateTo(AccountPage.class);
        return super.getAccountPage();
    }

    public void navigateTo(Class pageClassToProxy) {
        navigateTo(pageClassToProxy, "");
    }

    public void navigateTo(Class pageClassToProxy, String... urlParam) {
        String desiredUrl = String.format(pageLinksMap.get(pageClassToProxy), urlParam);
        if (!isItCurrentUrl(desiredUrl)) {
            webDriver.get(desiredUrl);
        }
    }

    public boolean isItCurrentUrl(String desiredUrl) {
        return webDriver.getCurrentUrl().equalsIgnoreCase(desiredUrl);
    }
}
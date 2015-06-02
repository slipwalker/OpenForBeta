package sut;

import org.apache.log4j.Logger;
import sut.factoryComponents.PageCreator;
import sut.factoryComponents.PageNavigator;
import webdriver.ExtendedWebDriver;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Created by demidovskiy-r on 01.06.2015.
 */
public class Sut {
    private static Logger log = Logger.getLogger(Sut.class);
    private static Lock webDriverLock = new ReentrantLock();
    private ExtendedWebDriver webDriver;
    private PageNavigator pageNavigator;
    private PageCreator pageCreator;
    private URL siteUrl;
    private ExtendedWebDriver.Browser siteBrowser;
    private String tempFolder;
    private Map<String, Object> browserCapabilityMap;

    public Sut(URL siteUrl, ExtendedWebDriver.Browser desiredBrowser, Map<String, Object> browserCapabilityMap, String tempFolder) {
        this.siteUrl = siteUrl;
        this.siteBrowser = desiredBrowser;
        this.tempFolder = tempFolder;
        this.browserCapabilityMap = browserCapabilityMap;
    }

    public void stop() {
        getWebDriver().quit();
    }

    public ExtendedWebDriver getWebDriver() {
        if (webDriver == null) {
            webDriverLock.lock();
            log.info("Starting browser");
            try {
                webDriver = siteBrowser == ExtendedWebDriver.Browser.CUSTOM_REMOTE_BROWSER
                        ? new ExtendedWebDriver(browserCapabilityMap)
                        : new ExtendedWebDriver(siteBrowser, new File(tempFolder));
            } finally {
                webDriverLock.unlock();
            }
        }
        return webDriver;
    }

    public PageNavigator getPageNavigator() {
        if (pageNavigator == null) {
            pageNavigator = new PageNavigator(getWebDriver(), this.siteUrl);
        }
        return pageNavigator;
    }

    public PageCreator getPageCreator() {
        if (pageCreator == null) {
            pageCreator = new PageCreator(getWebDriver());
        }
        return pageCreator;
    }
}
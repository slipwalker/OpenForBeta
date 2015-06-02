package webdriver;

import com.google.common.base.Function;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.SystemClock;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import static org.openqa.selenium.remote.CapabilityType.HAS_NATIVE_EVENTS;
import static org.openqa.selenium.remote.CapabilityType.SUPPORTS_JAVASCRIPT;
import static org.openqa.selenium.remote.CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR;

/*
 * Created by demidovskiy-r on 01.06.2015.
 */
public class ExtendedWebDriver implements SearchContext, WebDriver {
    private static final long LONG_TIMEOUT_MS = 30 * 1000;
    private static final long SHORT_TIMEOUT_MS = 15 * 1000;
    private static final long PULL_UP_INTERVAL_MS = 100;
    private final WebDriver browserDriver;
    private final File downloadFolder;
    private final Wait<WebDriver> wait;

    private static final Logger log = Logger.getLogger(ExtendedWebDriver.class);

    public ExtendedWebDriver() {
        this(Browser.FIREFOX);
    }

    public ExtendedWebDriver(Browser browser) {
        this(browser, null);
    }

    public ExtendedWebDriver(Browser browser, File downloadFolder) {
        this(browser, downloadFolder, null);
    }

    public ExtendedWebDriver(Map<String, Object> customProps) {
        this(Browser.CUSTOM_REMOTE_BROWSER, null, customProps);
    }

    public ExtendedWebDriver(Browser browser, File downloadFolder, Map<String, Object> customCapability) {
        if (downloadFolder == null) downloadFolder = new File(System.getProperty("java.io.tmpdir"));
        if (!downloadFolder.exists()) downloadFolder.mkdirs();
        this.downloadFolder = downloadFolder;
        this.browserDriver = prepareBrowser(browser, customCapability);

        turnImplicitlyWaitOn();
        this.wait = new WebDriverWait(this.browserDriver, getLongTimeoutSEC(), getPullUpIntervalMS());
        maximizeWindow();
    }

    protected WebDriver prepareBrowser(Browser browser, Map<String, Object> customCapability) {
        WebDriver readyBrowser;
        URL remoteUrl = getRemoteUrlFromSystemProperty();
        log.debug("init " + browser.toString());
        switch (browser) {
            case FIREFOX:
                readyBrowser = new FirefoxDriver(buildFFCapabilities());
                break;
            case CHROME:
                readyBrowser = new ChromeDriver();
                break;
            case IE:
                readyBrowser = new InternetExplorerDriver();
                break;
            case SAFARI:
                readyBrowser = new SafariDriver();
                break;
            case REMOTE_FIREFOX:
                readyBrowser = new RemoteWebDriver(remoteUrl, buildFFCapabilities());
                ((RemoteWebDriver) readyBrowser).setFileDetector(new LocalFileDetector());
                break;
            case REMOTE_IE:
                readyBrowser = new RemoteWebDriver(remoteUrl, DesiredCapabilities.internetExplorer());
                ((RemoteWebDriver) readyBrowser).setFileDetector(new LocalFileDetector());
                break;
            case REMOTE_CHROME:
                readyBrowser = new RemoteWebDriver(remoteUrl, DesiredCapabilities.chrome());
                ((RemoteWebDriver) readyBrowser).setFileDetector(new LocalFileDetector());
                break;
            case REMOTE_SAFARI:
                readyBrowser = new RemoteWebDriver(remoteUrl, DesiredCapabilities.safari());
                ((RemoteWebDriver) readyBrowser).setFileDetector(new LocalFileDetector());
                break;
            case CUSTOM_REMOTE_BROWSER:
                readyBrowser = new RemoteWebDriver(remoteUrl, new DesiredCapabilities(customCapability));
                ((RemoteWebDriver) readyBrowser).setFileDetector(new LocalFileDetector());
                break;
            default:
                readyBrowser = new HtmlUnitDriver();
                break;
        }
        return readyBrowser;
    }

    private URL getRemoteUrlFromSystemProperty() {
        try {
            return new URL(System.getProperty("webDriverGridRemoteUrl", "http://localhost:4444/wd/hub"));
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private DesiredCapabilities buildFFCapabilities() {
        FirefoxProfile profile;
        String customProfileDir = System.getProperty("firefoxCustomProfileDirectory");
        if (customProfileDir != null) {
            profile = new FirefoxProfile(new File(customProfileDir));
        } else {
            profile = new FirefoxProfile();
        }
        //profile.setEnableNativeEvents(true);
        //profile.setAlwaysLoadNoFocusLib(true);
        profile.setPreference("dom.max_chrome_script_run_time", 60);
        profile.setPreference("dom.max_script_run_time", 60);
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir", downloadFolder.toString());
        profile.setPreference("browser.download.defaultFolder", downloadFolder.toString());
        profile.setPreference("browser.helperApps.alwaysAsk.force", false);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/octet-stream,application/x-zip-compressed,application/unknown,video/quicktime");
        profile.setPreference("signed.applets.codebase_principal_support", true);
        DesiredCapabilities capability = DesiredCapabilities.firefox();
        capability.setCapability(FirefoxDriver.PROFILE, profile);
        capability.setCapability(UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
        capability.setCapability(HAS_NATIVE_EVENTS, true);
        capability.setCapability(SUPPORTS_JAVASCRIPT, true);
        capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
        return capability;
    }

    private void maximizeWindow() {
        browserDriver.manage().window().maximize();
    }

    public void get(String url) {
        browserDriver.get(url);
    }

    public String getCurrentUrl() {
        return browserDriver.getCurrentUrl();
    }

    public String getTitle() {
        return browserDriver.getTitle();
    }

    public List<WebElement> findElements(By by) {
        return browserDriver.findElements(by);
    }

    public WebElement findElement(By by) {
        return browserDriver.findElement(by);
    }

    public String getPageSource() {
        return browserDriver.getPageSource();
    }

    public void close() {
        browserDriver.close();
    }

    public void quit() {
        browserDriver.quit();
    }

    public Set<String> getWindowHandles() {
        return browserDriver.getWindowHandles();
    }

    public String getWindowHandle() {
        return browserDriver.getWindowHandle();
    }

    public WebDriver.TargetLocator switchTo() {
        return browserDriver.switchTo();
    }

    public WebDriver.Navigation navigate() {
        return browserDriver.navigate();
    }

    public WebDriver.Options manage() {
        return browserDriver.manage();
    }

    public long getLongTimeoutMS() {
        return LONG_TIMEOUT_MS;
    }

    public long getLongTimeoutSEC() {
        return getLongTimeoutMS() / 1000;
    }

    public long getShortTimeoutMS() {
        return SHORT_TIMEOUT_MS;
    }

    public long getPullUpIntervalMS() {
        return PULL_UP_INTERVAL_MS;
    }

    public JavascriptExecutor getJavascriptExecutor() {
        return (JavascriptExecutor) browserDriver;
    }

    public Boolean waitUntilElementDisappear(By by) {
        return waitUntil(ExpectedConditions.invisibilityOfElementLocated(by), "waitUntilElementDisappear");
    }

    public WebElement waitUntilElementAppearVisible(By by) {
        return waitUntil(ExpectedConditions.visibilityOfElementLocated(by), "waitUntilElementAppearVisible");
    }

    public <V> V waitUntil(Function<? super WebDriver, V> function, String timeoutMessage) {
        turnImplicitlyWaitOff();
        V result;
        try {
            result = wait.until(function);
        } catch (TimeoutException timeException) {
            turnImplicitlyWaitOn();
            throw new TimeoutException(timeException.getMessage() +
                    "\nTimeOut while waitUntil " + timeoutMessage, timeException.getCause());
        }
        turnImplicitlyWaitOn();
        return result;
    }

    public boolean isAlertPresent() {
        try {
            this.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public boolean isElementVisible(By locator) {
        boolean elementVisible = false;
        turnImplicitlyWaitOff();
        if (isElementPresent(locator)) {
            try {
                elementVisible = this.findElement(locator).isDisplayed();
            } catch (WebDriverException e) {
                elementVisible = false;
            }
        }
        turnImplicitlyWaitOn();
        return elementVisible;
    }

    public boolean isElementPresent(By locator) {
        boolean elementPresent;
        turnImplicitlyWaitOff();
        elementPresent = this.findElements(locator).size() > 0;
        turnImplicitlyWaitOn();
        return elementPresent;
    }

    private void turnImplicitlyWaitOff() {
        this.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
    }

    private void turnImplicitlyWaitOn() {
        this.manage().timeouts().implicitlyWait(getShortTimeoutMS(), TimeUnit.MILLISECONDS);
    }

    public File getScreenshotAs(OutputType<File> file) {
        return ((TakesScreenshot) browserDriver).getScreenshotAs(file);
    }

    public boolean getScreenshotAndSaveAs(File file) throws IOException {
        File screenShot = this.getScreenshotAs(OutputType.FILE);
        if (file.isDirectory()) {
            FileUtils.copyFileToDirectory(screenShot, file);
        } else {
            FileUtils.copyFile(screenShot, file);
        }
        FileUtils.forceDelete(screenShot);
        return true;
    }

    public File getDownloadDir() {
        return downloadFolder;
    }

    public String getAlertMessage() {
        String alertMessage = browserDriver.switchTo().alert().getText();
        browserDriver.switchTo().alert().accept();
        return alertMessage;
    }

    public boolean setCheckBoxTo(By by, boolean desiredStatement) {
        WebElement check_box = browserDriver.findElement(by);
        if (check_box.isEnabled()) {
            if (desiredStatement == check_box.isSelected()) {
                return true;
            } else {
                check_box.click();
                return true;
            }
        } else {
            if (desiredStatement == check_box.isSelected()) return true;
        }
        return false;
    }

    public Actions getActions() {
        return new Actions(browserDriver);
    }

    public enum Browser {
        FIREFOX("firefox", "firefox"),
        CHROME("chrome", "chrome"),
        SAFARI("safari", "safari"),
        IE("ie", "internet explorer"),
        REMOTE_FIREFOX("remoteFirefox", "firefox"),
        REMOTE_IE("remoteIE", "internet explorer"),
        REMOTE_CHROME("remoteChrome", "chrome"),
        REMOTE_SAFARI("remoteSafari", "safari"),
        CUSTOM_REMOTE_BROWSER("custom", "custom");

        private final String browser;
        private final String browserName;

        Browser(String browser, String browserName) {
            this.browser = browser;
            this.browserName = browserName;
        }

        @Override
        public String toString() {
            return browser;
        }

        public String getBrowserName() {
            return browserName;
        }

        public static Browser getByValue(String value) {
            for (Browser element : values()) {
                if (element.toString().equalsIgnoreCase(value)) {
                    return element;
                }
            }
            throw new IllegalArgumentException("Unknown browser: " + value);
        }
    }
}
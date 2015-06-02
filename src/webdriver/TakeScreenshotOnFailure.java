package webdriver;

import automation.com.openforbeta.tests.steps.BaseStep;
import jbehave.StoryContext;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.ScenarioType;
import org.jbehave.core.failures.PendingStepFound;
import org.jbehave.core.failures.UUIDExceptionWrapper;
import java.io.File;
import java.text.MessageFormat;

/*
 * Created by demidovskiy-r on 01.06.2015.
 */
public class TakeScreenshotOnFailure extends BaseStep {
    public static final String DEFAULT_SCREEN_SHOT_PATH_PATTERN = "{0}/screenshots/{1}/{2}/failed-scenario-{3}.png";
    protected final File outDirectory;
    protected final ThreadLocal<StoryContext> storyContext;
    protected final String screenshotPathPattern;

    public TakeScreenshotOnFailure(File outDirectory, ThreadLocal<StoryContext> storyContext) {
        this.outDirectory = outDirectory;
        this.storyContext = storyContext;
        this.screenshotPathPattern = DEFAULT_SCREEN_SHOT_PATH_PATTERN;
    }

    @AfterScenario(uponType = ScenarioType.ANY, uponOutcome = AfterScenario.Outcome.FAILURE)
    public void afterScenarioFailureExample(UUIDExceptionWrapper uuidWrappedFailure) throws Exception {
        ExtendedWebDriver driverProvider = getSut().getWebDriver();
        if (uuidWrappedFailure instanceof PendingStepFound) {
            return; // we don't take screen-shots for Pending Steps
        }
        String screenshotPath = screenshotPath(storyContext.get().getCurrentStory().getName(),
                storyContext.get().getCurrentScenario(),
                uuidWrappedFailure.getUUID().toString());
        String currentUrl = driverProvider.getCurrentUrl();
        boolean savedIt = false;
        try {
            savedIt = driverProvider.getScreenshotAndSaveAs(new File(screenshotPath));
        } catch (Exception e) {
            log.error("Screenshot of page '" + currentUrl + ". Will try again. Cause: ", e);
            try {
                savedIt = driverProvider.getScreenshotAndSaveAs(new File(screenshotPath));
            } catch (Exception e1) {
                log.error("Screenshot of page '" + currentUrl + "' has **NOT** been saved to '" + screenshotPath + "' because error '" + e.getMessage() + "' encountered. Stack trace follows:", e);
                return;
            }
        }
        if (savedIt) {
            log.debug("Screenshot of page '" + currentUrl + "' has been saved to '" + screenshotPath + "' with " + new File(screenshotPath).length() + " bytes");
        } else {
            log.debug("Screenshot of page '" + currentUrl + "' has **NOT** been saved. If there is no error, perhaps the WebDriver type you are using is not compatible with taking screenshots");
        }
    }

    protected String screenshotPath(String storyTitle, String scenarioTitle, String uid) {
        return MessageFormat.format(screenshotPathPattern, outDirectory, storyTitle, scenarioTitle, uid);
    }
}
package webdriver;

import automation.com.openforbeta.tests.steps.BaseStep;
import jbehave.StoryContext;
import org.jbehave.core.annotations.AfterStory;

/*
 * Created by demidovskiy-r on 01.06.2015.
 */
public class StopBrowserAfterStory extends BaseStep {
    private ThreadLocal<StoryContext> storyContext;

    public StopBrowserAfterStory(ThreadLocal<StoryContext> storyContext) {
        this.storyContext = storyContext;
    }

    @AfterStory()
    public void stopBrowserAfterStory() {
        log.info("Stop browser after story " + storyContext.get().getCurrentStory().getName());
        try{
            stopSut();
        } catch (Exception e){
            log.error("Error during stopping browser after story",e);
        }
    }
}
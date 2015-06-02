package jbehave;

import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.Narrative;
import org.jbehave.core.model.Story;
import org.jbehave.core.reporters.NullStoryReporter;
import java.util.List;
import java.util.Map;

/*
 * Created by demidovskiy-r on 31.05.2015.
 */
public class StoryContextReporter extends NullStoryReporter {
    private final StoryContext storyContext;

    public StoryContextReporter(ThreadLocal<StoryContext> storyContext) {
        StoryContext currentStoryContext = storyContext.get();
        if (currentStoryContext == null) {
            currentStoryContext = new StoryContext();
            storyContext.set(currentStoryContext);
        }
        this.storyContext = currentStoryContext;
    }

    @Override
    public void narrative(Narrative narrative) {
        storyContext.setCurrentNarrative(narrative);
    }

    @Override
    public void beforeStory(Story story, boolean givenStory) {
        storyContext.setCurrentStory(story);
        storyContext.setIsGiven(givenStory);
    }

    @Override
    public void beforeScenario(String title) {
        storyContext.setCurrentScenario(title);
    }

    @Override
    public void beforeExamples(List<String> steps, ExamplesTable table) {
    }

    @Override
    public void example(Map<String, String> tableRow) {
    }

    @Override
    public void afterExamples() {
    }

    @Override
    public void failed(String step, Throwable storyFailure) {
    }

    @Override
    public void pending(String step) {
    }

    @Override
    public void afterScenario() {

    }

    @Override
    public void afterStory(boolean givenStory) {
    }
}
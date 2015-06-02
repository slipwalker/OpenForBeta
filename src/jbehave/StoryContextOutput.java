package jbehave;

import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;

/*
 * Created by demidovskiy-r on 31.05.2015.
 */
public class StoryContextOutput extends org.jbehave.core.reporters.Format {
    private final ThreadLocal<StoryContext> storyContext;


    public StoryContextOutput(ThreadLocal<StoryContext> storyContext) {
        super("STORY_CONTEXT");
        this.storyContext = storyContext;
    }

    @Override
    public StoryReporter createStoryReporter(FilePrintStreamFactory filePrintStreamFactory, StoryReporterBuilder storyReporterBuilder) {
        return new StoryContextReporter(storyContext);
    }
}
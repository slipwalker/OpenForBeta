package jbehave;

import org.jbehave.core.steps.DelegatingStepMonitor;
import org.jbehave.core.steps.StepMonitor;

/*
 * Created by demidovskiy-r on 31.05.2015.
 */
public class StoryContextMonitor extends DelegatingStepMonitor {
    private final ThreadLocal<StoryContext> storyContext;
    public StoryContextMonitor(StepMonitor delegate, ThreadLocal<StoryContext> storyContext) {
        super(delegate);
        this.storyContext = storyContext;
    }

    @Override
    public void performing(String step, boolean dryRun) {
        super.performing(step, dryRun);
        storyContext.get().setCurrentStep(step);
    }
}
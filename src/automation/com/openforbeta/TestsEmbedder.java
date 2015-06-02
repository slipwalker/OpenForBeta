package automation.com.openforbeta;

import static java.util.Arrays.asList;
import static jbehave.FailedTestsReporter.FAILED_TEST_REPORTER;
import automation.com.openforbeta.tests.steps.LoginSteps;
import automation.com.openforbeta.tests.steps.SignUpSteps;
import automation.com.openforbeta.tests.steps.UserAccountSteps;
import jbehave.StoryContext;
import jbehave.StoryContextOutput;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.jbehave.core.ConfigurableEmbedder;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.failures.PassingUponPendingStep;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.io.LoadFromRelativeFile;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.FreemarkerViewGenerator;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.*;
import webdriver.StopBrowserAfterStory;
import webdriver.TakeScreenshotOnFailure;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML_TEMPLATE;
import static org.jbehave.core.reporters.Format.XML_TEMPLATE;

/*
 * Created by demidovskiy-r on 31.05.2015.
 */
public class TestsEmbedder extends ConfigurableEmbedder {
    private final static Logger log = Logger.getLogger(TestsEmbedder.class);
    private static File configFile;

    public TestsEmbedder(String[] args) {
        if (args.length > 0) {
            configFile = new File(args[0]);
            log.info("Trying to load non default config " + configFile.getAbsolutePath());
            TestsContext.getInstance().loadConfig(configFile);
        }
        log.info("Start tests embedder with args: " + TestsContext.getInstance().toString());
    }

    public TestsEmbedder() {
    }

    static ThreadLocal<StoryContext> storyContext = new ThreadLocal<>();

    @Override
    public Configuration configuration() {

        CrossReference crossReference = new CrossReference();
        crossReference.withThreadSafeDelegateFormat(new StoryContextOutput(storyContext));

        StoryReporterBuilder reporterBuilder = new StoryReporterBuilder()
                .withCodeLocation(codeLocationFromClass(this.getClass()))
                .withMultiThreading(true)
                .withFailureTrace(true)
                .withFailureTraceCompression(false)
                .withDefaultFormats()
                .withFormats(CONSOLE, HTML_TEMPLATE, XML_TEMPLATE)
                .withFormats(FAILED_TEST_REPORTER)
                .withCrossReference(crossReference);

        Configuration jBehaveConfiguration = new MostUsefulConfiguration();
        jBehaveConfiguration
                .useStoryControls(new StoryControls().doDryRun(false).doSkipScenariosAfterFailure(false).doResetStateBeforeStory(true))
                .useFailureStrategy(new PassingUponPendingStep())
                .useKeywords(new LocalizedKeywords())
                .useStoryParser(new RegexStoryParser())
                .usePendingStepStrategy(new PassingUponPendingStep())
                .useStepCollector(new MarkUnmatchedStepsAsPending(new StepFinder(new StepFinder.ByPriorityField())))
                .useViewGenerator(new FreemarkerViewGenerator())
                .useStoryReporterBuilder(reporterBuilder)
                .useStoryLoader(getLocalStoryLoader())
                .useParameterControls(new ParameterControls().useDelimiterNamedParameters(true))
                .useStepPatternParser(new RegexPrefixCapturingPatternParser());

        return jBehaveConfiguration;
    }

    private StoryLoader getLocalStoryLoader() {
        return getLocalStoryLoader(getStoriesFolder());
    }

    private StoryLoader getLocalStoryLoader(String relativePath) {
        try {
            return new LoadFromRelativeFile(new URL("file://" + relativePath.replace(" ", "%20")));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("wrong path", e);
        }
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), ArrayUtils.addAll(storySteps(), beforeAfterSteps()));
    }

    public static Object[] storySteps() {
        return new Object[]{
                new LoginSteps(),
                new SignUpSteps(),
                new UserAccountSteps()
        };
    }

    public Object[] beforeAfterSteps() {
        return new Object[]{
                new StopBrowserAfterStory(storyContext),
                new TakeScreenshotOnFailure(
                        configuration().storyReporterBuilder().outputDirectory(),
                        storyContext)
        };
    }

    public static String getStoriesFolder() {
        String STORIESPATH = TestsContext.getInstance().storiesPath;
        return System.getProperty("user.dir") + "/" + STORIESPATH;
    }

    public List<String> storyPaths() {
        List<String> listOfDesiredStories = asList(TestsContext.getInstance().storiesList);
        List<String> listOfResolvedDesiredStories = new ArrayList<>();
        String storyFilterRaw = System.getProperty("tests.story_filter","").trim();
        if (!storyFilterRaw.isEmpty()) {
            log.info("Gor story filter " + storyFilterRaw);
            List<String> listOfStoryFilter = asList(storyFilterRaw.split(";"));
            for (String storyFilter: listOfStoryFilter){
                for (String desiredStory : listOfDesiredStories) {
                    if (desiredStory.matches(storyFilter) && !listOfResolvedDesiredStories.contains(desiredStory + ".story")) {
                        listOfResolvedDesiredStories.add(desiredStory + ".story");
                    }
                }
            }
        } else {
            for (String desiredStory : listOfDesiredStories) {
                listOfResolvedDesiredStories.add(desiredStory + ".story");
            }
        }
        log.info("List of result stories" + ArrayUtils.toString(listOfResolvedDesiredStories));
        return listOfResolvedDesiredStories;
    }

    protected List<String> metaFilters() {
        return asList(TestsContext.getInstance().storiesMeta);
    }

    public void run() throws Throwable {
        Embedder embedder = configuredEmbedder();
        embedder.useMetaFilters(metaFilters());
        embedder.embedderControls().doIgnoreFailureInStories(true);
        embedder.embedderControls().doGenerateViewAfterStories(false);
        embedder.embedderControls().useThreads(TestsContext.getInstance().threads);
        embedder.embedderControls().useStoryTimeoutInSecs(24 * 60 * 60); //24h
        try {
            embedder.runStoriesAsPaths(storyPaths());
        } finally {
            embedder.generateCrossReference();
            if(TestsContext.getInstance().testsGenerateLocalReports != null && TestsContext.getInstance().testsGenerateLocalReports ) {
                embedder.generateReportsView();
            }
        }
    }
}
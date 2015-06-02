package automation.com.openforbeta;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import utils.ConfigLoader;
import utils.ConfigParam;
import utils.IO;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/*
 * Created by demidovskiy-r on 31.05.2015.
 */
public class TestsContext {
    @ConfigParam("application_url")
    public URL applicationURL;

    @ConfigParam("stories.path")
    public String storiesPath;

    @ConfigParam(value = "stories.list", splitBy = ",")
    public String[] storiesList;

    @ConfigParam(value = "stories.meta_filter", splitBy = ",")
    public String[] storiesMeta;

    @ConfigParam("tests.threads")
    public Integer threads;

    @ConfigParam("tests.browser")
    public String browser;

    @ConfigParam("tests.generate_local_reports")
    public Boolean testsGenerateLocalReports;

    @ConfigParam("tests.temp_folder")
    public String testsTempFolder;

    private Map<String,Object> map = new HashMap<>();

    private final static TestsContext TESTS_CONTEXT = new TestsContext();

    private TestsContext() {
        String path = "resources/default.properties";
        loadConfig(new File(path));
    }

    public static TestsContext getInstance() {
        return TESTS_CONTEXT;
    }

    public Map<String,Object> asMap(){
        return map;
    }

    public void loadConfig(File propertiesFile) {
        Properties props = IO.getProperties(propertiesFile);
        new ConfigLoader() {
            @Override
            public <T> String getConfigValue(T source, String configField) {
                return ((Properties) source).getProperty(configField);
            }
        }.load(this, props);
        for (final String name: props.stringPropertyNames())
            map.put(name, props.getProperty(name));
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(getInstance(), ToStringStyle.MULTI_LINE_STYLE);
    }
}
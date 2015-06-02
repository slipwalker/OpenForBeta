package automation.com.openforbeta.tests.steps;

import automation.com.openforbeta.TestsContext;
import org.apache.log4j.Logger;
import org.jbehave.core.model.ExamplesTable;
import sut.Sut;
import utils.Gen;
import webdriver.ExtendedWebDriver.Browser;
import java.util.HashMap;
import java.util.Map;

/*
 * Created by demidovskiy-r on 01.06.2015.
 */
public class BaseStep {
    protected final static Logger log = Logger.getLogger(BaseStep.class);
    private static ThreadLocal<Sut> sut = new ThreadLocal<>();

    public static TestsContext getContext() {
        return TestsContext.getInstance();
    }

    public static Sut getSut() {
        Sut currentSut = sut.get();
        if (currentSut == null) {
            currentSut = new Sut(getContext().applicationURL,
                    Browser.getByValue(getContext().browser),
                    extractWebdriverCapability(getContext().asMap()),
                    getContext().testsTempFolder);
            sut.set(currentSut);
        }
        return currentSut;
    }

    protected static void stopSut() {
        Sut currentSut = sut.get();
        if (currentSut != null) {
            currentSut.stop();
            sut.remove();
        }
    }

    private static Map<String, Object> extractWebdriverCapability(Map<String, Object> allMyProps) {
        String capabilityPrefix = "webdriver.";
        Map<String, Object> resultMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : allMyProps.entrySet()) {
            if (entry.getKey().startsWith(capabilityPrefix)) {
                resultMap.put(entry.getKey().substring(capabilityPrefix.length()), entry.getValue());
            }
        }
        return resultMap;
    }

    protected static Map<String, String> parametrizeTabularRow(ExamplesTable table) {
        return parametrizeTabularRow(table, 0);
    }

    protected static Map<String, String> parametrizeTabularRow(ExamplesTable table, int rowNumber) {
        Map<String, String> row = table.getRow(rowNumber);
        for (Map.Entry<String, String> rowEntry : row.entrySet()) {
            String key = rowEntry.getKey();
            String value = rowEntry.getValue();
            if (value.contains("<") && value.endsWith(">")) {
                String newValue = table.getRowAsParameters(rowNumber, true).valueAs(key, String.class);
                row.put(key, newValue.replaceFirst("<", "").substring(0, newValue.length() - 2));
            }
        }
        return row;
    }

    public static String wrapVariableWithTestSession(String variable) {
        String sessionId = Gen.getString(5);
        if (variable == null || variable.isEmpty()) return "";
        if (variable.contains(sessionId)) return variable;
        return variable + sessionId;
    }
}
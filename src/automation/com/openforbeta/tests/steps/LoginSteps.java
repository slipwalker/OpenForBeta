package automation.com.openforbeta.tests.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import sut.pageObjects.BasePage;
import sut.pageObjects.LoginForm;

/*
 * Created by demidovskiy-r on 31.05.2015.
 */
public class LoginSteps extends BaseStep {

    private BasePage openBasePage() {
        return getSut().getPageNavigator().getBasePage();
    }

    private BasePage getBasePage() {
        return getSut().getPageCreator().getBasePage();
    }

    private LoginForm getLoginForm() {
        return getBasePage().getLoginForm();
    }

    @Given("{I |}am on main OpenForBeta system page")
    public BasePage goToBaseSystemPage() {
        return openBasePage();
    }

    // | User Name | Password |
    @When("{I |}fill following fields on Login form: $fieldsTable")
    public void fillLoginForm(ExamplesTable fieldsTable) {
        getLoginForm().fill(parametrizeTabularRow(fieldsTable));
    }

    @When("{I |}login to system")
    public void loginToSystem() {
        getLoginForm().login();
    }
}
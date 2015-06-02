package automation.com.openforbeta.tests.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import sut.pageObjects.SignUpPage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/*
 * Created by demidovskiy-r on 02.06.2015.
 */
public class SignUpSteps extends LoginSteps {

    private SignUpPage openSignUpPage() {
        return getSut().getPageNavigator().getSignUpPage();
    }

    private SignUpPage getSignUpPage() {
        return getSut().getPageCreator().getSignUpPage();
    }

    @Given("{I |}go to Sign up page")
    public SignUpPage goToSignUpPage() {
        return openSignUpPage();
    }

    // | User Name | Password | Confirm Password | Email | Gender | Captcha |
    @When("{I |}fill following fields on Sign up page: $fieldsTable")
    public void fillSignUpForm(ExamplesTable fieldsTable) {
        getSignUpPage().fill(parametrizeTabularRow(fieldsTable));
    }

    @When("{I |}create your account on Sign Up page")
    public void createYourAccount() {
        getSignUpPage().createYourAccount();
    }

    @When("{I |}click Create your account button on Sign Up page")
    public void clickCreateYourAccountBtn() {
        getSignUpPage().clickCreateYourAccountBtn();
    }

    @Then("{I |}should see following captcha error '$errorMessage' on Sign Up page")
    public void checkCaptchaError(String errorMessage) {
        assertThat("Check error invalid captcha: " , getSignUpPage().getErrorMessage(), equalTo(errorMessage));
    }
}
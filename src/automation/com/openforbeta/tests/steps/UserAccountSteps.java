package automation.com.openforbeta.tests.steps;

import org.jbehave.core.annotations.Then;
import sut.pageObjects.AccountPage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/*
 * Created by demidovskiy-r on 02.06.2015.
 */
public class UserAccountSteps extends LoginSteps {

    private AccountPage getAccountPage() {
        return getSut().getPageCreator().getAccountPage();
    }

    @Then("{I |}should see following user name '$userName' on users Account page")
    public void checkAccountsUserName(String userName) {
        assertThat("Check user name: " , getAccountPage().getUserName(), equalTo(userName));
    }
}
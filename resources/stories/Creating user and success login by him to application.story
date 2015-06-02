Feature: Check registration and login to application
Narrative:
In order to:
As a Guest
I want to check registration and login to application

Scenario: Check registration a new user on OpenForBeta
Given I am on main OpenForBeta system page
And go to Sign up page
When I fill following fields on Sign up page:
| User Name       | Password | Confirm Password | Email              | Gender | Captcha |
| Dummy User Name | Welcome1 | Welcome1         | dummyuser@test.com | Male   | 12345   |
And click Create your account button on Sign Up page
Then I should see following captcha error 'Error: Image code wrong, please try again.' on Sign Up page

Scenario: Check login by an existing user to his account on OpenForBeta
Given I am on main OpenForBeta system page
When I fill following fields on Login form:
| User Name               | Password |
| Dummy User Name Another | Welcome1 |
And login to system
Then I should see following user name 'Dummy User Name Another' on users Account page
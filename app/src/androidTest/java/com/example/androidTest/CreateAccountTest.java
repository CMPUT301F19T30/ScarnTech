//package com.example.androidTest;
//
//import androidx.test.espresso.action.TypeTextAction;
//import androidx.test.espresso.action.ViewActions;
//import androidx.test.filters.LargeTest;
//import androidx.test.rule.ActivityTestRule;
//import androidx.test.runner.AndroidJUnit4;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import cmput301.moodi.R;
//import cmput301.moodi.ui.createAccount.CreateAccountActivity;
//import cmput301.moodi.ui.login.LoginActivity;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//
///**
// * Class: CreateAccountTest
// * Instrumented test for creation of an account
// * @see < href="https://developer.android.com/training/testing/espresso/recipes">Common UI Tests</a>
// * @see < href="https://developer.android.com/training/testing/espresso/recipes">Variety of Matcher, ViewAction, and ViewAssertions </a>
// */
//
//@RunWith(AndroidJUnit4.class)
//@LargeTest
//public class CreateAccountTest {
//    private String ExistingEmail, ExistingPasssword;
//    private String NewUsername, NewEmail, NewFirstName, NewLastName, NewPassword;
//
//    @Rule
//    public ActivityTestRule<CreateAccountActivity> activityRule
//            = new ActivityTestRule<>(CreateAccountActivity.class);
//
//    @Before
//    public void initValidString() {
//        // Existing User Input
//        ExistingEmail = "jarrod.j.los@gmail.com";
//        ExistingPasssword = "Password";
//
//        // New User Creation
//        NewUsername = "NourishedManatee";
//        NewEmail = "scarntech301@gmail.com";
//        NewFirstName = "ui";
//        NewLastName = "testing";
//        NewPassword = "Password";
//    }
//    /*
//     *  Attempts to create an account with no entries
//     */
//    @Test
//    public void clickCreateAccountButtonNoEntries_remainsOnLoginActivity() {
//        onView(withId(R.id.signup_button)).perform(click());
//        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
//    }
//
//    /*
//     *  Create account with a missing field: Username
//     */
//    @Test
//    public void clickCreateAccountButtonMissingUsername_remainsOnCreateAccountActivity() {
//
//        onView(withId(R.id.signup_firstName)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewFirstName));
//        onView(withId(R.id.signup_lastName)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewLastName));
//        onView(withId(R.id.signup_email)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewEmail));
//        onView(withId(R.id.signup_password)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewPassword)).perform(closeSoftKeyboard());
//
//        onView(withId(R.id.signup_button)).perform(click());
//        onView(withId(R.id.signup_button)).check(matches(isDisplayed()));
//    }
//
//    /*
//     *  Create account with a missing field: Username
//     */
//    @Test
//    public void clickCreateAccountButtonMissingFirstName_remainsOnCreateAccountActivity() {
//
//        onView(withId(R.id.signup_username)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewUsername));
//        onView(withId(R.id.signup_lastName)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewLastName));
//        onView(withId(R.id.signup_email)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewEmail));
//        onView(withId(R.id.signup_password)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewPassword)).perform(closeSoftKeyboard());
//
//        onView(withId(R.id.signup_button)).perform(click());
//        onView(withId(R.id.signup_button)).check(matches(isDisplayed()));
//    }
//    /*
//     *  Create account with a missing field: Username
//     */
//    @Test
//    public void clickCreateAccountButtonMissingLastName_remainsOnCreateAccountActivity() {
//
//        onView(withId(R.id.signup_username)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewUsername));
//        onView(withId(R.id.signup_firstName)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewFirstName));
//        onView(withId(R.id.signup_email)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewEmail));
//        onView(withId(R.id.signup_password)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewPassword)).perform(closeSoftKeyboard());
//
//        onView(withId(R.id.signup_button)).perform(click());
//        onView(withId(R.id.signup_button)).check(matches(isDisplayed()));
//    }
//    /*
//     *  Create account with a missing field: Username
//     */
//    @Test
//    public void clickCreateAccountButtonMissingEmail_remainsOnCreateAccountActivity() {
//
//        onView(withId(R.id.signup_username)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewUsername));
//        onView(withId(R.id.signup_firstName)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewFirstName));
//        onView(withId(R.id.signup_lastName)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewLastName));
//        onView(withId(R.id.signup_password)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewPassword)).perform(closeSoftKeyboard());
//
//        onView(withId(R.id.signup_button)).perform(click());
//        onView(withId(R.id.signup_button)).check(matches(isDisplayed()));
//    }
//    /*
//     *  Create account with a missing field: Username
//     */
//    @Test
//    public void clickCreateAccountButtonMissingPassword_remainsOnCreateAccountActivity() {
//
//        onView(withId(R.id.signup_username)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewUsername));
//        onView(withId(R.id.signup_firstName)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewFirstName));
//        onView(withId(R.id.signup_lastName)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewLastName));
//        onView(withId(R.id.signup_email)).perform(ViewActions.clearText())
//                .perform(new TypeTextAction(NewEmail)).perform(closeSoftKeyboard());
//
//        onView(withId(R.id.signup_button)).perform(click());
//        onView(withId(R.id.signup_button)).check(matches(isDisplayed()));
//    }
//
//
//}
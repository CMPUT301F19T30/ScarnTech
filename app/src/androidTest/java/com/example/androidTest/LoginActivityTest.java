package com.example.androidTest;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.action.TypeTextAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import cmput301.moodi.R;
import cmput301.moodi.ui.login.LoginActivity;

import static android.os.Trace.isEnabled;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Class: LoginActivityTest
 * Instrumented test for Login Activity
 * @see < href="https://developer.android.com/training/testing/espresso/recipes">Common UI Tests</a>
 * @see < href="https://developer.android.com/training/testing/espresso/recipes">Variety of Matcher, ViewAction, and ViewAssertions </a>
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    private String ExistingEmail, ExistingPasssword;
    private String NewUsername, NewEmail, NewName, NewLastName;

    @Rule
    public ActivityTestRule<LoginActivity> activityRule
            = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void initValidString() {
        // Existing User Input
        ExistingEmail = "jarrod.j.los@gmail.com";
        ExistingPasssword = "Password";

        // New User Creation
        NewUsername = "NourishedManatee";
        NewEmail = "saveTheManatees@gmail.com";
        NewName = "Oil";
        NewLastName = "Sands";
    }

    /*
     *  Clicks Login with no enteries and check if display is still on login
     */
    @Test
    public void clickLoginButtonNoEntries_remainsOnLoginActivity() {
        onView(withId(R.id.login_button)).perform(click());
//        onView(withId(R.id.textView7)).perform(click());
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));

    }

    /*
     *  Clicks Login with invalid entry and check if display is still on login
     */
    @Test
    public void clickLoginButtonInvalidEntries_remainsOnLoginActivity() {

        onView(withId(R.id.login_email)).perform(ViewActions.clearText())
                .perform(new TypeTextAction(NewUsername));
        onView(withId(R.id.login_password)).perform(ViewActions.clearText())
                .perform(new TypeTextAction(ExistingPasssword)).perform(closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

    /*
     *  Clicks Login with valid entry and checks if display has transitioned to home fragment
     */
    @Test
    public void clickLoginButtonValidEntries_gotoHomeFragment() {
        onView(withId(R.id.login_email)).perform(ViewActions.clearText())
                .perform(new TypeTextAction(ExistingEmail));
        onView(withId(R.id.login_password)).perform(ViewActions.clearText())
                .perform(new TypeTextAction(ExistingPasssword)).perform(closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

    /*
     *  Clicks Create New Account and checks if display has transitioned to Create Account Activity
     */
    @Test
    public void clickCreateNewAccount_gotoNewAccountActivity() {
        onView(withId(R.id.signup_text)).perform(click());
        onView(withId(R.id.register_form)).check(matches(isDisplayed()));
    }
}

package com.group15.seg2105finalproject;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.group15.seg2105finalproject.main.Registration;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {
    @Rule
    public ActivityTestRule<Registration> mRegistraionRule =
            new ActivityTestRule<>(Registration.class);

    @Test
    public void testNoFirstname() {
        Espresso.onView(withId(R.id.registerUser)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.firstName)).check(matches(hasErrorText("Must enter a first name.")));
    }

    @Test
    public void testInvalidFirstname() {
        Espresso.onView(withId(R.id.firstName)).perform(
                ViewActions.typeText("first123"));
        Espresso.onView(withId(R.id.lastName)).perform(
                ViewActions.typeText("last"),
                ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.registerUser)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.firstName)).check(matches(hasErrorText("Must not enter numbers or special characters.")));
    }

    @Test
    public void testNoLastname() {
        Espresso.onView(withId(R.id.firstName)).perform(
                ViewActions.typeText("first"),
                ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.registerUser)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.lastName)).check(matches(hasErrorText("Must enter a last name.")));
    }

    @Test
    public void testInvalidLastname() {
        Espresso.onView(withId(R.id.firstName)).perform(ViewActions.typeText("first"));
        Espresso.onView(withId(R.id.lastName)).perform(
                ViewActions.typeText("last1234"),
                ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.registerUser)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.lastName)).check(matches(hasErrorText("Must not enter numbers or special characters.")));
    }

    @Test
    public void testNoUsername() {
        Espresso.onView(withId(R.id.firstName)).perform(ViewActions.typeText("first"));
        Espresso.onView(withId(R.id.lastName)).perform(ViewActions.typeText("last"));
        Espresso.onView(withId(R.id.email)).perform(
                ViewActions.typeText("email@email.com"),
                ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.registerUser)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.username)).check(matches(hasErrorText("Must enter a username.")));
    }

    @Test
    public void testNoEmail() {
        Espresso.onView(withId(R.id.firstName)).perform(ViewActions.typeText("first"));
        Espresso.onView(withId(R.id.lastName)).perform(ViewActions.typeText("last"));
        Espresso.onView(withId(R.id.username)).perform(
                ViewActions.typeText("user"),
                ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.registerUser)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.email)).check(matches(hasErrorText("Must enter an email.")));
    }

    @Test
    public void testInvalidEmail() {
        Espresso.onView(withId(R.id.firstName)).perform(ViewActions.typeText("first"));
        Espresso.onView(withId(R.id.lastName)).perform(ViewActions.typeText("last"));
        Espresso.onView(withId(R.id.email)).perform(
                ViewActions.typeText("email"));
        Espresso.onView(withId(R.id.username)).perform(
                ViewActions.typeText("user"),
                ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.registerUser)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.email)).check(matches(hasErrorText("Must enter a valid email.")));
    }

    @Test
    public void testNoPassword() {
        Espresso.onView(withId(R.id.firstName)).perform(ViewActions.typeText("first"));
        Espresso.onView(withId(R.id.lastName)).perform(ViewActions.typeText("last"));
        Espresso.onView(withId(R.id.email)).perform(ViewActions.typeText("email@email.com"));
        Espresso.onView(withId(R.id.username)).perform(
                ViewActions.typeText("username"),
                ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.registerUser)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.password)).check(matches(hasErrorText("Must enter a password.")));
    }

    @Test
    public void testShortPassword() {
        Espresso.onView(withId(R.id.firstName)).perform(ViewActions.typeText("first"));
        Espresso.onView(withId(R.id.lastName)).perform(ViewActions.typeText("last"));
        Espresso.onView(withId(R.id.email)).perform(ViewActions.typeText("email@email.com"));
        Espresso.onView(withId(R.id.username)).perform(ViewActions.typeText("username"));
        Espresso.onView(withId(R.id.password)).perform(
                ViewActions.typeText("bla"),
                ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.registerUser)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.password)).check(matches(hasErrorText("Password must be at least 7 characters long.")));
    }

    @Test
    public void testInvalidPassword() {
        Espresso.onView(withId(R.id.firstName)).perform(ViewActions.typeText("first"));
        Espresso.onView(withId(R.id.lastName)).perform(ViewActions.typeText("last"));
        Espresso.onView(withId(R.id.email)).perform(ViewActions.typeText("email@email.com"));
        Espresso.onView(withId(R.id.username)).perform(ViewActions.typeText("username"));
        Espresso.onView(withId(R.id.password)).perform(
                ViewActions.typeText("blablabla"),
                ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.registerUser)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.password)).check(matches(hasErrorText("Password must contain numbers and/or special characters.")));
    }
}
package com.example.androiduitesting;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.espresso.intent.Intents;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShowActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUpIntents() { Intents.init(); }

    @After
    public void tearDownIntents() { Intents.release(); }

    private void addCity(String name) {
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_name))
                .perform(ViewActions.typeText(name), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.button_confirm)).perform(click());
    }

    private void clickFirstListItem() {
        onData(is(instanceOf(String.class)))
                .inAdapterView(withId(R.id.city_list))
                .atPosition(0)
                .perform(click());
    }

    // 1) Check whether the activity correctly switched
    @Test
    public void testSwitchToShowActivity() {
        addCity("Edmonton");
        clickFirstListItem();
        intended(hasComponent(hasClassName(ShowActivity.class.getName())));
    }

    // 2) Test whether the city name is consistent
    @Test
    public void testCityNameDisplayedMatchesTapped() {
        addCity("Vancouver");
        clickFirstListItem();
        onView(withId(R.id.text_city_name))
                .check(matches(withText("Vancouver")));
    }

    // 3) Test the "back" button
    @Test
    public void testBackButtonReturnsToMain() {
        addCity("Calgary");
        clickFirstListItem();
        onView(withId(R.id.button_back)).perform(click());
        // A view unique to MainActivity should be visible again
        onView(withId(R.id.button_add)).check(matches(isDisplayed()));
    }
}
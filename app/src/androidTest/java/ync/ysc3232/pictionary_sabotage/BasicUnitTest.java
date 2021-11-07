package ync.ysc3232.pictionary_sabotage;

import android.view.View;

import androidx.annotation.ContentView;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import androidx.test.filters.LargeTest;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.hamcrest.core.AllOf.allOf;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BasicUnitTest {
    public static final String STRING_TO_BE_TYPED = "some_email@gmail.com";

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes. This is a replacement for {@link androidx.test.rule.ActivityTestRule}.
     */
    @Rule public ActivityScenarioRule<LoginPage> activityScenarioRule
            = new ActivityScenarioRule<>(LoginPage.class);

    @Test
    public void playButtonGoesToRoom() {
        // Click on Play button
        onView(withId(R.id.startGameButton))
                .perform(click());

        //Check if view change by checking if we can see button createRoom
        onView(withId(R.id.createRoom)).check(matches(isDisplayed()));
    }

    @Test
    public void createRoomButtonGoesToWaitingRoom() {
        onView(withId(R.id.createRoom)).perform(click());
        onView(withId(R.id.startGame)).check(matches(isDisplayed()));
    }

    

}

package ync.ysc3232.pictionary_sabotage;

import android.view.View;

import androidx.annotation.ContentView;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import androidx.test.filters.LargeTest;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
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
        onView(withId(R.id.startGameButton)).perform(click());

        //Check if view change by checking if we can see button createRoom
        onView(withId(R.id.createRoom)).check(matches(isDisplayed()));
    }

    @Test
    public void createRoomButtonGoesToWaitingRoom() {
        onView(withId(R.id.startGameButton)).perform(click());
        onView(withId(R.id.createRoom)).perform(click());
        onView(withId(R.id.startGame)).check(matches(isDisplayed()));
    }

    @Test
    public void joinRoomButtonGoesToWaitingRoom() {
        onView(withId(R.id.startGameButton)).perform(click());

        //Enter some random room that exists on database but is not started
        onView(withId(R.id.enterRoomId)).perform(typeText("6047"));
        onView(withId(R.id.joinRoom)).perform(click());
        onView(withId(R.id.startGame)).check(matches(isDisplayed()));
    }

    @Test
    public void playerChoosesRoles(){
        onView(withId(R.id.startGameButton)).perform(click());
        onView(withId(R.id.createRoom)).perform(click());

        //Check can select Guesser
        onView(withId(R.id.chooseRole1)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Guesser"))).perform(click());
        onView(withId(R.id.chooseRole1)).check(matches(withSpinnerText(containsString("Guesser"))));

        //Check can select Saboteur
        onView(withId(R.id.chooseRole1)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Saboteur"))).perform(click());
        onView(withId(R.id.chooseRole1)).check(matches(withSpinnerText(containsString("Saboteur"))));

        //Check can select Drawer
        onView(withId(R.id.chooseRole1)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Drawer"))).perform(click());
        onView(withId(R.id.chooseRole1)).check(matches(withSpinnerText(containsString("Drawer"))));
    }

//    @Test
//    public void LoginPage() {
//        onView(withId(R.id.setEmail)).perform(typeText("jack@gmail.com"));
//        onView(withId(R.id.setEmail)).perform(typeText("njsrukfagdk"));
//        onView(withId(R.id.LoginButton)).perform(click());
//
//        //Check if view change by checking if we can see button createRoom
//        onView(withId(R.id.startGameButton)).check(matches(isDisplayed()));
//    }

}

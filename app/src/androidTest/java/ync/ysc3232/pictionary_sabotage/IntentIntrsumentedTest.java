package ync.ysc3232.pictionary_sabotage;

import androidx.annotation.ContentView;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.espresso.Espresso;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class IntentIntrsumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test void verifyPlayLeadsToRoom() {
//        Espresso.onView(R.id.);


//        // Types a message into a EditText element.
//        onView(withId(R.id.edit_message))
//                .perform(typeText(MESSAGE), closeSoftKeyboard());
//
//        // Clicks a button to send the message to another
//        // activity through an explicit intent.
//        onView(withId(R.id.send_message)).perform(click());
//
//        // Verifies that the DisplayMessageActivity received an intent
//        // with the correct package name and message.
//        intended(allOf(
//                hasComponent(hasShortClassName(".DisplayMessageActivity")),
//                toPackage(PACKAGE_NAME),
//                hasExtra(MainActivity.EXTRA_MESSAGE, MESSAGE)));
    }
}

package com.example.firebasecalendar;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import androidx.test.espresso.intent.Intents;

public class FrontPageActivityUITest {

    @Rule
    public ActivityTestRule<FrontPageActivity> activityTestRule =
            new ActivityTestRule<>(FrontPageActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testStartButton() throws InterruptedException {
        // Type values into EditText fields
        Espresso.onView(ViewMatchers.withId(R.id.front_phone)).perform(ViewActions.
                typeText("000-000-0000"));
        try {
            Espresso.closeSoftKeyboard();
            Thread.sleep(1000); // 1 second delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.front_name)).perform(ViewActions.
                typeText("Test Name"));
        try {
            Espresso.closeSoftKeyboard();
            Thread.sleep(1000); // 1 second delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.front_id)).perform(ViewActions.
                typeText("00000"));

        // Close soft keyboard
        Espresso.closeSoftKeyboard();

        // Click the "Start" button
        Espresso.onView(ViewMatchers.withId(R.id.btn_start)).perform(ViewActions.click());

        // Verify that the intent to start MainActivity is correctly created
        Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
    }

}

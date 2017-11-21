package com.electrolux.converter.currencyconverter;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Instrumented test, which will execute on an Android device.
 * Should be device in on state
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)

public class ConverterInstrumentedTest {

	@Rule
	public ActivityTestRule<MainActivity> mActivityRule =
			new ActivityTestRule<>(MainActivity.class);
	public MainActivity mainActivity;
	public UiDevice uiDevice;

	@Before
	public void setActivity() throws Exception {
/*		uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
		Point[] coordinates = new Point[4];
		coordinates[0] = new Point(248, 1520);
		coordinates[1] = new Point(248, 929);
		coordinates[2] = new Point(796, 1520);
		coordinates[3] = new Point(796, 929);
		try {
			if (!uiDevice.isScreenOn()) {
				uiDevice.wakeUp();
				uiDevice.swipe(coordinates, 10);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
*/
		mainActivity = mActivityRule.getActivity();
	}

	@Test
	public void calculateAppContext() throws Exception {

	//select "RUB"
		onView(withId(R.id.spinner1))            // withId(R.id.my_view) is a ViewMatcher
				.perform(click());               // click() is a ViewAction
		onData(allOf(is(instanceOf(String.class)), is("RUB"))).perform(click());
		Thread.currentThread().sleep(500);

	//select "EUR"
		onView(withId(R.id.spinner2))            // withId(R.id.my_view) is a ViewMatcher
				.perform(click());               // click() is a ViewAction
		onData(allOf(is(instanceOf(String.class)))).atPosition(8).perform(click());

		Thread.currentThread().sleep(500);
		onView(withId(R.id.currency1))            // withId(R.id.my_view) is a ViewMatcher
					.perform(typeText("50"), closeSoftKeyboard());
		Thread.currentThread().sleep(2000);
		onView(withId(R.id.currency2))
					.check(matches(withText(containsString("0.7128"))));

	}

}

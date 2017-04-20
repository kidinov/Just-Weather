package org.kidinov.w_app.weather.view;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kidinov.w_app.R;

import io.realm.Realm;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.kidinov.w_app.util.EspressoUtil.withRecyclerView;

@RunWith(AndroidJUnit4.class)
public class WeatherActivityTest {
    @Rule
    public ActivityTestRule<WeatherActivity> activityTestRule =
            new ActivityTestRule<>(WeatherActivity.class, false, false);

    @Test
    public void addedCitiesBecameVisibleInList() {
        activityTestRule.launchActivity(WeatherActivity.startActivityForTesting());

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(x -> realm.deleteAll());

        String[] cities = new String[]{"Amsterdam", "Berlin", "London", "New York", "Voronezh"};
        for (String cityName : cities) {
            addCity(cityName);
        }

        int position = 0;
        for (String cityName : cities) {
            onView(withId(R.id.recycler_view))
                    .perform(RecyclerViewActions.scrollToPosition(position));
            onView(withRecyclerView(R.id.recycler_view).atPosition(position))
                    .check(matches(hasDescendant(withText(Matchers.startsWith(cityName)))));
            position++;
        }
    }

    private void addCity(String cityName) {
        onView(withId(R.id.add_city_button))
                .perform(click());
        onView(withText(R.string.add_city))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        onView(withId(R.id.city_name_edit_text)).perform(typeText(cityName));
        onView(withText("OK")).perform(click());
    }
}

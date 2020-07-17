package com.practice.newscollector.ui.news_list;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.practice.newscollector.R;
import com.practice.newscollector.rule.NewsFragmentRule;
import com.practice.newscollector.test_utils.TestInstruments;
import com.practice.newscollector.ui.MainActivity;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import java.util.Objects;

import androidx.test.espresso.ViewInteraction;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.practice.newscollector.test_utils.TestInstruments.waitTime;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

public class NewsListFragmentTest {
    @Rule
    public NewsFragmentRule rule = new NewsFragmentRule(MainActivity.class);

    @Test
    public void viewsAreShownTest() {
        onView(withId(R.id.rvNews)).check(matches(isDisplayed()));
        waitTime(2000);
        ViewInteraction view = onView(allOf(withId(R.id.rvNews), isDisplayed()));
        for (int i = 0; i < 20; i++) {
            view.perform(scrollToPosition(i));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(withId(R.id.tvTitle)))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(withId(R.id.tvDate)))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(withId(R.id.tvSource)))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(withId(R.id.ivImage)))));
        }
    }

    @Test
    public void noInternetToastIsShownTest() {
        changeWiFiStatus(false);
        onView(withText(R.string.turn_on_internet))
                .inRoot(withDecorView(not(Matchers.is(rule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        changeWiFiStatus(true);
    }

    @Test
    public void refreshingBySwipeTest() {
        onView(withId(R.id.swipeRefresh))
                .perform(TestInstruments.withCustomConstraints(swipeDown(), isDisplayed()));
    }

    public void changeWiFiStatus(Boolean enable) {
        WifiManager wifiManager = (WifiManager) rule.getActivity().getSystemService(Context.WIFI_SERVICE);
        Objects.requireNonNull(wifiManager).setWifiEnabled(enable);
        if(enable){
            //wait for switching connection
            waitTime(5000);
        }
    }
}

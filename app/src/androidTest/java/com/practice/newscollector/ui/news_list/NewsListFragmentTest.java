package com.practice.newscollector.ui.news_list;

import com.practice.newscollector.R;
import com.practice.newscollector.rule.NewsFragmentRule;
import com.practice.newscollector.test_utils.TestInstruments;
import com.practice.newscollector.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.ViewInteraction;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.practice.newscollector.test_utils.TestInstruments.waitTime;
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
            waitTime(100);
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(withId(R.id.tvTitle)))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(withId(R.id.tvDate)))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(withId(R.id.tvSource)))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(withId(R.id.ivImage)))));
        }
    }

    @Test
    public void refreshingBySwipeTest() {
        onView(withId(R.id.swipeRefresh))
                .perform(TestInstruments.withCustomConstraints(swipeDown(), isDisplayed()));
    }

}

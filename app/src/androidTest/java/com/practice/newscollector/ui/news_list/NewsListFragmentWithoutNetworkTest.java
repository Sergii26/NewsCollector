package com.practice.newscollector.ui.news_list;

import com.practice.newscollector.R;
import com.practice.newscollector.rule.MockUtilsRule;
import com.practice.newscollector.ui.MainActivity;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

public class NewsListFragmentWithoutNetworkTest {

    @Rule
    public MockUtilsRule rule = new MockUtilsRule(MainActivity.class);

    @Test
    public void noInternetToastIsShownTest() {
        onView(withText(R.string.turn_on_internet))
                .inRoot(withDecorView(not(Matchers.is(rule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }
}

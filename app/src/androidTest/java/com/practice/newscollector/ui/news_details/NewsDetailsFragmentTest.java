package com.practice.newscollector.ui.news_details;

import com.practice.newscollector.R;
import com.practice.newscollector.model.dao.ArticleSchema;
import com.practice.newscollector.rule.NewsFragmentRule;
import com.practice.newscollector.test_utils.TestInstruments;
import com.practice.newscollector.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.practice.newscollector.test_utils.TestInstruments.waitTime;
import static org.hamcrest.core.AllOf.allOf;

public class NewsDetailsFragmentTest {
    @Rule
    public NewsFragmentRule rule = new NewsFragmentRule(MainActivity.class);

    @Test
    public void openRightArticleTest() {
        waitTime(2000);
        List<ArticleSchema> articles = rule.getArticles();
        String title = articles.get(3).getTitle();
        onView(allOf(withId(R.id.rvNews), isDisplayed()))
                .check(matches(TestInstruments.atPosition(3, hasDescendant(withId(R.id.tvTitle)))))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
        waitTime(500);
        onView(withId(R.id.tvArticleTitle)).check(matches(withText(title)));
    }

    @Test
    public void rightArticlesOrderOnNewsDetailsList(){
        waitTime(2000);
        List<ArticleSchema> articles = rule.getArticles();
        onView(allOf(withId(R.id.rvNews), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        waitTime(500);
        ViewInteraction view = onView(allOf(withId(R.id.rvDetails), isDisplayed()));
        for(int i = 0; i < 20; i ++){
            String title = articles.get(i).getTitle();
            waitTime(200);
            onView(withText(title)).check(matches(isDisplayed()));
            view.perform(swipeLeft());
        }
    }

}

package com.example.newsmvvm.ui


import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.newsmvvm.R
import com.example.newsmvvm.util.EspressoIdlingResourceRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
class FeedFragmentTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = ActivityScenarioRule(NewsActivity::class.java)

    @get: Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    val itemInTest = 1

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
    }

    @Test
    fun test_isNewsItemsVisibleOnAppLaunch() {
        onView(withId(R.id.rvNews))
            .check(matches(isDisplayed()))

        onView(withId(R.id.progressBar))
            .check(matches(CoreMatchers.not(isDisplayed())))
    }

    @Test
    fun test_selectNewsItem_isDetailFragmentVisible() {
        onView(withId(R.id.rvNews))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    itemInTest,
                    ViewActions.click()
                )
            )

        // Confirm nav to DetailFragment
        onView(withId(R.id.webView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_backNavigation_toFeedFragment() {

        onView(withId(R.id.rvNews))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    itemInTest,
                    ViewActions.click()
                )
            )

        // Confirm nav to DetailFragment and display webview
        onView(withId(R.id.webView)).check(matches(isDisplayed()))

        Espresso.pressBack()

        // Confirm FeedFragment in view
        onView(withId(R.id.rvNews)).check(matches(isDisplayed()))
    }

    @After
    fun cleanup() {
        Intents.release()
    }
}
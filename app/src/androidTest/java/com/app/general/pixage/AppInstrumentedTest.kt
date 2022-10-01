package com.app.general.pixage

import android.support.test.uiautomator.UiDevice
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.app.general.pixage.images.presentation.ImagePostsActivity
import com.app.general.pixage.images.presentation.components.ImagePostViewHolder
import junit.framework.AssertionFailedError
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AppInstrumentedTest {
    private lateinit var mUiDevice: UiDevice

    @get:Rule
    val imagePostsActivityRule: ActivityScenarioRule<ImagePostsActivity> =
        ActivityScenarioRule(ImagePostsActivity::class.java)

    @Before
    fun setup() {
        mUiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    /**
     * Test app's normal flow when the connectivity requirements are met
     */
    @Test
    fun testAppFlowA() {

        Espresso.onView(withId(R.id.rvImagePosts))
            .waitUntilVisible(5000)
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ImagePostViewHolder>(
                    0,
                    ViewActions.click()
                )
            )

        Espresso.onView(withId(android.R.id.button2))
            .inRoot(RootMatchers.isDialog())
            .waitUntilVisible(50000)
            .perform(ViewActions.click())

        Espresso.onView(withId(R.id.rvImagePosts))
            .waitUntilVisible(5000)
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ImagePostViewHolder>(
                    0,
                    ViewActions.click()
                )
            )

        Espresso.onView(withId(android.R.id.button1))
            .inRoot(RootMatchers.isDialog())
            .waitUntilVisible(50000)
            .perform(ViewActions.click())

        Espresso.onView(withId(R.id.ivLikes)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.ivComments)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.ivDownloads)).check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.tvLikes)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.tvComments)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.tvDownloads)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.tvTagList)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.tvUserName)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.ivImage)).check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.tvDownloads)).check(matches(isDisplayed()))

        mUiDevice.pressBack()

        Espresso.onView(withId(R.id.item_menu_search))
            .waitUntilVisible(5000)
            .perform(ViewActions.click())

        Espresso.onView(withId(R.id.searchView))
            .check(matches(isDisplayed()))
            .perform(typeSearchViewText("animals"))

        mUiDevice.pressBack()
    }

    /**
     * Wait for view to be visible
     */
    private fun ViewInteraction.waitUntilVisible(timeout: Long): ViewInteraction {
        val startTime = System.currentTimeMillis()
        val endTime = startTime + timeout

        do {
            try {
                check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                return this
            } catch (e: AssertionFailedError) {
                Thread.sleep(50)
            }
        } while (System.currentTimeMillis() < endTime)

        throw TimeoutException()
    }

    /**
     * For Entering text in SearchView
     */
    private fun typeSearchViewText(text: String): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Change view text"
            }

            override fun getConstraints(): Matcher<View> {
                return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as SearchView).setQuery(text, false)
            }
        }
    }
}
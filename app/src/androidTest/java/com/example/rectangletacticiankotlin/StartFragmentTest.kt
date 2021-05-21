package com.example.rectangletacticiankotlin

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test

class StartFragmentTest {

    @Test
    fun onClick() {
        launchFragmentInContainer {
            StartFragment(OnFragmentListenerStub())
        }
        onView(withId(R.id.startButton)).perform(click())
        onView(withId(R.id.regulationsButton)).perform(click())
        onView(withId(R.id.settingsButton)).perform(click())
        //pressBack()
    }
}

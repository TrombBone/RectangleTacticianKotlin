package com.example.rectangletacticiankotlin

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test

class PlayerStartFragmentTest {

    @Test
    fun onTouch() {
        launchFragmentInContainer {
            PlayerStartFragment(OnFragmentListenerStub(), MyAppData().playerNumber)
        }
        onView(withId(R.id.playerNotificationTV)).perform(click())
    }
}
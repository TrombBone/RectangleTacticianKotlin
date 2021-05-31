package com.example.rectangletacticiankotlin

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Test

class SettingsFragmentTest {

    @Test
    fun settings() {
        launchFragmentInContainer {
            SettingsFragment(OnFragmentListenerStub(), SettingsData())
        }
        // Не понял, как нажать на SwitchButton

        onView(withId(R.id.fieldWidthTIET)).perform(typeText("40"))
        onView(withId(R.id.fieldWidthTIET)).check(matches(withText("40")))
    }
}
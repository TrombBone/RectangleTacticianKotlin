package com.example.rectangletacticiankotlin

import androidx.fragment.app.testing.launchFragmentInContainer
import org.junit.Test

class SettingsFragmentTest {

    @Test
    fun settings() {
        launchFragmentInContainer {
            StartFragment(OnFragmentListenerStub())
        }

//        onView(withId(R.id.fieldWidthTIET)).perform(typeText("40"))
//        pressBack()
//        onView(withId(R.id.fieldWidthTIET)).check(matches(withText("40")))
    }
}
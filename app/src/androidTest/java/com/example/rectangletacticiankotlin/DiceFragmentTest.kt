package com.example.rectangletacticiankotlin

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Test

class DiceFragmentTest {

    @Test
    fun onClick() {
        val diceFragment = DiceFragment(OnFragmentListenerStub())
        launchFragmentInContainer {
            diceFragment
        }
        onView(withId(R.id.generateSidesButton)).perform(click())
        onView(withId(R.id.widthSideTV)).check(matches(withText("${diceFragment.sideWidthTV.text}")))
        onView(withId(R.id.heightSideTV)).check(matches(withText("${diceFragment.sideHeightTV.text}")))
    }
}
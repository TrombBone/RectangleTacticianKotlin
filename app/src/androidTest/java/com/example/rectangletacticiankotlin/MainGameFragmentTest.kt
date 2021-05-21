package com.example.rectangletacticiankotlin

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Test

class MainGameFragmentTest {// Process crashed. All test terminate

    private val allData = MyAppData()

    @Test
    fun exceptionTVNoException() {
        val mainGameFragment = MainGameFragment(OnFragmentListenerStub(), allData)
        launchFragmentInContainer {
            mainGameFragment
        }
        mainGameFragment.exceptionTVNoException()
        onView(withId(R.id.exceptionTV)).check(matches(withText(R.string.exceptionTV_good_text)))
    }

    @Test
    fun exceptionTVNoRectangle() {
        val mainGameFragment = MainGameFragment(OnFragmentListenerStub(), allData)
        launchFragmentInContainer {
            mainGameFragment
        }
        mainGameFragment.exceptionTVNoRectangle()
        onView(withId(R.id.exceptionTV)).check(matches(withText(R.string.exceptionTV_bad_NoRectangle_text)))
    }

    @Test
    fun exceptionTVOutOfBounds() {
        val mainGameFragment = MainGameFragment(OnFragmentListenerStub(), allData)
        launchFragmentInContainer {
            mainGameFragment
        }
        mainGameFragment.exceptionTVOutOfBounds()
        onView(withId(R.id.exceptionTV)).check(matches(withText(R.string.exceptionTV_bad_OutOfBoundsException_text)))
    }

    @Test
    fun exceptionTVLocation() {
        val mainGameFragment = MainGameFragment(OnFragmentListenerStub(), allData)
        launchFragmentInContainer {
            mainGameFragment
        }
        mainGameFragment.exceptionTVLocation()
        onView(withId(R.id.exceptionTV)).check(matches(withText(R.string.exceptionTV_bad_LocationException_text)))
    }

    @Test
    fun exceptionTVLocationFirstRect() {
        val mainGameFragment = MainGameFragment(OnFragmentListenerStub(), allData)
        launchFragmentInContainer {
            mainGameFragment
        }
        mainGameFragment.exceptionTVLocationFirstRect()
        onView(withId(R.id.exceptionTV)).check(matches(withText(R.string.exceptionTV_bad_LocationException_firstRect_text)))
    }

    @Test
    fun exceptionTVNotFreeSpace() {
        val mainGameFragment = MainGameFragment(OnFragmentListenerStub(), allData)
        launchFragmentInContainer {
            mainGameFragment
        }
        mainGameFragment.exceptionTVNoFreeSpace()
        onView(withId(R.id.exceptionTV)).check(matches(withText(R.string.exceptionTV_neutral_NotFreeSpaceException_text)))
    }

    @Test
    fun onClick() {
        launchFragmentInContainer {
            MainGameFragment(OnFragmentListenerStub(), allData)
        }
        onView(withId(R.id.rotationButton)).perform(click())
        onView(withId(R.id.nextTurnButton)).perform(click())
    }
}
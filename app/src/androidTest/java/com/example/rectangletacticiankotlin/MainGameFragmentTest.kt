package com.example.rectangletacticiankotlin

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Test

class MainGameFragmentTest {// Process crashed. All test terminate

    private val allDataStub = MyAppDataStub()

    @Test
    fun exceptionTVNoRectangle() {
        val mainGameFragment = MainGameFragment(allDataStub)
        launchFragmentInContainer {
            mainGameFragment
        }
        mainGameFragment.exceptionTVNoRect()
        onView(withId(R.id.exceptionTV)).check(matches(withText(R.string.exceptionTV_bad_NoRectangle_text)))
    }

    @Test
    fun exceptionTVNoException() {
        val mainGameFragment = MainGameFragment(allDataStub)
        launchFragmentInContainer {
            mainGameFragment
        }
        //надо пставить прямоугольник
        mainGameFragment.exceptionTVNoException()
        onView(withId(R.id.exceptionTV)).check(matches(withText(R.string.exceptionTV_good_text)))
    }

    @Test
    fun exceptionTVOutOfBounds() {
        val mainGameFragment = MainGameFragment(allDataStub)
        launchFragmentInContainer {
            mainGameFragment
        }
        //надо поставить прямоугольник за границами
        mainGameFragment.exceptionTVOutOfBounds()
        onView(withId(R.id.exceptionTV)).check(matches(withText(R.string.exceptionTV_bad_OutOfBoundsException_text)))
    }

    @Test
    fun exceptionTVLocation() {
        val mainGameFragment = MainGameFragment(allDataStub)
        launchFragmentInContainer {
            mainGameFragment
        }
        //надо поставить не первый прямоугольник либо внутри другого, либо не прикасаясь ни к кому
        mainGameFragment.exceptionTVLocation()
        onView(withId(R.id.exceptionTV)).check(matches(withText(R.string.exceptionTV_bad_LocationException_text)))
    }

    @Test
    fun exceptionTVLocationFirstRect() {
        val mainGameFragment = MainGameFragment(allDataStub)
        launchFragmentInContainer {
            mainGameFragment
        }
        //поставить первый прямоугольник не в своём углу
        mainGameFragment.exceptionTVLocationFirstRect()
        onView(withId(R.id.exceptionTV)).check(matches(withText(R.string.exceptionTV_bad_LocationException_firstRect_text)))
    }

    @Test
    fun exceptionTVNotFreeSpace() {
        val mainGameFragment = MainGameFragment(allDataStub)
        launchFragmentInContainer {
            mainGameFragment
        }
        //заставить всю полощадь прямоугольниками
        mainGameFragment.exceptionTVNoFreeSpace()
        onView(withId(R.id.exceptionTV)).check(matches(withText(R.string.exceptionTV_neutral_NotFreeSpaceException_text)))
    }

    @Test
    fun onClick() {
        launchFragmentInContainer {
            MainGameFragment(allDataStub)
        }
        onView(withId(R.id.rotationButton)).perform(click())
        onView(withId(R.id.nextTurnButton)).perform(click())
    }
}
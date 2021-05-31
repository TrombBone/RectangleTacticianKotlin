package com.example.rectangletacticiankotlin

import android.app.Dialog
import android.view.View
import androidx.fragment.app.testing.launchFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.junit.Test

class ConfirmStartDialogFragmentTest {

    @Test
    fun confirmStartDialogFragmentTest() {
        with(launchFragment { ConfirmStartDialogFragment(SettingsData()) }) {
            onFragment { fragment ->
                assertThat(fragment.dialog, object : BaseMatcher<Dialog?>(){
                    override fun describeTo(description: Description?) {}

                    override fun matches(item: Any?): Boolean {
                        return (item as? Dialog) != null
                    }

                })
//                assertThat(fragment.requireDialog().isShowing).isTrue()
//                fragment.dismiss()
//                fragment.parentFragmentManager.executePendingTransactions()
//                assertThat(fragment.dialog).isNull()
            }
        }

        onView(withText(R.string.no)).check(ViewAssertions.matches(object : BaseMatcher<View>(){
            override fun describeTo(description: Description?) {}

            override fun matches(item: Any?): Boolean {
                //val a = 3
                //check button
                return false
            }
        }))
    }
}
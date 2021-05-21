package com.example.rectangletacticiankotlin

import androidx.fragment.app.testing.launchFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Test

class ConfirmStartDialogFragmentTest {

    @Test
    fun confirmStartDialogFragmentTest() {
        with(launchFragment<ConfirmStartDialogFragment>()) {
            onFragment { fragment ->
//                assertThat(fragment.dialog).isNotNull()
//                assertThat(fragment.requireDialog().isShowing).isTrue()
//                fragment.dismiss()
//                fragment.parentFragmentManager.executePendingTransactions()
//                assertThat(fragment.dialog).isNull()
            }
        }

        // Assumes that the dialog had a button
        // containing the text "Cancel".
        onView(withText(R.string.no)).check(doesNotExist())
    }
}
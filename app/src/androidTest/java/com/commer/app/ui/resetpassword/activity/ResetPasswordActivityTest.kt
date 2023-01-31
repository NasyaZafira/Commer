package com.commer.app.ui.resetpassword.activity

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.commer.app.R
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ResetPasswordActivityTest : TestCase() {

    private lateinit var scenario: ActivityScenario<ResetPasswordActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(ResetPasswordActivity::class.java)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun testResetPassword() {
        val email = "test"

        onView(withId(R.id.editEmail)).perform(typeText(email))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.txtErrorHandlingEmail)).check(matches(withText(R.string.something_wrong_email)))
        onView(withId(R.id.editEmail))
            .perform(typeText("@commit.com"))
            .check(matches(withText("test@commit.com")))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.btnNext)).perform(click())
    }

}
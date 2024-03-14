package com.mariana.harmonia


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class InicioSesionTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun inicioSesionTest() {
        val appCompatEditText = onView(
            allOf(
                withId(R.id.editTextEmail),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.mainActivity),
                        2
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(click())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.editTextEmail),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.mainActivity),
                        2
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("gggg@gmail.com"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(withId(R.id.editTextContrasena),
            childAtPosition(
                childAtPosition(
                    withId(R.id.mainActivity),
                    2
                ),
                4
            ),
            isDisplayed()
        ))
        appCompatEditText3.perform(replaceText("Abcde123"), closeSoftKeyboard())

        val appCompatButton = onView(
            allOf(
                withId(R.id.botonIniciarSesion), withText("INICIAR SESI�N"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.mainActivity),
                        2
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}

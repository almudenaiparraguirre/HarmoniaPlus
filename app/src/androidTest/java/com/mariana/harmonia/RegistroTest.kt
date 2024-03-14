package com.mariana.harmonia

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class RegistroTest {
    @Test
    fun test_Navegacion_A_RegistroActivity() {
        // Lanzar la actividad
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        // Esperar a que la actividad esté lista
        activityScenario.onActivity {
            // Interactuar con la interfaz de usuario dentro de este bloque
            onView(withId(R.id.pruebaTests)).perform(click())
            onView(withId(R.id.registroActivity)).check(matches(isDisplayed()))
        }
    }
}
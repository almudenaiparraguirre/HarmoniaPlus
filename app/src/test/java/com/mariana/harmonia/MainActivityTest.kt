package com.mariana.harmonia

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mariana.harmonia.activitys.iniciarSesion.RegistroActivity
import com.mariana.harmonia.models.entity.User
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.time.Month

class MainActivityTest {

    @Test
    fun irRegistrate() {
    }
}

class AuthManager(private val firebaseAuth: FirebaseAuth) {
    fun isUserAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }
}

class AuthManagerTest {

    private lateinit var authManager: AuthManager
    private lateinit var firebaseAuth: FirebaseAuth

    @Before
    fun setup() {
        firebaseAuth = mock(FirebaseAuth::class.java)
        authManager = AuthManager(firebaseAuth)
    }

    @Test
    fun testIsUserAuthenticated_UserAuthenticated_ReturnsTrue() {
        val fakeUser = mock(FirebaseUser::class.java)
        `when`(firebaseAuth.currentUser).thenReturn(fakeUser)

        assertTrue(authManager.isUserAuthenticated())
    }

    @Test
    fun testIsUserAuthenticated_NoUserAuthenticated_ReturnsFalse() {
        `when`(firebaseAuth.currentUser).thenReturn(null)

        assertFalse(authManager.isUserAuthenticated())
    }
}
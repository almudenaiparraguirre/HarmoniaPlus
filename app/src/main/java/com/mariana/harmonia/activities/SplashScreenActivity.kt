package com.mariana.harmonia.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mariana.harmonia.MainActivity
import com.mariana.harmonia.R

/**
 * Actividad de pantalla de inicio que se muestra al iniciar la aplicación.
 */
class SplashScreenActivity : AppCompatActivity() {

    /**
     * Se llama cuando se crea la actividad.
     *
     * @param savedInstanceState Estado de la instancia guardada.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition{
            true

        }
        setContentView(R.layout.splash_screen_activity)
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)
    }
}
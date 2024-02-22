package com.mariana.harmonia.activitys

import android.content.ContentValues.TAG
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mariana.harmonia.MainActivity
import com.mariana.harmonia.NivelesAventuraActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.interfaces.PlantillaActivity
import com.mariana.harmonia.pruebasActivity

class EligeModoJuegoActivity : AppCompatActivity(), PlantillaActivity {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var nombreModoDeJuegoTextView: TextView

    companion object {
        private const val PREFS_NAME = "MyPrefsFile"
        private const val SESSION_KEY = "isSessionActive"


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.elige_modo_juego_activity)

        firebaseAuth = FirebaseAuth.getInstance()
        obtenerNombreModoDeJuego()

        // Inicialización de vistas
        val progressBar = findViewById<ProgressBar>(R.id.progressBarCarga)
        val porcentajeTextView = findViewById<TextView>(R.id.porcentajeTextView)
        nombreModoDeJuegoTextView = findViewById(R.id.nombreModoDeJuego)

        // Puedes actualizar el porcentaje directamente
        val porcentaje = 50 // ajusta esto a tu valor real de porcentaje
        progressBar.progress = porcentaje
        porcentajeTextView.text = "$porcentaje%"

        // Inicialización de la animación
        val imageView: ImageView = findViewById(R.id.fondoImageView)
        val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        imageView.startAnimation(anim)
    }

    fun menu_perfil(view: View){
        val intent = Intent(this, PerfilUsuarioActivity::class.java)
        startActivity(intent)
    }

    fun cerrarSesion(view: View) {
        firebaseAuth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity() // Cierra todas las actividades anteriores
    }

    fun clickOpciones(view: View){
        val intent = Intent(this, ConfiguracionActivity::class.java)
        startActivity(intent)
    }

    fun irModoAventura(view: View){
        val intent = Intent(this, NivelesAventuraActivity::class.java)
        startActivity(intent)
    }

    fun irDesafio(view: View){
        val intent = Intent(this, pruebasActivity::class.java)
        startActivity(intent)
    }

    private fun obtenerNombreModoDeJuego() {
        val currentUser = firebaseAuth.currentUser
        val emailFire = currentUser?.email
        // Suponiendo que tengas el email del usuario almacenado en una variable llamada "email"
        val email = emailFire?.replace(".", ",") //

        try {
            UserDao.getUserField(email, "name",
                onSuccess = { name ->
                    runOnUiThread {
                        nombreModoDeJuegoTextView.text = name as? CharSequence ?: ""

                    }
                }
            ) { exception ->
                Log.e(
                    TAG,
                    "Error al obtener el nombre del modo de juego: ${exception.message}",
                    exception
                )
                nombreModoDeJuegoTextView.text = "fallo"

            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al obtener el nombre del modo de juego: ${e.message}", e)
            nombreModoDeJuegoTextView.text = "fallo"
        }
    }

}

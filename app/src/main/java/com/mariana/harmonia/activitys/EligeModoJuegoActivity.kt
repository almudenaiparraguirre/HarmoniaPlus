package com.mariana.harmonia.activitys

import android.content.ContentValues.TAG
import android.content.Intent
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

class EligeModoJuegoActivity : AppCompatActivity(), PlantillaActivity {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var nombreModoDeJuegoTextView: TextView

    companion object {
        private const val PREFS_NAME = "MyPrefsFile"
        private const val SESSION_KEY = "isSessionActive"
    }

    // FUN --> OnCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.elige_modo_juego_activity)
        //colorearTexto(this, R.id.titleTextView)
        firebaseAuth = FirebaseAuth.getInstance()
        Utilidades.colorearTexto(this, R.id.titleTextView)
        nombreModoDeJuegoTextView = findViewById(R.id.nombreModoDeJuego)
        // Llamar al método para obtener el nombre del modo de juego y actualizar el TextView
        obtenerNombreModoDeJuego()

        //porcentaje barra Experiencia

        val progressBar = findViewById<ProgressBar>(R.id.progressBarCarga)
        val porcentajeTextView = findViewById<TextView>(R.id.porcentajeTextView)

        // Puedes actualizar el porcentaje directamente
        val porcentaje = 50 // ajusta esto a tu valor real de porcentaje
        progressBar.progress = porcentaje
        porcentajeTextView.text = "$porcentaje%"


        val imageView: ImageView = findViewById(R.id.fondoImageView)

        // Cargar la animación desde el archivo XML
        val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)

        // Aplicar la animación al ImageView
        imageView.startAnimation(anim)
    }

    // FUN --> Ir al perfil del usuario
    fun menu_perfil(view: View){
        val intent = Intent(this, PerfilUsuarioActivity::class.java)
        startActivity(intent)
    }

    // FUN --> Vuelve a la pantalla de inicio de sesión
    fun cerrarSesion(view: View) {
        firebaseAuth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity() // Cierra todas las actividades anteriores
    }
    // FUN --> Ir a la pantalla de opciones
    fun clickOpciones(view: View){
        val intent = Intent(this, ConfiguracionActivity::class.java)
        startActivity(intent)
    }

    // FUN --> Ir a los niveles de aventura
    fun irModoAventura(view: View){
        val intent = Intent(this, NivelesAventuraActivity::class.java)
        startActivity(intent)
    }

    private fun obtenerNombreModoDeJuego() {
        // Suponiendo que tengas el email del usuario almacenado en una variable llamada "email"
        val email = "pruebabd1@gmail,com" //
        try {
            UserDao.getUserField(email, "name",
                onSuccess = { name ->
                    runOnUiThread {
                        nombreModoDeJuegoTextView.text = name as? CharSequence ?: ""

                    }
                },
                onFailure = { exception ->
                    Log.e(TAG, "Error al obtener el nombre del modo de juego: ${exception.message}", exception)
                    nombreModoDeJuegoTextView.text = "fallo"

                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al obtener el nombre del modo de juego: ${e.message}", e)
            nombreModoDeJuegoTextView.text = "fallo"
        }
    }

}

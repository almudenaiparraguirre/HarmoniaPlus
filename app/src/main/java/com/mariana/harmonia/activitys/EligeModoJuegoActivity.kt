package com.mariana.harmonia.activitys

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.mariana.harmonia.InicioSesionActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.activitys.iniciarSesion.NivelesAventuraActivity
import com.mariana.harmonia.interfaces.PlantillaActivity


class EligeModoJuegoActivity : AppCompatActivity(), PlantillaActivity {

    private lateinit var firebaseAuth: FirebaseAuth
    companion object {
        private const val PREFS_NAME = "MyPrefsFile"
        private const val SESSION_KEY = "isSessionActive"
    }

    // FUN --> OnCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elige_modo_juego)
        //colorearTexto(this, R.id.titleTextView)
        firebaseAuth = FirebaseAuth.getInstance()


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
        val intent = Intent(this, InicioSesionActivity::class.java)
        startActivity(intent)
        finishAffinity() // Cierra todas las actividades anteriores
    }
    // FUN --> Ir a la pantalla de opciones
    fun clickOpciones(view: View){
        val intent = Intent(this, ConfiguracionActivity::class.java)
        startActivity(intent)
    }

    // FUN --> Ir a los niveles de aventura
    fun irModoAventura(){
        val intent = Intent(this, NivelesAventuraActivity::class.java)
        startActivity(intent)
    }
}
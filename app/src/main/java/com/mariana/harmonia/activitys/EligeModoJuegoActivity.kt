package com.mariana.harmonia.activitys

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mariana.harmonia.MainActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.interfaces.PlantillaActivity
import com.mariana.harmonia.utils.Utils
import kotlinx.coroutines.runBlocking

class EligeModoJuegoActivity : AppCompatActivity(), PlantillaActivity {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var nombreTextView: TextView
    private lateinit var porcentajeTextView: TextView
    private lateinit var imageViewFotoPerfil: ImageView
    private lateinit var progressBar: ProgressBar


    companion object {
        private const val PREFS_NAME = "MyPrefsFile"
        private const val SESSION_KEY = "isSessionActive"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.elige_modo_juego_activity)
        Utils.isExternalStorageWritable()
        Utils.isExternalStorageReadable()
        progressBar = findViewById<ProgressBar>(R.id.progressBarCarga)
        porcentajeTextView = findViewById<TextView>(R.id.porcentajeTextView)
        nombreTextView = findViewById(R.id.nombreModoDeJuego)
        imageViewFotoPerfil = findViewById(R.id.imageViewFotoPerfil)
        val imageView: ImageView = findViewById(R.id.fondoImageView)
        inicializarConBase()
        val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        imageView.startAnimation(anim)
        inicilalizarVariablesThis()
    }
    fun inicializarConBase()= runBlocking {
        nombreTextView.text = Utils.getNombre()
        porcentajeTextView.text = (Utils.getExperiencia()!!.toInt()/100).toString()
        progressBar.progress = Utils.getExperiencia()!!.toInt()%100

    }

    private fun inicilalizarVariablesThis() {
        Utilidades.colorearTexto(this, R.id.cerrarSesion)
        Utilidades.colorearTexto(this, R.id.titleTextView)
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        Utils.serializeImage(this,R.mipmap.img_gema)
        imageViewFotoPerfil.setImageBitmap(Utils.deserializeImage(this,"/storage/emulated/0/Download/imagenSerializada.json"))
    }

    fun menu_perfil(view: View){
        mediaPlayer.start()
        val intent = Intent(this, PerfilUsuarioActivity::class.java)
        startActivity(intent)
    }

    fun cerrarSesion(view: View) {
        mediaPlayer.start()
        firebaseAuth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity() // Cierra todas las actividades anteriores
    }

    fun clickOpciones(view: View){
        mediaPlayer.start()

        val intent = Intent(this, ConfiguracionActivity::class.java)
        startActivity(intent)
    }

    fun irModoAventura(view: View){
        mediaPlayer.start()
        val intent = Intent(this, NivelesAventuraActivity::class.java)
        startActivity(intent)
    }

    fun irDesafio(view: View){
        mediaPlayer.start()
        val intent = Intent(this, JuegoMusicalActivity::class.java)
        intent.putExtra("desafio", true)
        startActivity(intent)
    }
}
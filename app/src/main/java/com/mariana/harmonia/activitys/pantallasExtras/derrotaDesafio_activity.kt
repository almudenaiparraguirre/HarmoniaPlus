package com.mariana.harmonia.activitys.pantallasExtras

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.mariana.harmonia.activitys.JuegoMusicalActivity
import com.mariana.harmonia.activitys.NivelesAventuraActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.utils.Utils
import com.mariana.harmonia.utils.UtilsDB
import kotlinx.coroutines.runBlocking

class derrotaDesafio_activity : AppCompatActivity() {
    private var nivel: Int = 0
    private var notasHacertadas: Int = 0
    private var tiempoDurado: Int = 0
    private lateinit var textViewResultadoTotal: TextView
    private lateinit var textViewResultadoTiempo: TextView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var finTextView: TextView
    private lateinit var emogiTextView: TextView
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_derrota_desafio)
        //variablesIntent
        nivel = intent.getIntExtra("numeroNivel", 999)
        notasHacertadas = intent.getIntExtra("notasHacertadas", 0)
        tiempoDurado = intent.getIntExtra("tiempoDurado", 0)

        val emojis = arrayOf("😄", "😃", "😁", "😊", "😆")

        textViewResultadoTotal = findViewById(R.id.textViewResultadoTotal)
        textViewResultadoTiempo = findViewById(R.id.textViewResultadoTiempo)
        emogiTextView = findViewById(R.id.emogiTextView)
        finTextView = findViewById(R.id.desafioTextView)

        var tiempo = ((tiempoDurado-60)*-1)

        //Colorar datos
        emogiTextView.text = emojis.random().toString()
        textViewResultadoTotal.text = "Total: $notasHacertadas"
        textViewResultadoTiempo.text = "Tiempo: $tiempo s"
        Utils.degradadoTexto(this, finTextView.id, R.color.morado, R.color.negro)

        //sonido
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)

       //sonido Finalizar
        mediaPlayer = MediaPlayer.create(this, R.raw.desafio_finish_sound)
        mediaPlayer.setVolume(0.5f,0.5f);
        mediaPlayer.start()

        val imageView: ImageView = findViewById(R.id.fondoImageView)
        val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.animacion_pantallas_fin)
        imageView.startAnimation(anim)

        actualizarDatosInterfaz()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun actualizarDatosInterfaz() = runBlocking {
        val puntuacion = Pair(notasHacertadas, (60 - tiempoDurado))
        val puntuacionDB = UtilsDB.getPuntuacionDesafio()
        val mayorHaciertos = puntuacionDB?.first ?:0
        UtilsDB.setPuntuacionDesafio(puntuacion)
        UtilsDB.setPuntuacionDesafioGlobal(puntuacion)
    }

    fun irRepetir(view: View) {
        mediaPlayer.start()
        val intent = Intent(this, JuegoMusicalActivity::class.java)
        intent.putExtra("desafio", true)
        finish()
        startActivity(intent)
    }
    fun irMenu(view: View) {
        mediaPlayer.start()
        val intent = Intent(this, NivelesAventuraActivity::class.java)
        finish()
        startActivity(intent)
    }
}
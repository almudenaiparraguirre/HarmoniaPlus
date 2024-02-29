package com.mariana.harmonia.activitys.pantallasExtras

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.mariana.harmonia.activitys.JuegoMusicalActivity
import com.mariana.harmonia.activitys.NivelesAventuraActivity
import com.mariana.harmonia.R

class derrotaDesafio_activity : AppCompatActivity() {
    private var nivel: Int = 0
    private var notasHacertadas: Int = 0
    private var tiempoDurado: Int = 0
    private lateinit var textViewResultados: TextView
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        nivel = intent.getIntExtra("numeroNivel", 999)
        notasHacertadas = intent.getIntExtra("notasHacertadas", 0)
        tiempoDurado = intent.getIntExtra("tiempoDurado", 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_derrota_desafio)
        textViewResultados = findViewById(R.id.textViewResultados)
        var tiempo = ((tiempoDurado-60)*-1)
        textViewResultados.text = "Total=$notasHacertadas\nTiempo=$tiempo s"
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
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
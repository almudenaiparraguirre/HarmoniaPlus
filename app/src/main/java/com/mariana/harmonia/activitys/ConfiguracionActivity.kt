package com.mariana.harmonia.activitys

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mariana.harmonia.R

class ConfiguracionActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var buttonCambiarContra: Button
    private lateinit var switchEfectosSonido: Switch
    private lateinit var switchOtraOpcion: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.configuracion_activity)
        switchEfectosSonido = findViewById(R.id.switchEfectosSonido)
        switchOtraOpcion = findViewById(R.id.switchSonidos)
        buttonCambiarContra = findViewById(R.id.buttonCambiarContrasena)
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)

        configurarSwitchColor(switchEfectosSonido)
        configurarSwitchColor(switchOtraOpcion)

        buttonCambiarContra.setOnClickListener {
            mediaPlayer.start()
        }
    }

    private fun configurarSwitchColor(switch: Switch) {
        switch.setOnCheckedChangeListener { _, isChecked ->
            val thumbColor = if (isChecked) {
                ContextCompat.getColor(this, R.color.rosa_claro)
            } else {
                ContextCompat.getColor(this, R.color.grisClaro)
            }

            val trackColor = if (isChecked) {
                ContextCompat.getColor(this, R.color.rosa_claro)
            } else {
                ContextCompat.getColor(this, R.color.grisClaro)
            }

            switch.thumbTintList = ColorStateList.valueOf(thumbColor)
            switch.trackTintList = ColorStateList.valueOf(trackColor)
        }
    }

    fun volverModoJuego(view: View){
        finish()
    }
}
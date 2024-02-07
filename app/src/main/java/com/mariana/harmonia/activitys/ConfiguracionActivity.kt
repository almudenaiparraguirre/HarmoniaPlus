package com.mariana.harmonia.activitys

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.core.content.ContextCompat
import com.mariana.harmonia.R

class ConfiguracionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)
        val switchEfectosSonido = findViewById<Switch>(R.id.switchEfectosSonido)
        val switchOtraOpcion = findViewById<Switch>(R.id.switchSonidos)

        configurarSwitchColor(switchEfectosSonido)
        configurarSwitchColor(switchOtraOpcion)
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
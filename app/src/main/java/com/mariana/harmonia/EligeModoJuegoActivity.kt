package com.mariana.harmonia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mariana.harmonia.interfaces.PlantillaActivity

class EligeModoJuegoActivity : AppCompatActivity(), PlantillaActivity {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elige_modo_juego)
        Utilidades.colorearTexto(this, R.id.titleTextView)
    }


}
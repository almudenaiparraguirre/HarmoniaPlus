package com.mariana.harmonia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mariana.harmonia.interfaces.PlantillaActivity

class RegistroActivity : AppCompatActivity(), PlantillaActivity {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        Utilidades.colorearTexto(this, R.id.titleTextView)
    }

}
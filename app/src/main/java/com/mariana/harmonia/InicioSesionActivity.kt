package com.mariana.harmonia

import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InicioSesionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.prueba_inicio_sesion_ajustar)
        colorearTexto(R.id.titleTextView)
        colorearTexto(R.id.registrateTextView)


    }

    fun colorearTexto(id: Int) {
        val titleTextView = findViewById<TextView>(id)
        val paint = titleTextView.paint
        val width = paint.measureText(titleTextView.text.toString())

        // Asegúrate de importar el LinearGradient correctamente
        titleTextView.paint.shader = LinearGradient(
            0f, 0f, width, titleTextView.textSize,
            intArrayOf(
                resources.getColor(R.color.rosa),
                resources.getColor(R.color.morado)
            ),
            null,
            Shader.TileMode.CLAMP
        )
    }


    fun tuMetodoBoton(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun tuMetodoSalir(view: View?) {
        finish()
    }

    fun tuMetodoClicable1(view: View){
        val intent = Intent(this, RestableceContrasenaActivity::class.java)
        startActivity(intent)
    }
}

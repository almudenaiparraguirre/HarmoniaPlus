package com.mariana.harmonia

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.StateListDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.print.PrintAttributes.Margins
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import io.grpc.Context
import kotlin.random.Random

class NivelesAventuraActivity : AppCompatActivity() {

    private val numBotones = 50
    private lateinit var llBotonera: LinearLayout
    private var botonCorrecto: Int = 0
    private lateinit var menuSuperior: LinearLayout
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_niveles_aventura)

        llBotonera = findViewById(R.id.llBotonera)
        botonCorrecto = Random.nextInt(numBotones)
        menuSuperior = findViewById(R.id.llTopBar)

        val lp = LinearLayout.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.button_width),
            resources.getDimensionPixelSize(R.dimen.button_height)
        )

        // Margen entre botones y margen superior
        lp.setMargins(
            0,
            resources.getDimensionPixelSize(R.dimen.button_margin_top),
            0,
            resources.getDimensionPixelSize(R.dimen.button_margin)
        )

        for (i in 0 until numBotones) {

            val button = Button(this)
            button.text = String.format("%2d", i)

            // Crear un nuevo conjunto de parámetros de diseño para cada botón
            val lp = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.button_width),
                resources.getDimensionPixelSize(R.dimen.button_height)
            )

            // Configurar el margen aleatorio
            val randomMargin =
                Random.nextInt(0, resources.getDimensionPixelSize(R.dimen.max_margin))
            lp.setMargins(
                if (i % 2 == 0) randomMargin else 0,
                resources.getDimensionPixelSize(R.dimen.button_margin_top),
                if (i % 2 != 0) randomMargin else 0,
                lp.bottomMargin
            )

            if(i > 1){
                button.isEnabled = false
            }
            else{
                button.setOnClickListener{
                    val intent = Intent(this, pruebasActivity::class.java)
                    startActivity(intent)
                }
            }

            // Configurar la gravedad
            lp.gravity = Gravity.CENTER

            button.layoutParams = lp

            //button.setOnClickListener(buttonClickListener(i))

            button.setBackgroundResource(getRandomButtonDrawable())
            llBotonera.addView(button)
        }
    }

    private fun getRandomButtonDrawable(): Int {
        val buttonDrawables = listOf(
            R.drawable.style_round_button
        )
        return buttonDrawables[Random.nextInt(buttonDrawables.size)]
    }
}
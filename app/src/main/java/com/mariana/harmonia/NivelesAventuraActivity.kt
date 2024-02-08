package com.mariana.harmonia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import kotlin.random.Random

class NivelesAventuraActivity : AppCompatActivity() {

    private val numBotones = 50
    private lateinit var llBotonera: LinearLayout
    private var botonCorrecto: Int = 0
    private lateinit var menuSuperior: LinearLayout

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

        val constraintSet = ConstraintSet()

        for (i in 0 until numBotones) {
            val button = Button(this)
            button.layoutParams = lp
            button.text = String.format("%02d", i)
            setRandomVerticalPosition(button)
            button.setOnClickListener(buttonClickListener(i))

            // Cambia la forma del botón a redonda
            button.setBackgroundResource(getRandomButtonDrawable())

            llBotonera.addView(button)
        }
    }

    private fun buttonClickListener(index: Int): View.OnClickListener? {
        return View.OnClickListener {
            if (index == botonCorrecto) {
                Toast.makeText(
                    this@NivelesAventuraActivity,
                    "¡ME ENCONTRASTE!!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@NivelesAventuraActivity,
                    "Sigue buscando",
                    Toast.LENGTH_SHORT
                ).show()
            }
            botonCorrecto = Random.nextInt(numBotones)
        }
    }


    private fun getRandomButtonDrawable(): Int {
        val buttonDrawables = listOf(
            R.drawable.style_round_button
        )
        return buttonDrawables[Random.nextInt(buttonDrawables.size)]
    }

    private fun setRandomVerticalPosition(button: Button) {
        val layoutParams = button.layoutParams as LinearLayout.LayoutParams

        // Obtener el número de secciones
        val numSecciones = 4 // Ajusta esto según la cantidad de secciones que desees

        // Calcular la sección aleatoria
        val randomSection = Random.nextInt(numSecciones)

        // Obtener la guía correspondiente a la sección
        val guidelineId = when (randomSection) {
            0 -> R.id.guideline1
            1 -> R.id.guideline2
            2 -> R.id.guideline3
            else -> R.id.llBotonera4 // o app:layout_constraintTop_toBottomOf="@id/guideline3"
        }

        // Obtener la posición vertical de la guía
        val guidelinePosition = findViewById<View>(guidelineId).y

        // Calcular la posición del botón
        val buttonHeight = button.height
        val randomPosition = Random.nextFloat() * guidelinePosition

        // Configurar márgenes superiores e inferiores del botón
        layoutParams.topMargin = randomPosition.toInt()
        layoutParams.bottomMargin = guidelinePosition.toInt() - randomPosition.toInt() - buttonHeight

        button.layoutParams = layoutParams
    }



}
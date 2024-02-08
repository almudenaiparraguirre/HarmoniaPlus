package com.mariana.harmonia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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
                setRandomHorizontalPosition(button)
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

    private fun setRandomHorizontalPosition(button: Button) {
        val layoutParams = button.layoutParams as LinearLayout.LayoutParams

        val horizontalOptions = listOf(
            0.0f, // START
            1.0f, // CENTER_HORIZONTAL
            2.0f  // END
        )

        val randomWeight = horizontalOptions.random()

        layoutParams.apply {
            weight = randomWeight
        }

        button.layoutParams = layoutParams
    }
}
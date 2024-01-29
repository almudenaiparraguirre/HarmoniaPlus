package com.mariana.harmonia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

class EnvioCodigoActivity : AppCompatActivity() {

    private lateinit var textoNoRecibido: TextView
    private lateinit var cuentaRegresiva: CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_envio_codigo)

        textoNoRecibido = findViewById(R.id.textoNoRecibido)
        textoNoRecibido.isEnabled = false

        cuentaRegresiva = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val segundosRestantes = millisUntilFinished / 1000
                textoNoRecibido.text = "¿No has recibido el código? ($segundosRestantes s)"
            }

            override fun onFinish() {
                textoNoRecibido.isEnabled = true
                agregarEnlaceVolverAEnviar()
            }
        }

        cuentaRegresiva.start()
    }

    override fun onDestroy() {
        cuentaRegresiva.cancel()
        super.onDestroy()
    }

    private fun agregarEnlaceVolverAEnviar() {
        val textoCompleto = "¿No has recibido el código? Volver a enviar"
        val spannableStringBuilder = SpannableStringBuilder(textoCompleto)
        val volverAEnviarClick = object : ClickableSpan() {
            override fun onClick(widget: View) {
            }
        }

        spannableStringBuilder.setSpan(
            volverAEnviarClick,
            textoCompleto.indexOf("Volver a enviar"),
            textoCompleto.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textoNoRecibido.text = spannableStringBuilder
    }

    fun enviarCodigo(view: View) {
        val intent = Intent(this, escribirNuevaContrasenaActivity::class.java)
        startActivity(intent)
    }
}
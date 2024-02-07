package com.mariana.harmonia.activitys.iniciarSesion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.mariana.harmonia.MainActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.activitys.Utilidades
import com.mariana.harmonia.interfaces.PlantillaActivity

class EnvioCodigoActivity : AppCompatActivity(), PlantillaActivity {

    //Declaración de variables
    private lateinit var textoNoRecibido: TextView
    private lateinit var cuentaRegresiva: CountDownTimer

    // FUN --> On create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.envio_codigo_activity)

        Utilidades.colorearTexto(this, R.id.titleTextView)

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

    // FUN --> Destrucción cuenta regresiva código
    override fun onDestroy() {
        if (::cuentaRegresiva.isInitialized) {
            cuentaRegresiva.cancel()
        }

        super.onDestroy()
    }

    // FUN --> Añadir label volver a enviar
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

    // FUN --> Click botón confirmar código
    fun clickConfirmarCodigo(view: View) {
        val intent = Intent(this, escribirNuevaContrasenaActivity::class.java)
        startActivity(intent)
    }

    // FUN --> Volver al inicio de sesión (pantalla principal)
    fun irIniciarSesion(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // FUN --> Salir de la aplicación
    fun irSalir(view: View) {
        Utilidades.salirAplicacion(this)
    }
}
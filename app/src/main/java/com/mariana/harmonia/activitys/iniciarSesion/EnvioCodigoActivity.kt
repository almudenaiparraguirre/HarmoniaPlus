package com.mariana.harmonia.activitys.iniciarSesion

import android.content.Intent
import android.media.MediaPlayer
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
import com.mariana.harmonia.interfaces.PlantillaActivity
import com.mariana.harmonia.utils.Utils

/**
 * EnvioCodigoActivity es una actividad que gestiona el envío de un código y la cuenta regresiva
 * para volver a enviarlo en caso de no ser recibido.
 */
class EnvioCodigoActivity : AppCompatActivity(), PlantillaActivity {

    //Declaración de variables
    private lateinit var textoNoRecibido: TextView
    private lateinit var cuentaRegresiva: CountDownTimer

    /**
     * Método llamado cuando la actividad se está creando. Se encarga de inicializar la interfaz
     * de usuario y otros componentes necesarios.
     * @param savedInstanceState Si no es nulo, esta actividad está siendo reconstituida a partir de un estado guardado previamente.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.envio_codigo_activity)

        Utils.degradadoTexto(this, R.id.titleTextView,R.color.rosa,R.color.morado)

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

    /**
     * Método llamado cuando la actividad está a punto de ser destruida. Se encarga de cancelar
     * la cuenta regresiva para liberar recursos.
     */
    override fun onDestroy() {
        if (::cuentaRegresiva.isInitialized) {
            cuentaRegresiva.cancel()
        }

        super.onDestroy()
    }

    /**
     * Método privado para agregar un enlace "Volver a enviar" al texto cuando el código no es recibido.
     */
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

    /**
     * Método llamado al hacer clic en el botón para confirmar el código. Inicia la actividad
     * para escribir una nueva contraseña.
     */
    fun clickConfirmarCodigo(view: View) {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        mediaPlayer.start()
        val intent = Intent(this, escribirNuevaContrasenaActivity::class.java)
        startActivity(intent)
    }

    /**
     * Método llamado al hacer clic en el botón para volver al inicio de sesión (pantalla principal).
     */
    fun irIniciarSesion(view: View) {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        mediaPlayer.start()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Método llamado al hacer clic en el botón para salir de la aplicación.
     */
    fun irSalir(view: View) {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        mediaPlayer.start()
        Utils.salirAplicacion(this)
    }
}
package com.mariana.harmonia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class RestableceContrasenaActivity : AppCompatActivity() {

    private lateinit var textoNoRecibido: TextView
    private lateinit var cuentaRegresiva: CountDownTimer
    private lateinit var boton: Button
    private lateinit var email: EditText
    private lateinit var textoAdvertencia: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restablece_contrasena)

        boton = findViewById(R.id.botonEmail)
        email = findViewById(R.id.editText1)
        textoAdvertencia = findViewById(R.id.textoAdvertencia)

        //activarBoton()
    }

    fun clickVolverInicioSesion(view: View) {
        val intent = Intent(this, InicioSesionActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        cuentaRegresiva.cancel()
        super.onDestroy()
    }

    fun enviarEmail(view: View) {
        val emailText = email.text.toString()

        if (emailText.isEmpty()) {
            textoAdvertencia.visibility = View.VISIBLE
            Log.d("RestableceContrasena", "No se puede enviar el correo porque el email está vacío")
        } else {
            textoAdvertencia.visibility = View.GONE
            val intent = Intent(this, EnvioCodigoActivity::class.java)
            startActivity(intent)
        }
    }

}

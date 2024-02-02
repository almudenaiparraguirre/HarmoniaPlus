package com.mariana.harmonia.activitys.iniciarSesion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.mariana.harmonia.InicioSesionActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.activitys.Utilidades
import com.mariana.harmonia.interfaces.PlantillaActivity

class RestableceContrasenaActivity : AppCompatActivity(), PlantillaActivity {

    // Declaración de variables
    private lateinit var boton: Button
    private lateinit var email: EditText
    private lateinit var textoAdvertencia: TextView

    // FUN --> OnCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restablece_contrasena)

        boton = findViewById(R.id.botonEmail)

        email = findViewById(R.id.editText1)
        textoAdvertencia = findViewById(R.id.textoAdvertencia)
        Utilidades.colorearTexto(this, R.id.titleTextView)
    }

    // FUN --> Intento de enviar email
    fun enviarEmail(view: View) {
        val emailText = email.text.toString().trim()

        if (emailText.isEmpty()) {
            textoAdvertencia.visibility = View.VISIBLE
            Log.d("RestableceContrasena", "No se puede enviar el correo porque el email está vacío")
        } else if (!isValidEmail(emailText)) {
            textoAdvertencia.visibility = View.VISIBLE
            Log.d("RestableceContrasena", "Formato de correo electrónico no válido")
        } else {
            textoAdvertencia.visibility = View.GONE
            val intent = Intent(this, EnvioCodigoActivity::class.java)
            startActivity(intent)
        }
    }

    // FUN --> Comprueba que el email introducido es válido
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    // FUN --> Vuelve al inicio de sesión
    fun irIniciarSesion(view: View) {
        val intent = Intent(this, InicioSesionActivity::class.java)
        startActivity(intent)
        finish()
    }

    // FUN --> Salir de la aplicación
    fun irSalir(view: View) {
        Utilidades.salirAplicacion(this)
    }

}

package com.mariana.harmonia.activitys.iniciarSesion

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.mariana.harmonia.MainActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.interfaces.PlantillaActivity
import com.mariana.harmonia.utils.Utils

class escribirNuevaContrasenaActivity : AppCompatActivity(), PlantillaActivity {

    // FUN --> OnCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.escribir_nueva_contrasena_activity)


        val editText1: EditText = findViewById(R.id.editTextEmail)
        val editText2: EditText = findViewById(R.id.editTextContraseña)
        val botonConfirmar: Button = findViewById(R.id.botonIniciarSesion)

        botonConfirmar.isEnabled = false

        val textWatcher = object : TextWatcher {
             override fun beforeTextChanged(
                 charSequence: CharSequence?,
                 start: Int,
                 count: Int,
                 after: Int
             ) {
             }

             override fun onTextChanged(
                 charSequence: CharSequence?,
                 start: Int,
                 before: Int,
                 count: Int
             ) {
                 verificarCondiciones()
             }

             override fun afterTextChanged(editable: Editable?) {
             }
         }

         // Agrega el TextWatcher a ambos EditText
         editText1.addTextChangedListener(textWatcher)
         editText2.addTextChangedListener(textWatcher)
    }

    // FUN --> Confirmación de la nueva contraseña
    fun clickConfirmarContrasenaNueva(view: View) {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        mediaPlayer.start()
        val intent = Intent(this, ContrasenaRestablecidaCorrectamenteActivity::class.java)
        startActivity(intent)
    }

    // FUN --> Verificar que las contraseñas cumplen los requisitos
    fun verificarCondiciones() {
        val editText1: EditText = findViewById(R.id.editTextEmail)
        val editText2: EditText = findViewById(R.id.editTextContraseña)
        val botonConfirmar: Button = findViewById(R.id.botonIniciarSesion)

        val contrasena1 = editText1.text.toString()
        val contrasena2 = editText2.text.toString()

        val contrasenasIguales = contrasena1 == contrasena2
        val longitudSuficiente = contrasena1.length >= 6
        val tieneMayuscula = contrasena1.any { it.isUpperCase() }
        val tieneMinuscula = contrasena1.any { it.isLowerCase() }
        val tieneNumero = contrasena1.any { it.isDigit() }

        val condicionesCumplidas =
            contrasenasIguales && longitudSuficiente && tieneMayuscula && tieneMinuscula && tieneNumero

        botonConfirmar.isEnabled = condicionesCumplidas
    }

    // FUN --> Volver al inicio de sesión
    fun irIniciarSesion(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // FUN --> Salir de la aplicación
    fun irSalir(view: View) {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        mediaPlayer.start()
        Utils.salirAplicacion(this)
    }
}
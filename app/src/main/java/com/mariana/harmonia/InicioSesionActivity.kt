package com.mariana.harmonia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mariana.harmonia.activitys.EligeModoJuegoActivity
import com.mariana.harmonia.activitys.InicioSesion
import com.mariana.harmonia.activitys.MainActivity
import com.mariana.harmonia.activitys.iniciarSesion.RegistroActivity
import com.mariana.harmonia.activitys.iniciarSesion.RestableceContrasenaActivity
import com.mariana.harmonia.activitys.Utilidades
import com.mariana.harmonia.activitys.Utilidades.Companion.colorearTexto
import com.mariana.harmonia.interfaces.PlantillaActivity


class InicioSesionActivity : AppCompatActivity(),PlantillaActivity {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        colorearTexto(this, R.id.titleTextView)
        colorearTexto(this, R.id.registrateTextView)
        colorearTexto(this, R.id.recuerdasContrasena)
    }


    fun clickCrearCuenta(view: View) {
        //val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    fun clickNoRecuerdasLaContraseña(view: View){
        val intent = Intent(this, RestableceContrasenaActivity::class.java)
        startActivity(intent)
    }

    fun irRegistrate(view: View?){
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
    }
    fun irIniciarSesion(view: View) {
        firebaseAuth = FirebaseAuth.getInstance()

        val btnIngresar: Button = findViewById(R.id.botonIniciarSesion)
        val Email: TextView = findViewById(R.id.editText1)
        val contrasena: TextView = findViewById(R.id.editText2)

        // Validación de campos
        val emailText = Email.text.toString()
        val contrasenaText = contrasena.text.toString()

        if (emailText.isEmpty() || contrasenaText.isEmpty()) {
            Toast.makeText(baseContext, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación del formato de correo electrónico
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            Toast.makeText(baseContext, "Formato de correo electrónico no válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación de la longitud de la contraseña
        if (contrasenaText.length < 6) {
            Toast.makeText(baseContext, "La contraseña debe tener al menos 6 caracteres,asegurese de su contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(emailText, contrasenaText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Toast.makeText(baseContext, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, EligeModoJuegoActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("InicioSesion", "Error al iniciar sesión", task.exception)
                    Toast.makeText(baseContext, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }

    }





    fun irSalir(view: View) {
        Utilidades.salirAplicacion(this)
    }
    override fun onBackPressed() {
        Utilidades.salirAplicacion(this)
        super.onBackPressed()
    }

    fun iniciarSesionPruebas(view: View?){
        val intent = Intent(this, EligeModoJuegoActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun clickFireBase(view: View) {
        val intent = Intent(this, InicioSesion::class.java)
        startActivity(intent)
    }
}

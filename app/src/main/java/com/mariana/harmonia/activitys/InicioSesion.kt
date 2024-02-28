package com.mariana.harmonia.activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mariana.harmonia.R

class InicioSesion : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio_sesion_activity)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun signIn(email: String, contrasena: String) {
        firebaseAuth.signInWithEmailAndPassword(email, contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Toast.makeText(baseContext, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
                    // Crear un Intent para ir a EligeModoJuegoActivity
                    val intent = Intent(this, EligeModoJuegoActivity::class.java)
                    // Pasar el correo electrónico como extra al Intent
                    intent.putExtra("email", email)
                    // Iniciar la actividad
                    startActivity(intent)
                    // Aquí irías a la segunda actividad
                    Log.d("InicioSesion", "Inicio de sesión exitoso")
                } else {
                    Toast.makeText(baseContext, "Error de email o contraseña", Toast.LENGTH_SHORT).show()
                    Log.e("InicioSesion", "Error de autenticación: ${task.exception?.message}")
                }
            }
    }

    fun btnIngresar(view: View) {
        val emailTextView: TextView = findViewById(R.id.edtEmail)
        val contrasenaTextView: TextView = findViewById(R.id.edtContrasena)

        val email = emailTextView.text.toString()
        val contrasena = contrasenaTextView.text.toString()

        signIn(email, contrasena)
    }
}

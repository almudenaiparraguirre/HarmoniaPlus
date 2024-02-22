package com.mariana.harmonia.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mariana.harmonia.R

class InicioSesion : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio_sesion_activity)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun signIn(Email: String, contrasena: String, nombreModoDeJuegoTextView: TextView) {
        firebaseAuth.signInWithEmailAndPassword(Email, contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Toast.makeText(baseContext, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
                    // Llamar al método para obtener el nombre del modo de juego y actualizar el TextView
                    user?.email?.let { email ->
                    }
                    // Aquí irás a la segunda actividad
                    Log.d("InicioSesion", "Inicio de sesión exitoso")
                } else {
                    Toast.makeText(baseContext, "Error de email o contraseña", Toast.LENGTH_SHORT).show()
                    Log.e("InicioSesion", "Error de autenticación: ${task.exception?.message}")
                }
            }
    }

    fun btnIngresar(view: View) {
        val Email : TextView = findViewById(R.id.edtEmail)
        val contrasena : TextView = findViewById(R.id.edtContrasena)

    }
}

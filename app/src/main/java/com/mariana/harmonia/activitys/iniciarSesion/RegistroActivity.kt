package com.mariana.harmonia.activitys.iniciarSesion

import android.content.ContentValues.TAG
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.mariana.harmonia.MainActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.activitys.Utilidades
import com.mariana.harmonia.interfaces.PlantillaActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.mariana.harmonia.models.entity.User



class RegistroActivity : AppCompatActivity(), PlantillaActivity {
    private lateinit var firebaseAuth: FirebaseAuth
    // FUN --> OnCreate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_activity)
        Utilidades.colorearTexto(this, R.id.titleTextView)

        firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

    }

    // FUN --> Volver al inicio de sesión
    fun irIniciarSesion(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // FUN --> Salir de la aplicación
    fun irSalir(view: View) {
        Utilidades.salirAplicacion(this)
    }

    fun botonCrearCuenta(view: View) {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        mediaPlayer.start()
        // 1. Obtener los valores ingresados en los campos de correo y contraseña
        val emailTextView = findViewById<TextView>(R.id.editText1)
        val contraseñaTextView = findViewById<TextView>(R.id.editText3)
        val email = emailTextView.text.toString()
        val contraseña = contraseñaTextView.text.toString()
        val nombreTextView = findViewById<TextView>(R.id.editText2)
        val nombre = nombreTextView.text.toString()


        // 2. Validar los campos
        if (email.isEmpty() || contraseña.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // 3. Validar el formato de correo electrónico
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Formato de correo electrónico incorrecto", Toast.LENGTH_SHORT).show()
            return
        }

        // 4. Validar la longitud y composición de la contraseña
        if (!validarContraseña(contraseña)) {
            Toast.makeText(
                this,
                "La contraseña debe tener al menos 8 caracteres, 1 minúscula, 1 mayúscula y 1 número",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // 5. Llamar a una función para registrar al usuario en Firebase
        registrarUsuarioEnFirebase(email, contraseña,nombre)
    }

    // FUN --> Validar la contraseña
    private fun validarContraseña(contraseña: String): Boolean {
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}\$")
        return regex.matches(contraseña)
    }

    private fun registrarUsuarioEnFirebase(email: String, contraseña: String,nombre: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, contraseña)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registro exitoso
                    val user = User(email = email,name = nombre,355,1 )

                    // Llamar al método para crear la colección si no existe
                    UserDao.createUsersCollectionIfNotExists()

                    // Llamar al método addUser de UserDao para agregar el usuario
                    UserDao.addUser(user)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // Manejar caso en el que falla el registro
                    Toast.makeText(this, "Error al registrar usuario: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


}
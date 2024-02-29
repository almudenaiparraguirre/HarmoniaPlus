package com.mariana.harmonia.utils

import android.content.ContentValues
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Utils {
    companion object {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        fun obtenerModoDeJuego(nombreModoDeJuegoTextView: TextView?) {

            val emailFire = currentUser?.email
            val email = emailFire?.replace(".", ",")

            try {
                UserDao.getUserField(email, "name",
                    onSuccess = { name ->
                        nombreModoDeJuegoTextView?.post {
                            nombreModoDeJuegoTextView.text = name as? CharSequence ?: ""
                            Toast.makeText(nombreModoDeJuegoTextView.context, name as? CharSequence ?: "", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) { exception ->
                    Log.e(
                        ContentValues.TAG,
                        "Error al obtener el nombre del modo de juego: ${exception.message}",
                        exception
                    )
                    nombreModoDeJuegoTextView?.post {
                        nombreModoDeJuegoTextView?.text = "unnamed"
                    }
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Excepción al obtener el nombre del modo de juego: ${e.message}", e)
                nombreModoDeJuegoTextView?.post {
                    nombreModoDeJuegoTextView?.text = "unnamed"
                }
            }
        }

        fun actualizarCorreo(correoTextView: TextView?){
            val emailFire = currentUser?.email
            correoTextView?.text = emailFire

        }
    }
}

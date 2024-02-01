package com.mariana.harmonia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyCallback.CallStateListener
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.mariana.harmonia.interfaces.PlantillaActivity
import org.w3c.dom.Text

class InicioSesion : AppCompatActivity() {

    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)




        // Hace falta iniciar la variable FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        //TODO
        //HAY QUE COMPROBAR LOS CAMPOS DE TEXTO PARA COMPROBAR QUE NO ESTE VACIO Y
        // QUE SEA DE TIPO EMAIL ("DHNADIDNA@ANDASJN.COM")

    }

    //Función que comprueba que el usuario y contraseña existen
    private fun signIn (Email:String , contrasena : String){


       /* firebaseAuth.signInWithEmailAndPassword(Email,contrasena).addOnCompleteListener(this) {
            task -> if (task.isSuccessful){
                val user = firebaseAuth.currentUser
                Toast.makeText(baseContext,"autentitacion existosa", Toast.LENGTH_SHORT).show()
                // aqui vamos a ir a la segunda activity
        }
            else{
            Toast.makeText(baseContext, "Error de email o contraseña", Toast.LENGTH_SHORT).show()

        }
        }
       */

        //prueba manejo de excepciones
        Log.d("InicioSesion", "signInWithEmailAndPassword: $Email")
        println("ENTRANDO A LA COMPROBACION")

        firebaseAuth.signInWithEmailAndPassword(Email, contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Toast.makeText(baseContext, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
                    // Aquí irás a la segunda actividad
                    println("TODO OK")
                } else {
                    Toast.makeText(baseContext, "Error de email o contraseña", Toast.LENGTH_SHORT).show()
                    println("KO KO KO KO KO")
                }
            }
            .addOnFailureListener(this) { e ->
                Log.e("InicioSesion", "Error de autenticación: ${e.message}")
                println("FALLO")
            }

    }

    fun btnIngresar(view: View) {

        val btnIngresar : Button = findViewById(R.id.btnIngresar)
        val Email : TextView = findViewById(R.id.edtEmail)
        val contrasena : TextView = findViewById(R.id.edtContrasena)

        println("ASDFASDFASDFASDF")
        signIn(Email.text.toString(),contrasena.text.toString())
    }

}
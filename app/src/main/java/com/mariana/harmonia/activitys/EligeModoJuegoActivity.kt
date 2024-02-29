package com.mariana.harmonia.activitys

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mariana.harmonia.MainActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.interfaces.PlantillaActivity
import com.mariana.harmonia.utils.Utils

class EligeModoJuegoActivity : AppCompatActivity(), PlantillaActivity {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var nombreTextView: TextView
    private lateinit var porcentajeTextView: TextView
    private lateinit var progressBar: ProgressBar


    companion object {
        private const val PREFS_NAME = "MyPrefsFile"
        private const val SESSION_KEY = "isSessionActive"


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.elige_modo_juego_activity)
        firebaseAuth = FirebaseAuth.getInstance()

        Utilidades.colorearTexto(this, R.id.cerrarSesion)
        Utilidades.colorearTexto(this, R.id.titleTextView)

        // Inicialización de vistas
       progressBar = findViewById<ProgressBar>(R.id.progressBarCarga)
         porcentajeTextView = findViewById<TextView>(R.id.porcentajeTextView)
        nombreTextView = findViewById(R.id.nombreModoDeJuego)
        // Llama al método de utilidades para obtener el modo de juego y actualizar el TextView
        Utils.obtenerNombre(nombreTextView)

        // Puedes actualizar el porcentaje directamente
        Utils.obtenerNivel(porcentajeTextView)
       Utils.obtenerExperiencia(progressBar)

        // Inicialización de la animación
        val imageView: ImageView = findViewById(R.id.fondoImageView)
        val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        imageView.startAnimation(anim)


           println(Utils.serializeImage(this,R.mipmap.img_perfil_luis_golden))
    }

    fun menu_perfil(view: View){
        val intent = Intent(this, PerfilUsuarioActivity::class.java)
        startActivity(intent)
    }

    fun cerrarSesion(view: View) {
        firebaseAuth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity() // Cierra todas las actividades anteriores
    }

    fun clickOpciones(view: View){

      /*  val db = FirebaseFirestore.getInstance()
            val usuarios = db.collection("usuarios")

        val stateQuery = usuarios.whereEqualTo("email", FirebaseAuth.getInstance().currentUser?.email)
        println(FirebaseAuth.getInstance().currentUser?.email)

        // Ejecutar la consulta y obtener el resultado
        stateQuery.get().addOnSuccessListener { querySnapshot ->
            println("Consulta exitosa. Documentos encontrados: ${querySnapshot.size()}")
            // Recorrer los documentos obtenidos
            for (document in querySnapshot.documents) {
                // Obtener el nombre del usuario y imprimirlo por consola
                val nombre = document.getString("name")
                println("Nombre: $nombre")
            }
        }.addOnFailureListener { exception ->
            // Manejar cualquier error que ocurra al ejecutar la consulta
            println("Error al obtener los usuarios: $exception")
        }*/

        val intent = Intent(this, ConfiguracionActivity::class.java)
        startActivity(intent)
    }

    fun irModoAventura(view: View){
        val intent = Intent(this, NivelesAventuraActivity::class.java)
        startActivity(intent)
    }

    fun irDesafio(view: View){
        val intent = Intent(this, JuegoMusicalActivity::class.java)
        intent.putExtra("desafio", true)
        startActivity(intent)
    }


}

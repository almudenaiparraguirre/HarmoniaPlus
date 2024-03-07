package com.mariana.harmonia.activitys

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.mariana.harmonia.MainActivity

import com.mariana.harmonia.R
import com.mariana.harmonia.interfaces.PlantillaActivity
import com.mariana.harmonia.utils.Utils
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class EligeModoJuegoActivity : AppCompatActivity(), PlantillaActivity {

    private   var RC_NOTIFICATION = 99
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var nombreTextView: TextView
    private lateinit var porcentajeTextView: TextView
    private lateinit var imageViewFotoPerfil: ImageView
    private lateinit var progressBar: ProgressBar
    var storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.elige_modo_juego_activity) // Inflar el layout primero

        firebaseAuth = FirebaseAuth.getInstance()
        Utils.isExternalStorageWritable()
        Utils.isExternalStorageReadable()

        val imageView: ImageView = findViewById(R.id.fondoImageView)
        val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.animacion_principal)
        imageView.startAnimation(anim)

        nombreTextView = findViewById(R.id.nombreModoDeJuego)
        porcentajeTextView = findViewById(R.id.porcentajeTextView)
        progressBar = findViewById<ProgressBar>(R.id.progressBarCarga)
        imageViewFotoPerfil = findViewById(R.id.imageViewFotoPerfil) // Inicializar imageViewFotoPerfil

        inicilalizarVariablesThis()
        inicializarConBase()

        lifecycleScope.launch {
            downloadImage2()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), RC_NOTIFICATION)
        }
    }
    fun inicializarConBase() = runBlocking {
        var nivel = Utils.getExperiencia()!!/100
        var experienciaSobrante = Utils.getExperiencia()!!%100

        nombreTextView.text = Utils.getNombre()
        porcentajeTextView.text = nivel.toString()
        progressBar.progress = experienciaSobrante
    }

    private fun inicilalizarVariablesThis() {
        Utilidades.degradadoTexto(this, R.id.cerrarSesion,R.color.rosa,R.color.morado)
        Utilidades.degradadoTexto(this, R.id.titleTextView,R.color.rosa,R.color.morado)
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        Utils.serializeImage(this,R.mipmap.img_gema)
        imageViewFotoPerfil.setImageBitmap(Utils.deserializeImage(this,"/storage/emulated/0/Download/imagenSerializada.json"))
    }


    private fun downloadImage2() {
        val storageRef = storage.reference
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val imagesRef = storageRef.child("imagenesPerfilGente").child("$userId.jpg")

        imagesRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(this)
                .load(url)
                .into(imageViewFotoPerfil)

            // Call the function to change the name and upload the image
            /*runBlocking {
                changeAndUploadImage(url)
            }*/
        }.addOnFailureListener { exception ->
            println("Error al cargar la imagen: ${exception.message}")
        }
    }

    fun menu_perfil(view: View){
        mediaPlayer.start()
        val intent = Intent(this, PerfilUsuarioActivity::class.java)
        startActivity(intent)
    }

    fun cerrarSesion(view: View) {
        mediaPlayer.start()
        firebaseAuth.signOut()
        Utils.currentUser?.reload()


        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.getApplicationContext().getCacheDir().delete();
        finish()
        finishAffinity() // Cierra todas las actividades anteriores
    }

    fun clickOpciones(view: View){
        mediaPlayer.start()

        val intent = Intent(this, ConfiguracionActivity::class.java)
        startActivity(intent)
    }

    fun irModoAventura(view: View){
        mediaPlayer.start()
        val intent = Intent(this, NivelesAventuraActivity::class.java)
        startActivity(intent)
    }

    fun irDesafio(view: View){
        mediaPlayer.start()
        val intent = Intent(this, JuegoMusicalActivity::class.java)
        intent.putExtra("desafio", true)
        startActivity(intent)
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RC_NOTIFICATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "ALLOWED", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
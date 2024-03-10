package com.mariana.harmonia.activitys

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.mariana.harmonia.MainActivity

import com.mariana.harmonia.R
import com.mariana.harmonia.activitys.fragments.CargaFragment
import com.mariana.harmonia.activitys.fragments.FragmentoDificultadDesafio
import com.mariana.harmonia.interfaces.PlantillaActivity
import com.mariana.harmonia.models.db.FirebaseDB
import com.mariana.harmonia.utils.ServicioTiempo
import com.mariana.harmonia.utils.Utils
import com.mariana.harmonia.utils.UtilsDB
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class EligeModoJuegoActivity : AppCompatActivity(), PlantillaActivity {


    companion object{
        // Se usa para poder llamarla desde otras activitis
        lateinit var instance: EligeModoJuegoActivity
    }
    private   var RC_NOTIFICATION = 99

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var nombreTextView: TextView
    private lateinit var porcentajeTextView: TextView
    private lateinit var imageViewFotoPerfil: ImageView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.elige_modo_juego_activity) // Inflar el layout primero


        Utils.isExternalStorageWritable()
        Utils.isExternalStorageReadable()

        val imageView: ImageView = findViewById(R.id.fondoImageView)
        val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.animacion_principal)
        imageView.startAnimation(anim)
        instance = this
        nombreTextView = findViewById(R.id.nombreModoDeJuego)
        porcentajeTextView = findViewById(R.id.porcentajeTextView)
        progressBar = findViewById(R.id.progressBarCarga)
        imageViewFotoPerfil = findViewById(R.id.imageViewFotoPerfil)


        inicilalizarVariablesThis()
        inicializarConBase()

        lifecycleScope.launch {
            downloadImage2()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), RC_NOTIFICATION)
        }
        val serviceIntent = Intent(this, ServicioTiempo::class.java)
        startService(serviceIntent)
        println("SERVICIO CONTADOR COMENZO")
        crearFragmentoCarga()

    }

    fun crearFragmentoCarga(){
        val fragment = CargaFragment()

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragment_container_carga, fragment)

        fragmentTransaction.addToBackStack(null)

        fragmentTransaction.commit()
        ocultarFragmento()

    }


    fun inicializarConBase() = runBlocking {
        var nivel = UtilsDB.getExperiencia()!!/100
        var experienciaSobrante = UtilsDB.getExperiencia()!!%100

        nombreTextView.text = UtilsDB.getNombre()
        porcentajeTextView.text = "NV. "+nivel.toString()
        progressBar.progress = experienciaSobrante
    }


    private fun inicilalizarVariablesThis() {
        Utils.degradadoTexto(this, R.id.cerrarSesion,R.color.rosa,R.color.morado)
        Utils.degradadoTexto(this, R.id.titleTextView,R.color.rosa,R.color.morado)
        Utils.degradadoTexto(this, R.id.eligeModo,R.color.rosa,R.color.morado)
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        //Utils.serializeImage(this,R.mipmap.img_gema)
        //imageViewFotoPerfil.setImageBitmap(Utils.deserializeImage(this,"/storage/emulated/0/Download/imagenSerializada.json"))
    }

    //Descargar imagen
    private fun downloadImage2() {
        val storageRef = FirebaseDB.getInstanceStorage().reference
        val userId = FirebaseDB.getInstanceFirebase().currentUser?.uid
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
            imageViewFotoPerfil.setImageResource(R.mipmap.fotoperfil_guitarra)
        }
    }

    fun menu_perfil(view: View){
        mediaPlayer.start()
        mostrarFragmento()
        val intent = Intent(this, PerfilUsuarioActivity::class.java)
        startActivity(intent)
        inicializarConBase()
    }

    //Cerrar sesión
    fun cerrarSesion(view: View) {
        mediaPlayer.start()
        val serviceIntent = Intent(this, ServicioTiempo::class.java)
        stopService(serviceIntent)
        println("SERVICIO CONTADOR CERRADO")
        FirebaseDB.getInstanceFirebase().signOut()
        UtilsDB.currentUser?.reload()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        finishAffinity()
    }

    fun clickOpciones(view: View){
        mediaPlayer.start()
        val intent = Intent(this, ConfiguracionActivity::class.java)
        startActivity(intent)
    }

    fun irModoAventura(view: View){
        mostrarFragmento()
        mediaPlayer.start()
        val intent = Intent(this, NivelesAventuraActivity::class.java)
        startActivity(intent)
        mostrarFragmento()
    }


    fun irDesafio(view: View) {
        mediaPlayer.start()

        // Crea una instancia del fragmento que deseas mostrar
        val fragment = FragmentoDificultadDesafio()

        // Obtén el administrador de fragmentos y comienza una transacción
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Reemplaza el contenido del contenedor de fragmentos con el fragmento que deseas mostrar
        fragmentTransaction.replace(R.id.fragment_container_desafio, fragment)

        // Agrega la transacción al historial de retroceso (opcional)
        fragmentTransaction.addToBackStack(null)

        // Realiza la transacción
        fragmentTransaction.commit()

        // Oculta los botones de la actividad principal
    }

    //Volver a la pestaña anterior
    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun ocultarFragmento(){
        var fragmento =  findViewById<FrameLayout>(R.id.fragment_container_carga)
        fragmento.visibility = View.GONE
    }

    fun mostrarFragmento(){
        var fragmento =  findViewById<FrameLayout>(R.id.fragment_container_carga)
        fragmento.visibility = View.VISIBLE
    }

    ///Permisos de notificación
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
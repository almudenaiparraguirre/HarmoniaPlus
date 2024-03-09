package com.mariana.harmonia.activitys

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.mariana.harmonia.R
import java.io.ByteArrayOutputStream
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import com.mariana.harmonia.activitys.fragments.CargaFragment
import com.mariana.harmonia.databinding.PerfilUsuarioActivityBinding
import com.mariana.harmonia.models.db.FirebaseDB
import com.mariana.harmonia.utils.UtilsDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class PerfilUsuarioActivity : AppCompatActivity() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var nombreUsuarioTextView: TextView
        @SuppressLint("StaticFieldLeak")
        private lateinit var gmailUsuarioTextView: TextView
        private const val REQUEST_CAMERA = 123
    }

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var imagen: ImageView
    private lateinit var lapiz: ImageView
    private lateinit var cardViewPerfil: CardView
    private lateinit var progressBar1: ProgressBar
    private lateinit var progressBar2: ProgressBar
    private lateinit var progressBar3: ProgressBar
    private lateinit var progressBar4: ProgressBar
    private lateinit var progressBar5: ProgressBar
    private lateinit var progressBar6: ProgressBar
    private lateinit var progressBar7: ProgressBar
    private lateinit var progressBar8: ProgressBar
    private lateinit var porcentajeTextView1: TextView
    private lateinit var porcentajeTextView2: TextView
    private lateinit var porcentajeTextView3: TextView
    private lateinit var porcentajeTextView4: TextView
    private lateinit var porcentajeTextView5: TextView
    private lateinit var porcentajeTextView6: TextView
    private lateinit var porcentajeTextView7: TextView
    private lateinit var porcentajeTextView8: TextView
    private lateinit var nivelTextView: TextView
    private lateinit var experienciaTextView: TextView
    private lateinit var precisionTextView: TextView
    private lateinit var editText: EditText
    private lateinit var miStorage: StorageReference
    private lateinit var fechaRegistro: TextView
    private lateinit var nivelRango: TextView
    private lateinit var centerCircle: ImageView
    private lateinit var binding: PerfilUsuarioActivityBinding
    private lateinit var originalText: Editable
    private lateinit var instancia: FirebaseAuth

    var mutableList: MutableList<String> = mutableListOf(
        "Novato", "Principiante", "Amateur",
        "Intermedio", "Avanzado", "Experto", "Maestro", "Leyenda", "Virtuoso", "Genio"
    )
    var randomImagenInstrumentos: MutableList<String> = mutableListOf("fotoperfil_acordeon", "fotoperfil_bateria",
        "fotoperfil_guitarra", "fotoperfil_harpa", "fotoperfil_maraca", "fotoperfil_piano", "fotoperfil_saxofon",
        "fotoperfil_tambor", "fotoperfil_trompeta", "fotoperfil_tronbon")

    var listaImagenesStorage: MutableList<String> = mutableListOf("pablo", "david", "bendicion", "pedro", "luis", "png-transparent-deer-deer-animal-deer-clipart.png")

    // FUN --> OnCreate
    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil_usuario_activity)
        binding = PerfilUsuarioActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            cardViewPerfil = findViewById(R.id.cardview_perfil)
            imagen = findViewById(R.id.roundedImageView)
            lapiz = findViewById(R.id.lapiz_editar)
            nivelRango = findViewById(R.id.nivelHabilidad)
            editText = findViewById(R.id.nombre_usuario)
            nombreUsuarioTextView = findViewById(R.id.nombre_usuario)
            gmailUsuarioTextView = findViewById(R.id.gmail_usuario)
            originalText = editText.text
            instancia = FirebaseDB.getInstanceFirebase()

            // Porcentaje barra Experiencia
            progressBar1 = findViewById(R.id.progressBarLogro1)
            porcentajeTextView1 = findViewById(R.id.TextViewLogro1)
            progressBar2 = findViewById(R.id.progressBarLogro2)
            porcentajeTextView2 = findViewById(R.id.TextViewLogro2)
            progressBar3 = findViewById(R.id.progressBarLogro3)
            porcentajeTextView3 = findViewById(R.id.TextViewLogro3)
            progressBar4 = findViewById(R.id.progressBarLogro4)
            porcentajeTextView4 = findViewById(R.id.TextViewLogro4)
            progressBar5 = findViewById(R.id.progressBarLogro5)
            porcentajeTextView5 = findViewById(R.id.TextViewLogro5)
            progressBar6 = findViewById(R.id.progressBarLogro6)
            porcentajeTextView6 = findViewById(R.id.TextViewLogro6)
            progressBar7 = findViewById(R.id.progressBarLogro7)
            porcentajeTextView7 = findViewById(R.id.TextViewLogro7)
            progressBar8 = findViewById(R.id.progressBarLogro8)
            porcentajeTextView8 = findViewById(R.id.TextViewLogro8)
        }

        fechaRegistro = findViewById(R.id.fechaRegistro)
        nivelTextView = findViewById(R.id.nivelTextView)
        precisionTextView = findViewById(R.id.precisionTextView)
        experienciaTextView = findViewById(R.id.experienciaTextView)
        centerCircle = findViewById(R.id.centerCircle)

        val userId = FirebaseDB.getInstanceFirebase().currentUser?.uid
        val storageRef: StorageReference = FirebaseDB.getInstanceStorage().reference.child("imagenesPerfilGente").child("$userId.jpg")
        val imagesRef = storageRef.child("imagenesPerfilGente/$userId.jpg")
        println("Image URL: $imagesRef")
        Glide.with(this)
            .load(imagesRef)
            .into(imagen)

        inicializarConBase()
        mostrarImagenGrande()
        crearMenuSuperior()

        setTextoLogro1(30)
        setTextoLogro2(5)
        setTextoLogro3(3)
        setTextoLogro4(99)
        setTextoLogro5(99)
        setTextoLogro6(20)
        setTextoLogro7(99)
        setTextoLogro8(99)

        setlogro1(30)
        setlogro2(5)
        setlogro3(3)
        //setlogro6(20)

        lifecycleScope.launch {
            downloadImage()
            downloadImage2()
        }

//oculta la barra de carga
        EligeModoJuegoActivity.instance.ocultarFragmento()

    }




    // Descarga la imagen correspondiente a la etapa del usuario
    private suspend fun downloadImage() {
        val storageRef = FirebaseDB.getInstanceStorage().reference
        val etapa = obtenerNombreEtapa()

        if (listaImagenesStorage.isNotEmpty()) {

            var nombreImagen = when (etapa) {
                "Novato" -> listaImagenesStorage[0]
                "Principiante" -> listaImagenesStorage[1]
                "Amateur" -> listaImagenesStorage[2]
                "Intermedio" -> listaImagenesStorage[3]
                "Avanzado" -> listaImagenesStorage[4]
                "Experto" -> listaImagenesStorage[5]
                else -> {null}
            }

            val imagesRef = storageRef.child("ImagenesEtapa/$nombreImagen.png")

            imagesRef.downloadUrl.addOnSuccessListener { url ->
                Glide.with(this)
                    .load(url)
                    .into(binding.centerCircle)
            }.addOnFailureListener { exception ->
                println("Error al cargar la imagen: ${exception.message}")
                imagen.setImageResource(R.mipmap.fotoperfil_guitarra)
            }
        } else {
            println("La lista de imágenes está vacía")
            imagen.setImageResource(R.mipmap.fotoperfil_guitarra)
        }
    }

    // Guarda la imagen de perfil del usuario en Firebase
    private suspend fun changeAndUploadImage(oldImageUrl: Uri) {
        withContext(Dispatchers.IO) {
            val userId = FirebaseDB.getInstanceFirebase().currentUser?.uid

            // Descarga la imagen en un Bitmap
            val bitmap = Glide.with(this@PerfilUsuarioActivity)
                .asBitmap()
                .load(oldImageUrl)
                .submit()
                .get()

            // Sube la nueva imagen a Firebase Storage con el nombre del usuario
            val newImageRef = FirebaseDB.getInstanceStorage().reference.child("imagenesPerfilGente").child("$userId.jpg")
            try {
                // Save the new image to Firebase Storage
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                Tasks.await(newImageRef.putBytes(data))

                // Now you can use newImageRef to show the image in your app if needed
            } catch (exception: Exception) {
                println("Error uploading new image: ${exception.message}")
            }
        }
    }

    //Descarga de Firebase la imagen de perfil correspondiente al usuario
    private fun downloadImage2() {
        val storageRef = FirebaseDB.getInstanceStorage().reference
        val userId = FirebaseDB.getInstanceFirebase().currentUser?.uid
        val imagesRef = storageRef.child("imagenesPerfilGente").child("$userId.jpg")

        imagesRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(this)
                .load(url)
                .into(imagen)

            // Call the function to change the name and upload the image
            runBlocking {
                changeAndUploadImage(url)
            }
        }.addOnFailureListener { exception ->
            println("Error al cargar la imagen: ${exception.message}")

            imagen.setImageResource(R.mipmap.fotoperfil_guitarra)
        }
    }

    // Obtiene el nombre de etapa según el nivel del usuario
    private suspend fun obtenerNombreEtapa(): String {
        val etapa: String? = when (UtilsDB.getNivelActual()) {
            in 1..10 -> mutableList[0]
            in 11..20 -> mutableList[1]
            in 21..30 -> mutableList[2]
            in 31..40 -> mutableList[3]
            in 41..50 -> mutableList[4]
            else -> mutableList[5]
        }
        nivelRango.text = etapa?.uppercase()
        return etapa.toString()
    }

    @SuppressLint("SetTextI18n")
    private fun inicializarConBase() = runBlocking{
        fechaRegistro.text = "Se unió en " + UtilsDB.obtenerFechaActualEnTexto()
        nombreUsuarioTextView.text = UtilsDB.getNombre()
        gmailUsuarioTextView.text = UtilsDB.getCorreo()
        experienciaTextView.text = UtilsDB.getExperiencia().toString()
        nivelTextView.text= UtilsDB.getNivelActual().toString()
        precisionTextView.text =UtilsDB.getMediaPrecisiones().toString()+"%"
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun crearMenuSuperior() {
        val constraintLayout: ConstraintLayout = findViewById(R.id.constraintLayoutID)
        constraintLayout.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Oculta el teclado
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)

                // Quita el foco del EditText
                editText.clearFocus()
            }
            false
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No es necesario implementar
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No es necesario implementar
            }

            override fun afterTextChanged(s: Editable?) {
                // No es necesario implementar
            }
        })
    }

    private fun mostrarDialogoConfirmacion() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cambiar nombre de usuario")
        builder.setMessage("¿Estás seguro de que quieres cambiar el nombre de usuario?")
        builder.setPositiveButton("Sí") { _: DialogInterface, _: Int ->
            cambiarNombreUsuario()
        }
        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun cambiarNombreUsuario() {

        Toast.makeText(this, "Usuario cambiado", Toast.LENGTH_SHORT).show()
    }

    private fun mostrarImagenGrande() {
        cardViewPerfil.setOnClickListener {
            mostrarDialogImagen(imagen)
        }

        lapiz.setOnClickListener {
            mostrarDialogoElegirOrigen()
        }
    }

    private fun mostrarDialogoElegirOrigen() {
        val opciones = arrayOf("Tomar foto", "Elegir de la galería")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccionar origen de la imagen")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> {
                        // Opción "Tomar foto" seleccionada
                        requestCameraPermission()
                    }

                    1 -> {
                        // Opción "Elegir de la galería" seleccionada
                        abrirGaleria()
                    }
                }
            }

        val dialog = builder.create()
        dialog.show()
    }

    // FUN --> Mostrar la imagen del perfil en grande
    private fun mostrarDialogImagen(imageView: ImageView) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_imagen)

        val imagenAmpliada = dialog.findViewById<ImageView>(R.id.imagenAmpliada)
        imagenAmpliada.setImageDrawable(imageView.drawable)

        // Animación de escala
        val escala = ScaleAnimation(
            0.2f,  // Escala de inicio
            1.0f,  // Escala de fin
            0.2f,  // Punto focal de inicio (X)
            0.2f,  // Punto focal  de inicio (Y)
            Animation.RELATIVE_TO_SELF, 0.5f,  // Punto focal de fin (X)
            Animation.RELATIVE_TO_SELF, 0.5f   // Punto focal de fin (Y)
        )

        escala.duration = 200  // Duración de la animación en milisegundos
        imagenAmpliada.startAnimation(escala)

        // Cierra el dialog al hacer clic en la imagen
        imagenAmpliada.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    private var selectedImageUri: Uri? = null
    val startForActivityGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.data
                selectedImageUri = data
                imagen.setImageURI(data)

                // Descargar la imagen desde la URI y guardarla en un archivo
                //descargarYGuardarImagen(selectedImageUri)
                guardarImagenEnFirebase(selectedImageUri)
            }

            val preferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("profileImageUri", selectedImageUri.toString())
            editor.apply()
        }

    private fun guardarImagenEnFirebase(imageUri: Uri?) {
        if (imageUri != null) {
            val userId = FirebaseDB.getInstanceFirebase().currentUser?.uid
            if (userId != null) {
                val storageRef = FirebaseDB.getInstanceStorage().reference
                val imagesRef = storageRef.child("imagenesPerfilGente").child("$userId.jpg")

                // Subir la imagen a Firebase Storage con el nuevo nombre
                imagesRef.putFile(imageUri)
                    .addOnSuccessListener {
                        println("Éxito al subir la imagen con el nombre $userId")
                    }
                    .addOnFailureListener { exception ->
                        println("Error al subir la imagen: ${exception.message}")
                    }
            } else {
                println("El ID del usuario es nulo.")
            }
        }
    }

    fun volverModoJuego(view: View) {
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        mediaPlayer.start()
        finish()
    }

    fun irConfiguracion(view: View) {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        mediaPlayer.start()
        val intent = Intent(this, ConfiguracionActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.fade_in_config_perfil, R.anim.fade_out);
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permiso concedido, abrir la cámara
                abrirCamara()
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
            }
        }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permiso ya concedido, abrir la cámara
                abrirCamara()
            }
            else -> {
                // Solicitar permiso para la cámara
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun guardarImagen(bitmap: Bitmap?) {
        val userId = FirebaseDB.getInstanceFirebase().currentUser?.uid

        if (userId != null) {
            val storageRef = FirebaseDB.getInstanceStorage().reference
            val imagesRef = storageRef.child("imagenesPerfilGente").child("$userId.jpg")

            // Guardar la imagen en Firebase Storage
            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            imagesRef.putBytes(data)
                .addOnSuccessListener {
                    println("Éxito al subir la imagen con el nombre $userId.jpg")
                }
                .addOnFailureListener { exception ->
                    println("Error al subir la imagen: ${exception.message}")
                }
        } else {
            println("El ID del usuario es nulo.")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            imagen.setImageBitmap(imageBitmap)
            guardarImagen(imageBitmap)
            val uri = data?.data

            val filePath = uri?.lastPathSegment?.let { miStorage.child("hit").child(it) }
            if (uri != null) {
                filePath?.putFile(uri)?.addOnSuccessListener { taskSnapshot ->
                    Toast.makeText(this, "Éxito al subir el archivo", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Error al subir el archivo: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun abrirCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    private fun setTextoLogro1(cantidad: Int) {
        var tituloLogro1 = findViewById<TextView>(R.id.tituloLogro1)
        tituloLogro1.text = "MAESTRO DEL DESAFÍO I"
        var fraseLogro1 = findViewById<TextView>(R.id.fraseLogro1)
        fraseLogro1.text = "Juega un total de $cantidad minutos"
    }
    private fun setTextoLogro2(cantidad: Int) {
        var tituloLogro2 = findViewById<TextView>(R.id.tituloLogro2)
        tituloLogro2.text = "CARRERA MUSICAL I"
        var fraseLogro2 = findViewById<TextView>(R.id.fraseLogro2)
        fraseLogro2.text = "Completa los primeros $cantidad niveles"
    }
    private fun setTextoLogro3(cantidad: Int) {
        var tituloLogro3 = findViewById<TextView>(R.id.tituloLogro3)
        tituloLogro3.text = "VIRTUOSO I"
        var fraseLogro3 = findViewById<TextView>(R.id.fraseLogro3)
        fraseLogro3.text = "Pasate $cantidad niveles musicales sin fallar"
    }
    private fun setTextoLogro4(cantidad: Int) {
        var tituloLogro4 = findViewById<TextView>(R.id.tituloLogro4)
        tituloLogro4.text = "OIDO DE ÁGUILA(en desarrollo)"
        var fraseLogro4 = findViewById<TextView>(R.id.fraseLogro4)
        fraseLogro4.text = "Pasate $cantidad niveles de oido sin fallar"
    }
    private fun setTextoLogro5(cantidad: Int) {
        var tituloLogro5 = findViewById<TextView>(R.id.tituloLogro5)
        tituloLogro5.text = "RITMO INVENCIBLE I(en desarrollo)"
        var fraseLogro5 = findViewById<TextView>(R.id.fraseLogro5)
        fraseLogro5.text = "Pasate $cantidad niveles de ritmo sin fallar"
    }
    private fun setTextoLogro6(cantidad: Int) {
        var tituloLogro6 = findViewById<TextView>(R.id.tituloLogro6)
        tituloLogro6.text = "HARMONIA CELESTIAL I"
        var fraseLogro6 = findViewById<TextView>(R.id.fraseLogro6)
        fraseLogro6.text = "Consigue una racha de $cantidad notas seguidas en el modo desafío Melódico"
    }
    private fun setTextoLogro7(cantidad: Int) {
        var tituloLogro7 = findViewById<TextView>(R.id.tituloLogro7)
        tituloLogro7.text = "OÍDO ABSOLUTO I(en desarrollo)"
        var fraseLogro7 = findViewById<TextView>(R.id.fraseLogro7)
        fraseLogro7.text = "Consigue una racha de $cantidad seguidas en el modo desafío Ritmico"
    }
    private fun setTextoLogro8(cantidad: Int) {
        var tituloLogro8 = findViewById<TextView>(R.id.tituloLogro8)
        tituloLogro8.text = "METRONOMO I(en desarrollo)"
        var fraseLogro8 = findViewById<TextView>(R.id.fraseLogro8)
        fraseLogro8.text = "Consigue una racha de $cantidad seguidas en el modo desafío Auditivo"
    }




    private fun setlogro1(cantidad: Int)= runBlocking{
        val porcentaje1 = UtilsDB.getTiempoJugado()?.div(60)
        progressBar1.progress = (porcentaje1!!.times(100).div(cantidad))
        porcentajeTextView1.text = "$porcentaje1/$cantidad"

    }
    private fun setlogro2(cantidad: Int)= runBlocking {
        val porcentaje2 = UtilsDB.getNivelActual()
        progressBar2.progress = (porcentaje2!!.times(100).div(cantidad))
        porcentajeTextView2.text = "$porcentaje2/$cantidad"
    }
    private fun setlogro3(cantidad: Int)= runBlocking {
        val porcentaje3 = UtilsDB.getCantidadPerfectos()
        progressBar3.progress = (porcentaje3!!.times(100).div(cantidad))
        porcentajeTextView3.text = "$porcentaje3/$cantidad"
    }

    /*private fun setlogro6(cantidad: Int)= runBlocking {
        val porcentaje6 = UtilsDB.getMayorPuntuacionDesafio(UtilsDB.getPuntuacionDesafio()!!).get("notas")!!.toInt()
        progressBar6.progress = (porcentaje6.times(100).div(cantidad))
        porcentajeTextView6.text = "$porcentaje6/$cantidad"
    }*/

}
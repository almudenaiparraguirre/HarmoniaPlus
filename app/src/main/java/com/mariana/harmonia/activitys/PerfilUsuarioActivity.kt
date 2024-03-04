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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mariana.harmonia.R
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import android.util.Base64
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storageMetadata
import com.mariana.harmonia.utils.Utils
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min

class PerfilUsuarioActivity : AppCompatActivity() {

    companion object {
        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var nombreUsuarioTextView: TextView
        private lateinit var gmailUsuarioTextView: TextView
        private const val PERMISSION_REQUEST_CODE = 122
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
    var mutableList: MutableList<String> = mutableListOf(
        "Novato", "Principiante", "Amateur",
        "Intermedio", "Avanzado", "Experto", "Maestro", "Leyenda", "Virtuoso", "Genio"
    )

    // FUN --> OnCreate
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) = runBlocking {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil_usuario_activity)
        cardViewPerfil = findViewById(R.id.cardview_perfil)
        imagen = findViewById(R.id.roundedImageView)
        lapiz = findViewById(R.id.lapiz_editar)
        nivelRango = findViewById(R.id.nivelHabilidad)
        fechaRegistro = findViewById(R.id.fechaRegistro)
        fechaRegistro.text = "Se unió en " + Utils.obtenerFechaActualEnTexto()
        editText = findViewById(R.id.nombre_usuario)
        nombreUsuarioTextView = findViewById(R.id.nombre_usuario)
        gmailUsuarioTextView = findViewById(R.id.gmail_usuario)
        nombreUsuarioTextView.text = Utils.getNombre()
        gmailUsuarioTextView.text = Utils.getCorreo()
        experienciaTextView.text = Utils.getExperiencia()
        mostrarImagenGrande()

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

        nivelTextView = findViewById(R.id.nivelTextView)
        precisionTextView = findViewById(R.id.precisionTextView)
        experienciaTextView = findViewById(R.id.experienciaTextView)


        val porcentaje1 = 10
        progressBar1.progress = porcentaje1
        porcentajeTextView1.text = "$porcentaje1/100"

        val porcentaje2 = 20
        progressBar2.progress = porcentaje2
        porcentajeTextView2.text = "$porcentaje2/100"

        val porcentaje3 = 30
        progressBar3.progress = porcentaje3
        porcentajeTextView3.text = "$porcentaje3/100"

        val porcentaje4 = 40
        progressBar4.progress = porcentaje4
        porcentajeTextView4.text = "$porcentaje4/100"

        val porcentaje5 = 50
        progressBar5.progress = porcentaje5
        porcentajeTextView5.text = "$porcentaje5/100"

        val porcentaje6 = 60
        progressBar6.progress = porcentaje6
        porcentajeTextView6.text = "$porcentaje6/100"

        val porcentaje7 = 70
        progressBar7.progress = porcentaje7
        porcentajeTextView7.text = "$porcentaje7/100"

        val porcentaje8 = 80
        progressBar8.progress = porcentaje8
        porcentajeTextView8.text = "$porcentaje8/100"

        val niveles: JSONObject? = obtenerNivelesJSON()
        val ultimoNivelCompletadoId = obtenerUltimoNivelCompletado(niveles)

        if (ultimoNivelCompletadoId != null) {
            nivelRango.text = asignarNivelHabilidad(ultimoNivelCompletadoId)
        } else {
            nivelRango.text = "NOVATO"
        }

        iniciarContadorToast()
        println(Utils.getNivelActual())
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


    private fun iniciarContadorToast() {
        Toast.makeText(
            this,
            "Pulse sobre el email o nombre de usuario para editarlos",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun obtenerUltimoNivelCompletado(nivelesJson: JSONObject?): Int? {
        val nivelesArray = nivelesJson?.getJSONArray("niveles")

        if (nivelesArray != null) {
            for (i in nivelesArray.length() - 1 downTo 0) {
                val nivel = nivelesArray.getJSONObject(i)
                val completado = nivel.getBoolean("completado")

                if (completado) {
                    return nivel.getInt("id")
                }
            }
        }
        return null
    }

    private fun obtenerNivelesJSON(): JSONObject? {
        var nivelesJson: JSONObject? = null
        try {
            val inputStream: InputStream = resources.openRawResource(R.raw.info_niveles)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonString = String(buffer, Charsets.UTF_8)
            nivelesJson = JSONObject(jsonString)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return nivelesJson
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_MEDIA_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            abrirGaleria()
        } else {
            // Solicitar permisos explícitamente
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_MEDIA_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        }
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
                descargarYGuardarImagen(selectedImageUri)
            }

            val preferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("profileImageUri", selectedImageUri.toString())
            editor.apply()
        }

    private fun descargarYGuardarImagen(imageUri: Uri?) {
        if (imageUri != null) {
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)

            // Convertir la imagen a una cadena Base64
            val base64String = inputStream?.readBytes()?.toString(Charsets.ISO_8859_1)

            // Crear o recuperar el archivo "imagenes.txt" en el directorio de almacenamiento externo
            val file = File(getExternalFilesDir(null), "imagenes.txt")

            try {
                // Crear un flujo de salida para escribir en el archivo
                val fileOutputStream = FileOutputStream(file)

                // Escribir la cadena Base64 en el archivo
                fileOutputStream.write(base64String?.toByteArray(Charsets.ISO_8859_1))

                // Cerrar los flujos de entrada y salida
                inputStream?.close()
                fileOutputStream.close()

                // Mostrar un mensaje de éxito
                Toast.makeText(
                    this,
                    "Imagen descargada y guardada correctamente",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Error al descargar y guardar la imagen", Toast.LENGTH_SHORT)
                    .show()
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
        // Guardar la imagen en preferencias o en otro lugar si es necesario
        // Puedes utilizar SharedPreferences o almacenamiento en el sistema de archivos
        // Ejemplo usando SharedPreferences:
        val preferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("profileImageBitmap", encodeBitmapToBase64(bitmap))
        editor.apply()
    }

    private fun encodeBitmapToBase64(bitmap: Bitmap?): String {
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
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

    private fun asignarNivelHabilidad(nivelActual: Int): String {
        val nivelesDeHabilidad = mutableListOf(
            "Novato", "Principiante", "Amateur", "Intermedio",
            "Avanzado", "Experto", "Maestro", "Leyenda", "Virtuoso", "Genio"
        )

        val indiceNivel = (nivelActual - 1) / 10

        // Asegúrate de no exceder el índice máximo
        val indiceFinal = min(indiceNivel, nivelesDeHabilidad.size - 1)

        // Obtiene el nivel de habilidad correspondiente
        return nivelesDeHabilidad[indiceFinal]
    }
}
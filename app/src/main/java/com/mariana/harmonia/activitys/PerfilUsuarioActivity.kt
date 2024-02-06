package com.mariana.harmonia.activitys

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.*
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.google.android.material.progressindicator.BaseProgressIndicator
import com.mariana.harmonia.InicioSesionActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.databinding.ActivityMain2Binding
import com.mariana.harmonia.databinding.ActivityPrincipalBinding

class PerfilUsuarioActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
    }

    private lateinit var binding: ActivityPrincipalBinding
    private lateinit var imagen: ImageView
    private lateinit var lapiz: ImageView

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            //val message = if (isGranted) "Permission Granted" else "Permission rejected"
            //Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            if (isGranted) {
                abrirGaleria()
            } else {
                Toast.makeText(this, "Necesitas activar los permisos de la galería", Toast.LENGTH_SHORT).show()
            }
        }

    // FUN --> OnCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_usuario)

        val cardViewPerfil = findViewById<CardView>(R.id.cardview_perfil)
        imagen = findViewById(R.id.roundedImageView)
        val fondoMitadSuperior = findViewById<ImageView>(R.id.fondoMitadSuperior)
        lapiz = findViewById(R.id.lapiz_editar)
        val fondoMitadSuperiorBack = findViewById<ImageView>(R.id.fondoMitadSuperiorBackground)

        cardViewPerfil.setOnClickListener {
            mostrarDialogImagen(imagen)
        }
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
       lapiz.setOnClickListener { requestPermission() }

        // Obtener el color promedio de la imagen
        val bitmap = (imagen.drawable as BitmapDrawable).bitmap
        val colorPromedio = obtenerColorPromedio(bitmap)

        //Este lIstener hace que al clicar fuera de la pantalla se deje de rellenar el campo del nombre
        val constraintLayout: ConstraintLayout = findViewById(R.id.constraintLayoutID) // Reemplaza con el ID de tu ConstraintLayout
        val editText: EditText = findViewById(R.id.nombre_usuario) // Reemplaza con el ID de tu EditText

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
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    abrirGaleria()
                }

                else -> requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            abrirGaleria()
        }
    }

    private fun obtenerColorPromedio(bitmap: Bitmap): Int {
        var red = 0
        var green = 0
        var blue = 0
        var pixelCount = 0

        for (y in 0 until bitmap.height) {
            for (x in 0 until bitmap.width) {
                val pixel = bitmap.getPixel(x, y)
                red += Color.red(pixel)
                green += Color.green(pixel)
                blue += Color.blue(pixel)
                pixelCount++
            }
        }

        red /= pixelCount
        green /= pixelCount
        blue /= pixelCount

        return Color.rgb(red, green, blue)
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
        //val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    val startForActivityGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.data
                imagen.setImageURI(data)
            }
        }

    /*private fun solicitarPermisoManualmente() {
        // Mostrar un diálogo de solicitud de permisos
        AlertDialog.Builder(this)
            .setTitle("Permiso necesario")
            .setMessage("Se requiere permiso para acceder a la galería. Por favor, concede el permiso en la configuración de la aplicación.")
            .setPositiveButton("OK") { _, _ ->
                // Abre la configuración de la aplicación para que el usuario pueda conceder el permiso manualmente
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancelar") { _, _ ->
                // El usuario canceló la solicitud de permisos
                Toast.makeText(
                    this,
                    "Permiso denegado. No se puede acceder a la galería.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .show()
    }*/


    /*val permisosGaleria =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permiso concedido, abrir la galería
                abrirGaleria()
            } else {
                // Permiso denegado, mostrar la solicitud de permisos nuevamente
                solicitarPermisoManualmente()
            }
        }*/

    /*private val seleccionarImagen =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Obtener la URI de la imagen seleccionada
                val uri = result.data?.data
                // Cargar la nueva imagen y realizar otras operaciones
                cargarNuevaImagen(uri)
            } else {
                Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
            }
        }*/

    // ...

    /*private fun cargarNuevaImagen(uri: Uri?) {
        val imageView = findViewById<ImageView>(R.id.roundedImageView)
        imageView.setImageURI(uri)

        // Obtener el color promedio de la nueva imagen
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val colorPromedio = obtenerColorPromedio(bitmap)

        // Aplicar el color de fondo a la mitad superior
        val fondoMitadSuperior = findViewById<ImageView>(R.id.fondoMitadSuperior)
        val fondoMitadSuperiorBack = findViewById<ImageView>(R.id.fondoMitadSuperiorBackground)
        fondoMitadSuperior.setColorFilter(colorPromedio)
        fondoMitadSuperiorBack.setColorFilter(colorPromedio)

        // Otras operaciones que deseas realizar después de cargar la nueva imagen
        // ...
    }*/

    /*override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, abrir la galería
                abrirGaleria()
            } else {
                Toast.makeText(
                    this,
                    "Permiso denegado. No se puede acceder a la galería.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }*/

    fun volverModoJuego(view: View){
        val intent = Intent(this, EligeModoJuegoActivity::class.java)
        startActivity(intent)
        finish()
    }
}
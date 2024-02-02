package com.mariana.harmonia.activitys

import android.Manifest
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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.mariana.harmonia.R

class PerfilUsuarioActivity : AppCompatActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            val message = if (isGranted) "Permission Granted" else "Permission rejected"
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()

            if (isGranted) {
                // Permiso concedido, ahora puedes realizar acciones adicionales si es necesario
                abrirGaleria()
            } else {
                // Permiso denegado, puedes manejarlo de acuerdo a tus necesidades
                Toast.makeText(this, "No se puede acceder a la galería.", Toast.LENGTH_SHORT).show()
                solicitarPermisosGaleria()
            }
        }

    // FUN --> OnCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_usuario)

        val cardViewPerfil = findViewById<CardView>(R.id.cardview_perfil)
        val imageView = findViewById<ImageView>(R.id.roundedImageView)
        val fondoMitadSuperior = findViewById<ImageView>(R.id.fondoMitadSuperior)
        val lapiz = findViewById<ImageView>(R.id.lapiz_editar)
        val fondoMitadSuperiorBack = findViewById<ImageView>(R.id.fondoMitadSuperiorBackground)

        cardViewPerfil.setOnClickListener {
            mostrarDialogImagen(imageView)
        }

        // Obtener el color promedio de la imagen
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val colorPromedio = obtenerColorPromedio(bitmap)

        // Establecer el color de fondo de la mitad superior
        /*fondoMitadSuperior.setColorFilter(colorPromedio)
        fondoMitadSuperiorBack.setColorFilter(colorPromedio)*/
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

    // Selección de imagen de la galería
    private val seleccionarImagen =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Obtener la URI de la imagen seleccionada
                val uri = result.data?.data
                // Aquí puedes guardar la URI en tu ImageView o realizar otras operaciones
                val imageView = findViewById<ImageView>(R.id.roundedImageView)
                imageView.setImageURI(uri)
            } else {
                Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
            }
        }


    private fun abrirGaleria() {
        // Lanzar la solicitud para acceder a la galería
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        seleccionarImagen.launch(intent)
    }

    private fun solicitarPermisosGaleria() {
        // Verificar si el permiso ya está concedido
        while (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // El permiso no está concedido, solicitarlo
            requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        // El permiso ya está concedido, abrir la galería
        abrirGaleria()
    }

    val permisosGaleria =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permiso concedido, abrir la galería
                abrirGaleria()
            } else {
                Toast.makeText(this, "Permiso denegado. No se puede acceder a la galería.", Toast.LENGTH_SHORT).show()
            }
        }


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

    // FUN --> Carga la imagen
    fun cargarImagen(view: View) {
        // Solicitar permisos antes de iniciar la galería
        solicitarPermisosGaleria()
    }

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

}
package com.mariana.harmonia.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest
import java.util.Locale

class Utils {
    companion object {
        val firebaseAuth = FirebaseAuth.getInstance()
        private val db = FirebaseFirestore.getInstance()
        val currentUser = firebaseAuth.currentUser
        private val usersCollection = db.collection("usuarios")
        val email = currentUser?.email?.lowercase()
        val emailEncriptado = HashUtils.sha256(email!!)


        suspend fun getCorreo(): String? {
            val docRef = db.collection("usuarios").document(emailEncriptado)
            return try {
                val document = docRef.get().await()
                if (document.exists()) {
                    val nivelActual = document.data?.get("correo")
                    println("Nivel actual: $nivelActual")
                    nivelActual?.toString()
                } else {
                    println("No such document")
                    null
                }
            } catch (exception: Exception) {
                println("Error getting documents: $exception")
                null
            }
        }

        suspend fun getNombre(): String? {
            val docRef = db.collection("usuarios").document(emailEncriptado)

            return try {
                val document = docRef.get().await()
                if (document.exists()) {
                    val nivelActual = document.data?.get("name")
                    println("Nivel actual: $nivelActual")
                    nivelActual?.toString()
                } else {
                    println("No such document")
                    null
                }
            } catch (exception: Exception) {
                println("Error getting documents: $exception")
                null
            }
        }




        suspend fun getExperiencia(): Int? {
            val docRef = db.collection("usuarios").document(emailEncriptado)

            return try {
                val document = docRef.get().await()
                if (document.exists()) {
                    val experiencia = document.data?.get("experiencia") as? Long
                    experiencia?.toInt()
                } else {
                    println("No such document")
                    null
                }
            } catch (exception: Exception) {
                println("Error getting documents: $exception")
                null
            }
        }




        suspend fun getNivelActual(): Int? {
            val docRef = db.collection("usuarios").document(emailEncriptado)

            return try {
                val document = docRef.get().await()
                if (document.exists()) {
                    val nivelActual = document.data?.get("nivelActual")
                    println("Nivel actual: $nivelActual")
                    (nivelActual as? Long)?.toInt() // Convertir a Int
                } else {
                    println("No existe el documento")
                    null
                }
            } catch (exception: Exception) {
                println("Error al obtener documentos: $exception")
                null
            }
        }

        suspend fun getVidas(): String? {
            val docRef = db.collection("usuarios").document(emailEncriptado)

            return try {
                val document = docRef.get().await()
                if (document.exists()) {
                    val nivelActual = document.data?.get("vidas")
                    println("Nivel actual: $nivelActual")
                    nivelActual?.toString()
                } else {
                    println("No such document")
                    null
                }
            } catch (exception: Exception) {
                println("Error getting documents: $exception")
                null
            }
        }
        suspend fun getPrecisiones(): List<Int>? {
            val docRef = db.collection("usuarios").document(emailEncriptado)

            return try {
                val document = docRef.get().await()
                if (document.exists()) {
                    val precisionesList = document.data?.get("precisiones") as? List<Int>
                    println("precisiones: $precisionesList")
                    precisionesList
                } else {
                    println("No such document")
                    null
                }
            } catch (exception: Exception) {
                println("Error getting documents: $exception")
                null
            }
        }



        @SuppressLint("SimpleDateFormat")
        fun obtenerFechaActualEnTexto(): String {
            val fechaActual: String

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Para versiones de Android 8.0 y superiores
                val currentDate = java.time.LocalDate.now()
                val formatter = java.time.format.DateTimeFormatter.ofPattern("MMMM 'de' yyyy", Locale.getDefault())
                fechaActual = currentDate.format(formatter)
            } else {
                // Para versiones anteriores a Android 8.0
                val currentDate = Calendar.getInstance().time
                val formato = SimpleDateFormat("MMMM 'de' yyyy", Locale.getDefault())
                fechaActual = formato.format(currentDate)
            }

            return fechaActual
        }



        fun setCorreo(correo: String) {
            val data = hashMapOf(
                "correo" to correo
                // Agrega cualquier otro campo que necesites actualizar
            )

            usersCollection.document(emailEncriptado).update(data as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Correo actualizado para el usuario con email: $email")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error al actualizar correo para el usuario con email: $email", e)
                }
        }

        fun setExperiencia(nuevaExperiencia: Int) {
                  val data = hashMapOf(
                "experiencia" to nuevaExperiencia
                // Agrega cualquier otro campo que necesites actualizar
            )

            usersCollection.document(emailEncriptado).update(data as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Experiencia actualizada para el usuario con email: $email")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error al actualizar experiencia para el usuario con email: $email", e)
                }
        }
        fun setNivelActual(nuevoNivelActual: Int) {
            val data = hashMapOf(
                "nivelActual" to nuevoNivelActual
                // Agrega cualquier otro campo que necesites actualizar
            )

            usersCollection.document(emailEncriptado).update(data as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "NivelActual actualizada para el usuario con email: $email")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error al actualizar NivelActual para el usuario con email: $email", e)
                }
        }
        fun setVidas(nuevaVidas: Int) {
            val data = hashMapOf(
                "vidas" to nuevaVidas
                // Agrega cualquier otro campo que necesites actualizar
            )

            usersCollection.document(emailEncriptado).update(data as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "nuevaVidas actualizada para el usuario con email: $email")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error al actualizar nuevaVidas para el usuario con email: $email", e)
                }
        }
        fun setPrecisiones(precisiones: List<Int>) {
            val data = hashMapOf(
                "precisiones" to precisiones
            )
            usersCollection.document(emailEncriptado).update(data as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Precisión actualizada correctamente")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error al actualizar la precisión", e)
                }
        }


        suspend fun getMediaPrecisiones(): Int {
            val precisionesList = Utils.getPrecisiones() ?: return 0

            if (precisionesList.isNotEmpty()) {
                var suma = 0
                var contador = 0

                for (precision in precisionesList) {
                    if (precision != 0) {
                        suma += precision
                        contador++
                    }
                }

                return if (contador > 0) {
                    suma / contador
                } else {
                    0
                }
            }

            return 0
        }

        fun readJsonFromRaw(resourceId: Int, context: Context): String {
            val inputStream: InputStream = context.resources.openRawResource(resourceId)
            val json = inputStream.bufferedReader().use { it.readText() }
            inputStream.close()
            return json
        }

        fun serializeImage(context: Context, resourceId: Int) {
            try {
                val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val imageBytes: ByteArray = outputStream.toByteArray()
                outputStream.close()
                val base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)

                val enviroment = Environment.getExternalStorageDirectory()
                val textFile = File(enviroment, "Download/imagenSerializada.json")

                try {
                    val fos = FileOutputStream(textFile)
                    fos.write(base64Image.toByteArray())
                    fos.close()
                } catch (e: IOException) {
                   println( e.toString())
                    // Manejar la excepción, por ejemplo, mostrar un mensaje al usuario
                }
            } catch (e: IOException) {
                e.printStackTrace()

                // Manejar la excepción, por ejemplo, mostrar un mensaje al usuario
            }
        }




        fun deserializeImage(context: Context, jsonFilePath: String): Bitmap? {
            try {
                val jsonFile = File(jsonFilePath)
                if (!jsonFile.exists()) {
                    println("El archivo JSON no existe.")
                    return null
                }

                val inputStream: InputStream = FileInputStream(jsonFile)
                val jsonString = inputStream.bufferedReader().use { it.readText() }

                val imageBytes: ByteArray = Base64.decode(jsonString, Base64.DEFAULT)
                val inputStreamImage: InputStream = ByteArrayInputStream(imageBytes)
                return BitmapFactory.decodeStream(inputStreamImage)
            } catch (e: IOException) {
                e.printStackTrace()
                println(e.toString())
                // Manejar la excepción, por ejemplo, mostrar un mensaje al usuario
            }
            return null
        }


         fun isExternalStorageWritable(): Boolean {
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                Log.i("State", "Yes, it is writable!")
                println("Yes, it is writable!")
                return true
            }else{
                Log.i("State", "Caution, it's not writable!")
                println("Caution, it's not writable!")
            }
            return false
        }

         fun isExternalStorageReadable(): Boolean {
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() ||
                Environment.MEDIA_MOUNTED_READ_ONLY == Environment.getExternalStorageState()) {
                Log.i("State", "Yes, it is readable!")
                println("Yes, it is readable!")
                return true
            }else{
                Log.i("State", "Caution, it's not readable!")
                println("Caution, it's not readable!")
            }
            return false
        }

         fun hashString(type: String, input: String): String {
            val HEX_CHARS = "0123456789ABCDEF"
            val bytes = MessageDigest
                .getInstance(type)
                .digest(input.toByteArray())
            val result = StringBuilder(bytes.size * 2)

            bytes.forEach {
                val i = it.toInt()
                result.append(HEX_CHARS[i shr 4 and 0x0f])
                result.append(HEX_CHARS[i and 0x0f])
            }

            return result.toString()

    }

    }
}

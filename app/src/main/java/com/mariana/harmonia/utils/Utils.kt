package com.mariana.harmonia.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.LinearGradient
import android.graphics.Shader
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mariana.harmonia.models.db.FirebaseDB
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class Utils {
    companion object {


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
                    println(e.toString())
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
            } else {
                Log.i("State", "Caution, it's not writable!")
                println("Caution, it's not writable!")
            }
            return false
        }

        fun isExternalStorageReadable(): Boolean {
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() ||
                Environment.MEDIA_MOUNTED_READ_ONLY == Environment.getExternalStorageState()
            ) {
                Log.i("State", "Yes, it is readable!")
                println("Yes, it is readable!")
                return true
            } else {
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
        fun salirAplicacion(activity: Activity){
            activity.finishAffinity()
        }

        fun degradadoTexto(contexto: Context, id: Int, colorRosa: Int, colorMorado: Int) {
            val titleTextView = (contexto as Activity).findViewById<TextView>(id)
            val paint = titleTextView.paint
            val width = paint.measureText(titleTextView.text.toString())

            titleTextView.paint.shader = LinearGradient(
                0f, 0f, width, titleTextView.textSize,
                intArrayOf(
                    contexto.resources.getColor(colorRosa),
                    contexto.resources.getColor(colorMorado)
                ),
                null,
                Shader.TileMode.CLAMP
            )
        }

    }


}


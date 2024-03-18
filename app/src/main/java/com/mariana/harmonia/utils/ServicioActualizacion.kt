package com.mariana.harmonia.utils

import android.app.AlertDialog
import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import kotlinx.coroutines.runBlocking
import android.content.Context
import android.net.ConnectivityManager
import com.mariana.harmonia.activitys.EligeModoJuegoActivity

class ServicioActualizacion : Service() {

    var countDownTimer: CountDownTimer? = null
    var segundosTranscurridos: Long = 0
    var mostrandoMenu: Boolean = false

    override fun onBind(intent: Intent?): IBinder? {
        // No necesitas este método si no planeas interactuar con el servicio a través de unión
        return null
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        iniciarBaseDatos()
        startTimer()
        return START_STICKY
    }

    fun iniciarBaseDatos()= runBlocking {
            var tiempo = UtilsDB.getTiempoJugado()

            tiempo = tiempo ?: 0 // Si tiempo es nulo, asigna 0
            segundosTranscurridos = tiempo.toLong()
        }
    fun subirSegundoBD()= runBlocking {
      UtilsDB.setTiempoJugado(segundosTranscurridos.toInt())
        UtilsDB.setUltimoTiempo()
    }


    private fun startTimer() {
        countDownTimer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Incrementa los segundos transcurridos cada vez que se llama a onTick

                segundosTranscurridos++

                subirSegundoBD()
                verificarConexionInternet(EligeModoJuegoActivity.instance)
            }

            override fun onFinish() {

                // El temporizador nunca finaliza debido a Long.MAX_VALUE
            }
        }

        countDownTimer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel() // Detener el temporizador cuando el servicio se destruye
        // Subir el tiempo total a la base de datos cuando el servicio se destruye
        println("destruido$segundosTranscurridos")

        UtilsDB.setTiempoJugado(segundosTranscurridos.toInt())
    }

    private fun verificarConexionInternet(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        // Si no hay conexión a internet, muestra el diálogo
        if (networkInfo == null || !networkInfo.isConnected) {
            mostrarDialogoSinConexion(context)
        }
    }

    private fun mostrarDialogoSinConexion(context: Context) {
        if (!mostrandoMenu) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("No hay conexión a internet")
            builder.setMessage("Por favor, revisa tu conexión e intenta nuevamente.")

            // Botón de reintentar
            builder.setPositiveButton("Reintentar") { dialog, _ ->
                dialog.dismiss()
                verificarConexionInternet(context)
                mostrandoMenu = false
            }

            // Botón de salir
            builder.setNegativeButton("Salir") { dialog, _ ->
                dialog.dismiss()
                EligeModoJuegoActivity.instance.finish()
                mostrandoMenu = false
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        mostrandoMenu = true
    }

    // Método para subir el tiempo total a la base de datos
}
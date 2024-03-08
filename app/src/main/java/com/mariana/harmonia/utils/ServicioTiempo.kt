package com.mariana.harmonia.utils

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import kotlinx.coroutines.runBlocking
import  android.app.Application
import kotlinx.coroutines.delay

class ServicioTiempo : Service() {

    var countDownTimer: CountDownTimer? = null
    var segundosTranscurridos: Long = 0

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
    }


    private fun startTimer() {
        countDownTimer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Incrementa los segundos transcurridos cada vez que se llama a onTick

                segundosTranscurridos++

                subirSegundoBD()
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
        println("destruido" + segundosTranscurridos)

        UtilsDB.setTiempoJugado(segundosTranscurridos.toInt())
    }


    // Método para subir el tiempo total a la base de datos



}
package com.mariana.harmonia.activitys

import android.app.Activity
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView

class Utilidades {

    companion object{
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
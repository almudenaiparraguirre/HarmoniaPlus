package com.mariana.harmonia.activitys.fragments

import android.content.Intent
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.mariana.harmonia.R

class CargaFragment : Fragment() {

    lateinit var imageView: ImageView
    lateinit var animatedVectorDrawable: AnimatedVectorDrawable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_carga, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.animated_vector_imageview)

        // Obtener el AnimatedVectorDrawable desde los recursos
        animatedVectorDrawable = imageView.drawable as AnimatedVectorDrawable

        // Iniciar la animación
        animatedVectorDrawable.start()

        // Configurar un listener para la animación
        animatedVectorDrawable.registerAnimationCallback(object : Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                // Reiniciar la animación al finalizar
                animatedVectorDrawable.start()
            }
        })
    }
}

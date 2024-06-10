package com.mariana.harmonia.activities.fragments

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

/**
 * Fragmento utilizado para mostrar una animación con AnimatedVectorDrawable en una ImageView durante la carga.
 *
 * Esta clase extiende [Fragment] y se encarga de la visualización de la animación de carga.
 */
class CargaFragment : Fragment() {

    lateinit var imageView: ImageView
    lateinit var animatedVectorDrawable: AnimatedVectorDrawable

    /**
     * Método llamado para crear y devolver la jerarquía de vistas asociada con el fragmento.
     *
     * @param inflater El objeto [LayoutInflater] que se utiliza para inflar la vista.
     * @param container El contenedor al que se adjunta la vista inflada.
     * @param savedInstanceState Bundle que contiene el estado previamente guardado del fragmento.
     * @return La vista root del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño del fragmento
        return inflater.inflate(R.layout.fragment_carga, container, false)
    }

    /**
     * Método llamado después de que onCreateView ha finalizado, para realizar la inicialización.
     *
     * @param view La vista root del fragmento.
     * @param savedInstanceState Bundle que contiene el estado previamente guardado del fragmento.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.animated_vector_imageview)
        animatedVectorDrawable = imageView.drawable as AnimatedVectorDrawable
        animatedVectorDrawable.start()
        animatedVectorDrawable.registerAnimationCallback(object : Animatable2.AnimationCallback() {
            /**
             * Método llamado cuando la animación ha finalizado.
             *
             * @param drawable El objeto [Drawable] que contiene la animación.
             */
            override fun onAnimationEnd(drawable: Drawable?) {
                animatedVectorDrawable.start()
            }
        })
    }
}

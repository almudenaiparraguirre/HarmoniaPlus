package com.mariana.harmonia.activitys.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mariana.harmonia.R
import com.mariana.harmonia.activitys.JuegoMusicalActivity
import com.mariana.harmonia.utils.Utils

class FragmentoDificultadDesafio : Fragment() {


    private lateinit var botonAventura: androidx.appcompat.widget.AppCompatButton
    private lateinit var botonDesafio: androidx.appcompat.widget.AppCompatButton
    private lateinit var botonOpciones: androidx.appcompat.widget.AppCompatButton

    private lateinit var botonFacil: androidx.appcompat.widget.AppCompatButton
    private lateinit var botonMedio: androidx.appcompat.widget.AppCompatButton
    private lateinit var botonFificil: androidx.appcompat.widget.AppCompatButton
    private lateinit var textViewVolver: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        // Infla el diseño del fragmento que contiene los botones
        val view = inflater.inflate(R.layout.fragment_dificultad_desafio, container, false)

        botonAventura = requireActivity().findViewById(R.id.botonAventura)
        botonDesafio = requireActivity().findViewById(R.id.botonChallenge)
        botonOpciones = requireActivity().findViewById(R.id.botonOpciones)

        // Encuentra los botones en el diseño
        botonFacil = view.findViewById(R.id.botonFacil)
        botonMedio = view.findViewById(R.id.botonMedio)
        botonFificil = view.findViewById(R.id.botonFificil)
        textViewVolver = view.findViewById(R.id.textViewVolver)
        //Utils.degradadoTexto(requireActivity(),R.id.tituloDificultad,R.color.rosa,R.color.morado)

        // Configura el comportamiento de los botones
        botonFacil.setOnClickListener {
            val intent = Intent(requireActivity(), JuegoMusicalActivity::class.java)
            intent.putExtra("desafio", true)
            intent.putExtra("dificultad", 0)
            requireActivity().startActivity(intent)
            encenderBotonesDetras()

            // Cierra el fragmento actual
            requireActivity().supportFragmentManager.popBackStack()
        }

        botonMedio.setOnClickListener {
            val intent = Intent(requireActivity(), JuegoMusicalActivity::class.java)
            intent.putExtra("desafio", true)
            intent.putExtra("dificultad", 1)
            requireActivity().startActivity(intent)
            encenderBotonesDetras()

            // Cierra el fragmento actual
            requireActivity().supportFragmentManager.popBackStack()
        }

        botonFificil.setOnClickListener {
            val intent = Intent(requireActivity(), JuegoMusicalActivity::class.java)
            intent.putExtra("desafio", true)
            intent.putExtra("dificultad", 2)
            requireActivity().startActivity(intent)
            encenderBotonesDetras()

            // Cierra el fragmento actual
            requireActivity().supportFragmentManager.popBackStack()
        }


        textViewVolver.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            var botonAventura =
                requireActivity().findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.botonAventura)
            var botonDesafio =
                requireActivity().findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.botonChallenge)
            var botonOpciones =
                requireActivity().findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.botonOpciones)
            encenderBotonesDetras()
        }


        return view
    }
    fun encenderBotonesDetras(){
        botonAventura.visibility = View.VISIBLE
        botonDesafio.visibility = View.VISIBLE
        botonOpciones.visibility = View.VISIBLE
    }

}
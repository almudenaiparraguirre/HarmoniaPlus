package com.mariana.harmonia.activitys.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mariana.harmonia.R
import com.mariana.harmonia.activitys.JuegoMusicalActivity

/**
 * FragmentoDificultadDesafio es un Fragment que permite al usuario seleccionar la dificultad
 * para un desafío musical. Contiene botones para las dificultades "Fácil", "Medio" y "Difícil".
 */
class FragmentoDificultadDesafio : Fragment() {

    // Declaración de variables para los botones y TextView
    private lateinit var botonFacil: androidx.appcompat.widget.AppCompatButton
    private lateinit var botonMedio: androidx.appcompat.widget.AppCompatButton
    private lateinit var botonFificil: androidx.appcompat.widget.AppCompatButton
    private lateinit var textViewVolver: TextView

    /**
     * Método llamado para crear y devolver la jerarquía de vistas asociada con el fragmento.
     * @param inflater El objeto LayoutInflater que puede inflar vistas en el contexto actual.
     * @param container Si no es nulo, este es el grupo de vistas al que se adjunta el fragmento.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo reconstituido a partir de un estado guardado previamente.
     * @return Devuelve la Vista principal del fragmento.
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dificultad_desafio, container, false)

        botonFacil = view.findViewById(R.id.botonFacil)
        botonMedio = view.findViewById(R.id.botonMedio)
        botonFificil = view.findViewById(R.id.botonFificil)
        textViewVolver = view.findViewById(R.id.textViewVolver)

        botonFacil.setOnClickListener {
            val intent = Intent(requireActivity(), JuegoMusicalActivity::class.java)
            intent.putExtra("desafio", true)
            intent.putExtra("dificultad", 0)
            requireActivity().startActivity(intent)

            requireActivity().supportFragmentManager.popBackStack()
        }

        botonMedio.setOnClickListener {
            val intent = Intent(requireActivity(), JuegoMusicalActivity::class.java)
            intent.putExtra("desafio", true)
            intent.putExtra("dificultad", 1)
            requireActivity().startActivity(intent)

            requireActivity().supportFragmentManager.popBackStack()
        }

        botonFificil.setOnClickListener {
            val intent = Intent(requireActivity(), JuegoMusicalActivity::class.java)
            intent.putExtra("desafio", true)
            intent.putExtra("dificultad", 2)
            requireActivity().startActivity(intent)

            requireActivity().supportFragmentManager.popBackStack()
        }

        textViewVolver.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }
}

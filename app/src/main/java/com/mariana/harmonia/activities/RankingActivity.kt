package com.mariana.harmonia.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mariana.harmonia.R
import com.mariana.harmonia.adapters.RankingReciclerViewAdapter
import com.mariana.harmonia.utils.UtilsDB
import kotlinx.coroutines.runBlocking

class RankingActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var rankingAdapter: RankingReciclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.ranking_activity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        runBlocking {
            val arrayTops = UtilsDB.getPuntuacionDesafioPorDificultad(0)

            // Ordenar por las notas totales en orden descendente
            val sortedList = arrayTops?.sortedByDescending { it["notas"]?.toInt() } ?: listOf()

            // Crear la lista final con el nombre del mapa
            val finalList = sortedList.mapIndexed { index, item ->
                item.toMutableMap().apply {
                    put("nombre", "$index".toInt() )
                }
            }

            rankingAdapter = RankingReciclerViewAdapter(finalList)
            recyclerView.adapter = rankingAdapter
        }
    }
}

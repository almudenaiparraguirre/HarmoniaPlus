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
            val arrayTops = UtilsDB.getPuntuacionDesafioGlobalPorDificultad(0)

            // Convertir y filtrar los datos según sea necesario
            val finalList = arrayTops?.map { item ->
                item.toMutableMap().mapValues { entry ->
                    when (entry.value) {
                        is Long, is Int -> (entry.value as Number).toInt()
                        is Double, is Float -> (entry.value as Number).toDouble()
                        else -> entry.value.toString()
                    }
                }
            } ?: listOf()

            rankingAdapter = RankingReciclerViewAdapter(finalList)
            recyclerView.adapter = rankingAdapter
        }
    }
}

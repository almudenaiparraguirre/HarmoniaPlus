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
    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var recyclerView3: RecyclerView
    private lateinit var rankingAdapter1: RankingReciclerViewAdapter
    private lateinit var rankingAdapter2: RankingReciclerViewAdapter
    private lateinit var rankingAdapter3: RankingReciclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.ranking_activity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView1 = findViewById(R.id.recyclerView1)
        recyclerView2 = findViewById(R.id.recyclerView2)
        recyclerView3 = findViewById(R.id.recyclerView3)

        recyclerView1.layoutManager = LinearLayoutManager(this)
        recyclerView2.layoutManager = LinearLayoutManager(this)
        recyclerView3.layoutManager = LinearLayoutManager(this)

        runBlocking {
            val arrayTops1 = UtilsDB.getPuntuacionDesafioGlobalPorDificultad(0)
            val arrayTops2 = UtilsDB.getPuntuacionDesafioGlobalPorDificultad(1)
            val arrayTops3 = UtilsDB.getPuntuacionDesafioGlobalPorDificultad(2)

            val finalList1 = arrayTops1?.map { item ->
                item.toMutableMap().mapValues { entry ->
                    when (entry.value) {
                        is Long, is Int -> (entry.value as Number).toInt()
                        is Double, is Float -> (entry.value as Number).toDouble()
                        else -> entry.value.toString()
                    }
                }
            } ?: listOf()

            val finalList2 = arrayTops2?.map { item ->
                item.toMutableMap().mapValues { entry ->
                    when (entry.value) {
                        is Long, is Int -> (entry.value as Number).toInt()
                        is Double, is Float -> (entry.value as Number).toDouble()
                        else -> entry.value.toString()
                    }
                }
            } ?: listOf()

            val finalList3 = arrayTops3?.map { item ->
                item.toMutableMap().mapValues { entry ->
                    when (entry.value) {
                        is Long, is Int -> (entry.value as Number).toInt()
                        is Double, is Float -> (entry.value as Number).toDouble()
                        else -> entry.value.toString()
                    }
                }
            } ?: listOf()

            rankingAdapter1 = RankingReciclerViewAdapter(finalList1)
            rankingAdapter2 = RankingReciclerViewAdapter(finalList2)
            rankingAdapter3 = RankingReciclerViewAdapter(finalList3)

            recyclerView1.adapter = rankingAdapter1
            recyclerView2.adapter = rankingAdapter2
            recyclerView3.adapter = rankingAdapter3
        }
    }
}

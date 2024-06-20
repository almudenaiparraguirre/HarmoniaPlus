package com.mariana.harmonia.activities

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
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

    private lateinit var btnFacil: TextView
    private lateinit var btnMedio: TextView
    private lateinit var btnDificil: TextView
    private lateinit var title1: TextView
    private lateinit var title2: TextView
    private lateinit var title3: TextView
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
        btnFacil = findViewById(R.id.btnFacil)
        btnMedio = findViewById(R.id.btnMedio)
        btnDificil = findViewById(R.id.btnDificil)

        title1 = findViewById(R.id.tvFacil)
        title2 = findViewById(R.id.tvMedio)
        title3 = findViewById(R.id.tvDificil)

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

    fun irFacil(view: View) {
        btnFacil.setBackgroundResource(R.drawable.style_menu_inferior_perfil_activo)
        btnMedio.setBackgroundResource(R.drawable.style_menu_inferior_perfil_desactivado)
        btnDificil.setBackgroundResource(R.drawable.style_menu_inferior_perfil_desactivado)
        btnFacil.setTextColor(R.color.blanco)
        btnMedio.setTextColor(R.color.negro)
        btnDificil.setTextColor(R.color.negro)
        title1.visibility = View.VISIBLE
        title2.visibility = View.GONE
        title3.visibility = View.GONE
        recyclerView1.visibility = View.VISIBLE
        recyclerView2.visibility = View.GONE
        recyclerView3.visibility = View.GONE

    }
    fun irMedio(view: View) {
        btnFacil.setBackgroundResource(R.drawable.style_menu_inferior_perfil_desactivado)
        btnMedio.setBackgroundResource(R.drawable.style_menu_inferior_perfil_activo)
        btnDificil.setBackgroundResource(R.drawable.style_menu_inferior_perfil_desactivado)
        btnFacil.setTextColor(R.color.negro)
        btnMedio.setTextColor(R.color.blanco)
        btnDificil.setTextColor(R.color.negro)
        title1.visibility = View.GONE
        title2.visibility = View.VISIBLE
        title3.visibility = View.GONE
        recyclerView1.visibility = View.GONE
        recyclerView2.visibility = View.VISIBLE
        recyclerView3.visibility = View.GONE
    }
    fun irDificil(view: View) {
        btnFacil.setBackgroundResource(R.drawable.style_menu_inferior_perfil_desactivado)
        btnMedio.setBackgroundResource(R.drawable.style_menu_inferior_perfil_desactivado)
        btnDificil.setBackgroundResource(R.drawable.style_menu_inferior_perfil_activo)
        btnFacil.setTextColor(R.color.negro)
        btnMedio.setTextColor(R.color.negro)
        btnDificil.setTextColor(R.color.blanco)
        title1.visibility = View.GONE
        title2.visibility = View.GONE
        title3.visibility = View.VISIBLE
        recyclerView1.visibility = View.GONE
        recyclerView2.visibility = View.GONE
        recyclerView3.visibility = View.VISIBLE
    }
}

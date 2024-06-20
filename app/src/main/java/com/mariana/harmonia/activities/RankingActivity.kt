package com.mariana.harmonia.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mariana.harmonia.R
import com.mariana.harmonia.activities.otros.RankingReciclerViewAdapter
import com.mariana.harmonia.utils.UtilsDB
import kotlinx.coroutines.runBlocking

class RankingActivity : AppCompatActivity() {

    private lateinit var btnFacil: TextView
    private lateinit var btnMedio: TextView
    private lateinit var btnDificil: TextView
    private lateinit var title1: TextView
    private lateinit var title2: TextView
    private lateinit var title3: TextView
    private lateinit var medallaOro: ImageView
    private lateinit var medallaPlata: ImageView
    private lateinit var medallaBronce: ImageView
    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var recyclerView3: RecyclerView
    private lateinit var rankingAdapter1: RankingReciclerViewAdapter
    private lateinit var rankingAdapter2: RankingReciclerViewAdapter
    private lateinit var rankingAdapter3: RankingReciclerViewAdapter
    private lateinit var recyclerViewTamano: Map<String, Int>

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

        medallaOro = findViewById(R.id.medallaOro)
        medallaPlata = findViewById(R.id.medallaPlata)
        medallaBronce = findViewById(R.id.medallaBronce)

        recyclerView1 = findViewById(R.id.recyclerView1)
        recyclerView2 = findViewById(R.id.recyclerView2)
        recyclerView3 = findViewById(R.id.recyclerView3)

        recyclerView1.layoutManager = LinearLayoutManager(this)
        recyclerView2.layoutManager = LinearLayoutManager(this)
        recyclerView3.layoutManager = LinearLayoutManager(this)


        runBlocking {
            val arrayTops1sinOrdenar = UtilsDB.getPuntuacionDesafioGlobalPorDificultad(0)
            val arrayTops2sinOrdenar = UtilsDB.getPuntuacionDesafioGlobalPorDificultad(1)
            val arrayTops3sinOrdenar = UtilsDB.getPuntuacionDesafioGlobalPorDificultad(2)

            val comparador = compareByDescending<Map<String, Any>> { it["notas"] as Long }
                .thenBy { it["tiempo"] as Long }

            val arrayTops1 = arrayTops1sinOrdenar!!.sortedWith(comparador)
            val arrayTops2 = arrayTops2sinOrdenar!!.sortedWith(comparador)
            val arrayTops3 = arrayTops3sinOrdenar!!.sortedWith(comparador)

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

        recyclerViewTamano = mapOf(
            "0" to (recyclerView1.adapter?.itemCount ?: 0).toInt(),
            "1" to (recyclerView2.adapter?.itemCount ?: 0).toInt(),
            "2" to (recyclerView3.adapter?.itemCount ?: 0).toInt()
        )
        actualizarMedallas(0)

    }
    fun actualizarMedallas(dificultad:Int){
        var tamano = recyclerViewTamano.get(dificultad.toString())
        if(tamano==0){
            medallaOro.alpha = 0f
            medallaPlata.alpha = 0f
            medallaBronce.alpha = 0f
        }else if(tamano==1){
            medallaOro.alpha = 1f
            medallaPlata.alpha = 0f
            medallaBronce.alpha = 0f
        }else if(tamano==2){
            medallaOro.alpha = 1f
            medallaPlata.alpha = 1f
            medallaBronce.alpha = 0f
        }else{
            medallaOro.alpha = 1f
            medallaPlata.alpha = 1f
            medallaBronce.alpha = 1f
        }
    }
    fun irFacil(view: View) {
        actualizarMedallas(0)
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
        actualizarMedallas(1)
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
        actualizarMedallas(2)
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

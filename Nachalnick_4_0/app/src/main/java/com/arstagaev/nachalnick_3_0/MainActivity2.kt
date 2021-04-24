package com.arstagaev.nachalnick_3_0

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arstagaev.nachalnick_3_0.model.FactorySector

class MainActivity2 : AppCompatActivity(), FactoryAdapter.OnItemClickListener {

    private val movieList = ArrayList<FactorySector>()
    private lateinit var moviesAdapter: FactoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        title = "Список участков"

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        moviesAdapter = FactoryAdapter(movieList,this)

        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = moviesAdapter
        prepareMovieData()









    }


    private fun prepareMovieData() {
        var movie = FactorySector("Участок №1",40,80)
        movieList.add(movie)
        movie = FactorySector("Участок №2",90,80)
        movieList.add(movie)
        movie = FactorySector("Участок №3",34,95)
        movieList.add(movie)
        movie = FactorySector("Участок №4",10,80)
        movieList.add(movie)
        movie = FactorySector("Участок №5",2,95)
        movieList.add(movie)
        movie = FactorySector("Участок №6",100,80)
        movieList.add(movie)
        movie = FactorySector("Участок №7",65,95)
        movieList.add(movie)


        moviesAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {
        Log.d("ccc","click!!!!!!")
        val intent = Intent(this,MainActivity3::class.java)
        startActivity(intent)
    }
}
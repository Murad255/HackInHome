package com.arstagaev.nachalnick_3_0

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.arstagaev.nachalnick_3_0.model.FactorySector
import params.com.stepprogressview.StepProgressView

internal class FactoryAdapter(private var moviesList: List<FactorySector>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<FactoryAdapter.MyViewHolder>() {


//    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//
//        var title: TextView = view.findViewById(R.id.name)
//        var progressBar: StepProgressView = view.findViewById(R.id.progress_bar)
//
//    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_of_production_site, parent, false)

        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = moviesList[position]
        holder.title.text = movie.name
        holder.progressBar.currentProgress = movie.progress
        holder.progressBar.setOnClickListener {

        }

    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        var title: TextView = view.findViewById(R.id.name)
        var progressBar: StepProgressView = view.findViewById(R.id.progress_bar)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}
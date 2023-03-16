package com.example.myapplication.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ComicsClickListener
import com.example.myapplication.databinding.CardCellBinding
import com.example.myapplication.model.Comics

//CardAdapter for our comics
class CardAdapter(private val comicsList: ArrayList<Comics>, private val clickListener: ComicsClickListener)
    : RecyclerView.Adapter<CardAdapter.CardViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardCellBinding.inflate(from, parent, false)
        return CardViewHolder(binding,clickListener)
    }

    override fun getItemCount(): Int = comicsList.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindComics(comicsList[position])
    }



    inner class CardViewHolder(private val cardCellBinding: CardCellBinding, private val clickListener: ComicsClickListener) : RecyclerView.ViewHolder(cardCellBinding.root) {
        fun bindComics(comics: Comics) {
            cardCellBinding.tvComicsTitle.text = comics.title
            cardCellBinding.tvComicsType.text = comics.type
            cardCellBinding.ivCover.setBackgroundResource(comics.cover)
            cardCellBinding.cvComics.setOnClickListener { clickListener.onClick(comics) }
        }

    }
}
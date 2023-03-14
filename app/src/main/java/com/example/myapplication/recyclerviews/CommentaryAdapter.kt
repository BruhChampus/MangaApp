package com.example.myapplication.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myapplication.model.CommentaryEntity
import com.example.myapplication.databinding.CommentaryRowBinding

class CommentaryAdapter(
    private val items: ArrayList<CommentaryEntity>,
    private val updateListener: (id: Int) -> Unit,
    private val deleteListener: (id: Int) -> Unit
) :
    RecyclerView.Adapter<CommentaryAdapter.CommentaryViewHolder>() {

    inner class CommentaryViewHolder(binding: CommentaryRowBinding) : ViewHolder(binding.root) {
        val tvDate = binding.tvCommentaryDate
        val tvCommentary = binding.tvCommentary
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentaryViewHolder {
        return CommentaryViewHolder(
            CommentaryRowBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CommentaryViewHolder, position: Int) {
        val commentaries = items[position]
        holder.tvDate.text = commentaries.date
        holder.tvCommentary.text = commentaries.commentary

        holder.ivEdit.setOnClickListener {updateListener.invoke(commentaries.commentaryId)}
        holder.ivDelete.setOnClickListener {deleteListener.invoke(commentaries.commentaryId)}
    }
}
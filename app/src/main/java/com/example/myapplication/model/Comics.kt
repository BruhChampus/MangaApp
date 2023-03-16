package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.R


@Entity(tableName = "comics-table")
data class Comics(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val type:String = "Manga",
    val title:String = "??",
    val cover:Int = R.drawable.cover_default,
    val backgroundCover:Int = R.drawable.cover_default,
    val description:String = "No description yet"
)

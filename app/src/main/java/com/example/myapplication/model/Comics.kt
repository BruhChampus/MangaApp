package com.example.myapplication.model

import android.content.ContentResolver
import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.R

val defaultCoverUri: Uri =Uri.parse("android.resource://com.example.myapplication.model/${R.drawable.cover_default}")

@Entity(tableName = "comics-table")
data class Comics(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val type:String = "Manga",
    val title:String = "??",
    val cover:String? = defaultCoverUri.toString(),
    val backgroundCover:String? = defaultCoverUri.toString(),
    val description:String = "No description yet"
)

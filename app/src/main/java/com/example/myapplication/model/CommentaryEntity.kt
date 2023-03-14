package com.example.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

//
//@Entity(
//    foreignKeys = [ForeignKey(
//        entity = Comics::class,
//        parentColumns = arrayOf("id"),
//        childColumns = arrayOf("titleIdInWhich"),
//        onDelete = ForeignKey.CASCADE
//    )]
//)

@Entity(tableName = "commentaries-table")
data class CommentaryEntity(
    @PrimaryKey(autoGenerate = true)
    val commentaryId: Int = 0,
    val titleIdInWhich: Int = 0,
    val commentary: String = "",
    val date: String = ""
)

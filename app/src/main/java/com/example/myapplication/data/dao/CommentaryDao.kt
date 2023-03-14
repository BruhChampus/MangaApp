package com.example.myapplication.data.dao

import androidx.room.*
import com.example.myapplication.model.CommentaryEntity


@Dao
interface CommentaryDao {

    @Insert
    suspend fun insert(commentaryEntity: CommentaryEntity)

    @Update
    suspend fun update(commentaryEntity: CommentaryEntity)

    @Delete
    suspend fun delete(commentaryEntity: CommentaryEntity)

    @Query("SELECT *FROM 'commentaries-table' WHERE titleIdInWhich =:id AND commentaryId = :commentaryId")
    fun fetchCommentaryById(id: Int, commentaryId:Int): kotlinx.coroutines.flow.Flow<CommentaryEntity>

    @Query("SELECT *FROM 'commentaries-table' WHERE titleIdInWhich =:id")
    fun fetchAllCommentaries(id: Int): kotlinx.coroutines.flow.Flow<List<CommentaryEntity>>
}
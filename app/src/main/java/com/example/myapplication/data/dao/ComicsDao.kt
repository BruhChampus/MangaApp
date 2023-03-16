package com.example.myapplication.data.dao

import androidx.room.*
import com.example.myapplication.model.Comics


@Dao
interface ComicsDao {

    @Insert
    suspend fun insert(comics: Comics)

    @Update
    suspend fun update(comics: Comics)

    @Delete
    suspend fun delete(comics: Comics)

    @Query("SELECT *FROM 'comics-table' WHERE id =:id")
    fun fetchComicsById(id: Int): kotlinx.coroutines.flow.Flow<Comics>

    @Query("SELECT *FROM 'comics-table'")
    fun fetchAllComics(): kotlinx.coroutines.flow.Flow<List<Comics>>
}

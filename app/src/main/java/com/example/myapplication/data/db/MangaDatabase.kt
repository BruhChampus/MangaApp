package com.example.myapplication.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.data.dao.ComicsDao
import com.example.myapplication.data.dao.CommentaryDao
import com.example.myapplication.model.Comics
import com.example.myapplication.model.CommentaryEntity


@Database(entities = [Comics::class, CommentaryEntity::class], version = 1)
abstract class MangaDatabase : RoomDatabase() {

    abstract fun CommentaryDao(): CommentaryDao
    abstract fun ComicsDao(): ComicsDao

    companion object {
        @Volatile
        private var INSTANCE: MangaDatabase? = null

        fun getInstance(context: Context): MangaDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MangaDatabase::class.java,
                        "manga_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
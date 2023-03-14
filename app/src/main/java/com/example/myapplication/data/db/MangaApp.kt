package com.example.myapplication.data.db

import android.app.Application

class MangaApp: Application() {
    val db by lazy{
        MangaDatabase.getInstance(this)
    }
}
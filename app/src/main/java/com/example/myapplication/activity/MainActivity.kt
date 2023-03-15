package com.example.myapplication.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.NightMode
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.ComicsClickListener
import com.example.myapplication.NightModeSetUp
import com.example.myapplication.R
import com.example.myapplication.data.Constants
import com.example.myapplication.data.dao.ComicsDao
import com.example.myapplication.data.dao.CommentaryDao
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.Comics
import com.example.myapplication.model.CommentaryEntity
import com.example.myapplication.recyclerviews.CardAdapter
import com.example.myapplication.recyclerviews.CommentaryAdapter
import java.util.ArrayList

class MainActivity : AppCompatActivity(), ComicsClickListener, NightModeSetUp {

    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    var nightMode = AppCompatDelegate.getDefaultNightMode()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setting how our recycler view will display our comicses cells
        binding.rvComicsList.apply {
            layoutManager = GridLayoutManager(applicationContext, 2)
            adapter = CardAdapter(Constants.getComics(), this@MainActivity)

        }
        toggle = ActionBarDrawerToggle(
            this,
            binding.dlMainDrawerLayout,
            R.string.open_nav_bar,
            R.string.close_nav_bar
        )

        //Connecting toggle with drawer layout
        binding.dlMainDrawerLayout.addDrawerListener(toggle)
        //telling toggle that it ready to be used
        toggle.syncState()

        //setDisplayHomeAsUpEnabled - transforms our burger icon into backArrow cliking on which we can close our slideable menu
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.nvNavView.setNavigationItemSelectedListener {
            onOptionsItemSelected(it)
        }


    }

    private fun setupListOfComicsIntoRecyclerView(
        comicsList: ArrayList<Comics>,
        comicsDao: ComicsDao
    ) {
        if (comicsList.isNotEmpty()) {
            val comicsAdapter = CardAdapter(
                comicsList,this)
            comicsList.reverse()
        }
        else{
            binding.rvComicsList.visibility = View.GONE
        }

    }



    //inflating our menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tool_bar_menu_main_activity, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //if our burger button tapped return true
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        when (item.itemId) {
            R.id.mi_item1 -> Toast.makeText(
                applicationContext,
                "Clicked item 1",
                Toast.LENGTH_SHORT
            ).show()
            R.id.mi_item2 -> Toast.makeText(
                applicationContext,
                "Clicked item 2",
                Toast.LENGTH_SHORT
            ).show()
            R.id.mi_item3 -> Toast.makeText(
                applicationContext,
                "Clicked item 3",
                Toast.LENGTH_SHORT
            ).show()

            R.id.mi_add_manga -> startActivity(Intent(applicationContext, AddManga::class.java))

            R.id.mi_dark_mode -> {
                setUpNightMode()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    //Setting up nightMode
    override fun setUpNightMode() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Toast.makeText(
                applicationContext,
                "Night mode off",
                Toast.LENGTH_SHORT
            ).show()

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Toast.makeText(
                applicationContext,
                "Night mode on",
                Toast.LENGTH_SHORT
            ).show()

        }
    }


    //Function needed to tell us what will happen if we click on comics card
    override fun onClick(comics: Comics) {
        val intent = Intent(applicationContext, DetailActivity::class.java)
        intent.putExtra(Constants.COMICS_ID, comics.id)
        startActivity(intent)
    }
}



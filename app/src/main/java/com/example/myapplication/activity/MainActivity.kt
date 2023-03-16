package com.example.myapplication.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.ComicsClickListener
import com.example.myapplication.NightModeSetUp
import com.example.myapplication.R
import com.example.myapplication.data.Constants
import com.example.myapplication.data.dao.ComicsDao
import com.example.myapplication.data.db.MangaDatabase
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.Comics
import com.example.myapplication.recyclerviews.CardAdapter
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), ComicsClickListener, NightModeSetUp {

    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setting how our recycler view will display our comics cells
        val comicsDao = MangaDatabase.getInstance(this).ComicsDao()

        binding.rvComicsList.apply {
            lifecycleScope.launch {
                comicsDao.fetchAllComics().collect() {
                    val list = ArrayList(it)
                    setupListOfComicsIntoRecyclerView(list, comicsDao)
                }
            }
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

        //setDisplayHomeAsUpEnabled - transforms our burger icon into backArrow clicking on which we can close our slideable menu
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.nvNavView.setNavigationItemSelectedListener {
            onOptionsItemSelected(it)
        }

    }



    //Method that setups how elements will be displayed in RecyclerView
    private fun setupListOfComicsIntoRecyclerView(
        comicsList: ArrayList<Comics>,
        comicsDao: ComicsDao
    ) {
        if (comicsList.isNotEmpty()) {
            val comicsAdapter = CardAdapter(
                comicsList,this)
            binding.rvComicsList.layoutManager = GridLayoutManager(applicationContext, 2)
            binding.rvComicsList.adapter = comicsAdapter
            binding.rvComicsList.visibility = View.VISIBLE
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


    //Describing what happens when we touch icon
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


    //Describes what will happen if we click on comics card.
    //There we just open Activity Detail and passing id of card which we clicked, further this id will help us get info from DataBase for specific comics
    override fun onClick(comics: Comics) {
        val intent = Intent(applicationContext, DetailActivity::class.java)
        intent.putExtra(Constants.COMICS_ID, comics.id)
        startActivity(intent)
    }
}



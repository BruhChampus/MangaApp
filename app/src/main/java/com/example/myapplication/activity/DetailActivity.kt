package com.example.myapplication.activity

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapplication.NightModeSetUp
import com.example.myapplication.data.Constants
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDetailBinding
import com.example.myapplication.fragments.CommentariesFragment
import com.example.myapplication.fragments.InformationFragment
import com.example.myapplication.model.Comics

class DetailActivity : AppCompatActivity(), NightModeSetUp {

    private lateinit var binding: ActivityDetailBinding
    private val comicsList = Constants.getComics()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val comicsId = intent.getIntExtra(Constants.COMICS_ID, -1)
        val comics = getComicsFromId(comicsId)



        if (comics != null) {
            binding.ivCover.setImageResource(comics.cover)
            binding.ivBackgroundCover.setImageResource(comics.backgroundCover)
            binding.tvDetailsComicsTitle.text = comics.title
            binding.tvDetailsComicsTitleTranslated.text = comics.title
        }


        val fragmentInfo = InformationFragment.newInstance(comicsId)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, fragmentInfo).commit()
        }

        //Setting back arrow for our app bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.rbInfo.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl_fragment, fragmentInfo).commit()
            }
        }


        val fragmentCommentaries = CommentariesFragment.newInstance(comicsId)
        binding.rbCommentaries.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl_fragment, fragmentCommentaries).commit()
            }
        }
    }



    //Method for back arrow. When pressed - return back on our stack
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    //Inflating our toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tool_bar_menu_detail_activity, menu)
        return true
    }

    //Describing what will happen when we click on certain button on app bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_dark_mode -> setUpNightMode()

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




    //Getting comics by id
    private fun getComicsFromId(id: Int): Comics? {
        for (comics in comicsList) {
            if (comics.id == id) {
                return comics
            }
        }
        return null
    }


}
package com.example.myapplication.activity

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.NightModeSetUp
import com.example.myapplication.data.Constants
import com.example.myapplication.R
import com.example.myapplication.data.dao.ComicsDao
import com.example.myapplication.data.dao.CommentaryDao
import com.example.myapplication.data.db.MangaDatabase
import com.example.myapplication.databinding.ActivityDetailBinding
import com.example.myapplication.fragments.CommentariesFragment
import com.example.myapplication.fragments.InformationFragment
import com.example.myapplication.model.Comics
import com.example.myapplication.model.CommentaryEntity
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity(), NightModeSetUp {

    private lateinit var binding: ActivityDetailBinding
   // private val comicsList = Constants.getComics()
    private val comicsDao = MangaDatabase.getInstance(this).ComicsDao()
    private var comicsId:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

         comicsId = intent.getIntExtra(Constants.COMICS_ID, -1)

        //Fetching comics from DataBase by id which we received from MainActivity
        lifecycleScope.launch {
            comicsDao.fetchComicsById(comicsId).collect() {
                if(it != null) {
                    binding.ivCover.setImageResource(it.cover)
                    binding.ivBackgroundCover.setImageResource(it.backgroundCover)
                    binding.tvDetailsComicsTitle.text = it.title
                    binding.tvDetailsComicsTitleTranslated.text = it.title
                }
            }
        }


        //Creating Information fragment and setting it first when DetailActivity created
        val fragmentInfo = InformationFragment.newInstance(comicsId)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, fragmentInfo).commit()
        }

        //Setting back arrow for our app bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        //Setting on click listener for radio button. When clicking on button information fragment substitutes
        binding.rbInfo.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl_fragment, fragmentInfo).commit()
            }
        }


        //Creating Commentaries fragment and setting on click listener for radio button. When clicking on button commentaries fragment substitutes
        val fragmentCommentaries = CommentariesFragment.newInstance(comicsId)
        binding.rbCommentaries.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl_fragment, fragmentCommentaries).commit()
            }
        }
    }


    //Method for deleting comics from database
    private fun deleteRecordAlertDialog(comicsId: Int, comicsDao: ComicsDao) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                comicsDao.delete(Comics(comicsId))
                Toast.makeText(this@DetailActivity, "Comics successfully deleted", Toast.LENGTH_SHORT)
                    .show()
                onBackPressedDispatcher.onBackPressed()
            }
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
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

    //Describing what will happen when we click on certain button on toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_dark_mode -> setUpNightMode()
            R.id.mi_delete ->  deleteRecordAlertDialog(comicsId, comicsDao)

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




//    //Getting comics by id
//    private fun getComicsFromId(id: Int): Comics? {
//        for (comics in comicsList) {
//            if (comics.id == id) {
//                return comics
//            }
//        }
//        return null
//    }


}
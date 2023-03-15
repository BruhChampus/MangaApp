package com.example.myapplication.activity

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.NightModeSetUp
import com.example.myapplication.R
import com.example.myapplication.data.dao.ComicsDao
import com.example.myapplication.data.dao.CommentaryDao
import com.example.myapplication.data.db.MangaDatabase
import com.example.myapplication.databinding.ActivityAddMangaBinding
import com.example.myapplication.model.Comics
import com.example.myapplication.model.CommentaryEntity
import kotlinx.coroutines.launch

class AddManga : AppCompatActivity(), NightModeSetUp {
    private lateinit var binding: ActivityAddMangaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMangaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        val comicsDao = MangaDatabase.getInstance(this).ComicsDao()
        binding.ibAddCoverImage.setOnClickListener {
            requestStoragePermission()
        }

        binding.btnSave.setOnClickListener {
            saveComics(comicsDao, 1)
        }
    }


    val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val imageBackground: ImageView = binding.ibAddCoverImage
                imageBackground.setImageURI(result.data?.data)
            }
        }

    val requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    val pickIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryLauncher.launch(pickIntent)


                } else {
                    if (permissionName == android.Manifest.permission.READ_EXTERNAL_STORAGE) {
                        Toast.makeText(this, "You've denied the permission", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Manga App").setMessage("Manga App needs access to your storage")
                .setPositiveButton("Cancel") { dialog, _ -> dialog.dismiss() }
            builder.create().show()
        } else {
            requestPermission.launch(
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )

        }
    }

    private fun saveComics(comicsDao: ComicsDao, comicsId: Int) {
        val comicsType = binding.spComicsTypeEdit.selectedItem.toString()
        val comicsTitle = binding.etMangaTitleEdit.text.toString()
        val comicsCover = binding.ibAddCoverImage
        val comicsBackgroundCover = binding.ibAddBackgroundCoverImage
        val comicsDescription = binding.etTitleDescriptionEdit.text

        if (comicsTitle != null) {
            if (comicsTitle.isNotEmpty()) {
                lifecycleScope.launch {
                    comicsDao.insert(
                        Comics(
                            type = comicsType,
                            title = comicsTitle,
                            description = comicsDescription.toString()
                        )
                    )

                }
            } else {
                Toast.makeText(this, "Comics title cannot be blank", Toast.LENGTH_SHORT)
                    .show()
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
}
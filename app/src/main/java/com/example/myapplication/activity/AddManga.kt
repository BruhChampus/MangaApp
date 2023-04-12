package com.example.myapplication.activity

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
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
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddManga : AppCompatActivity(), NightModeSetUp {
    private lateinit var binding: ActivityAddMangaBinding
    private var saveCoverImageToInternalStorage: Uri? = null
    private var saveBackgroundCoverImageToInternalStorage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMangaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Setting back arrow for toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        //Getting instance of MangaDatabase and calling its abstract function that returns us comicsDao
        val comicsDao = MangaDatabase.getInstance(this).ComicsDao()


        binding.ibAddCoverImage.setOnClickListener {
            chooseCoverPhotoFromGallery()
        }
        binding.ibAddBackgroundCoverImage.setOnClickListener {
            chooseBackgroundCoverPhotoFromGallery()
        }
        binding.btnSave.setOnClickListener {
            saveComics(comicsDao)
        }
        binding.btnCancel.setOnClickListener {
            clearFields()
        }


    }






    //Method that is responsible for inserting data in DataBase
    private fun saveComics(comicsDao: ComicsDao) {
        val comicsType = binding.spComicsTypeEdit.selectedItem.toString()
        val comicsTitle = binding.etMangaTitleEdit.text.toString()
        val comicsCover: String = saveCoverImageToInternalStorage.toString()
         val comicsBackgroundCover: String = saveBackgroundCoverImageToInternalStorage.toString()

        val comicsDescription = binding.etTitleDescriptionEdit.text

        if (comicsTitle.isNotEmpty()) {
            lifecycleScope.launch {
                comicsDao.insert(
                    Comics(
                        type = comicsType,
                        title = comicsTitle,
                        description = comicsDescription.toString(),
                        cover = comicsCover,
                        backgroundCover = comicsBackgroundCover
                    )
                )
                Toast.makeText(this@AddManga, "Comics successfully created", Toast.LENGTH_SHORT)
                    .show()
                onBackPressedDispatcher.onBackPressed()

            }
        } else {
            Toast.makeText(this, "Comics title cannot be blank", Toast.LENGTH_SHORT)
                .show()
        }
    }

    //Method that clears fields in AddManga activity
    private fun clearFields(){
        binding.etMangaTitleEdit.text?.clear()
        binding.etTitleDescriptionEdit.text?.clear()
        binding.etAuthorEdit.text?.clear()
        binding.etChapterUploaded.text?.clear()
        binding.ibAddCoverImage.setImageResource(R.drawable.cover_default)
        binding.ibAddBackgroundCoverImage.setImageResource(R.drawable.cover_default)
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


    private val openGalleryForCover =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val contentURI = result.data!!.data
                try {
                    val selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                       this.contentResolver,
                        contentURI
                    )
                    saveCoverImageToInternalStorage = saveImageToInternalStorage(selectedImageBitmap)
                    Log.e("Saved image:", "Path: $saveCoverImageToInternalStorage")
                    binding.ibAddCoverImage.setImageBitmap(selectedImageBitmap)
                } catch (e: IOException) {
                    e.stackTrace
                    Toast.makeText(
                        this,
                        "Failed to load image from gallery!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    private fun chooseCoverPhotoFromGallery() {

        Dexter.withContext(this).withPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report!!.areAllPermissionsGranted()) {
                    val galleryIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryForCover.launch(galleryIntent)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest?>?,
                token: PermissionToken?
            ) {
                showRationalDialogForPermissions()
            }
        }).onSameThread().check()
    }


    private val openGalleryForBackgroundCover =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val contentURI = result.data!!.data
                try {
                    val selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                        this.contentResolver,
                        contentURI
                    )
                    saveBackgroundCoverImageToInternalStorage = saveImageToInternalStorage(selectedImageBitmap)
                    Log.e("Saved image:", "Path: $saveBackgroundCoverImageToInternalStorage")
                    binding.ibAddBackgroundCoverImage.setImageBitmap(selectedImageBitmap)
                } catch (e: IOException) {
                    e.stackTrace
                    Toast.makeText(
                        this,
                        "Failed to load image from gallery!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    private fun chooseBackgroundCoverPhotoFromGallery() {

        Dexter.withContext(this).withPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {

            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                 if (report!!.areAllPermissionsGranted()) {
                     val galleryIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryForBackgroundCover.launch(galleryIntent)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest?>?,
                token: PermissionToken?
            ) {
                showRationalDialogForPermissions()
            }
        }).onSameThread().check()
    }




    private fun showRationalDialogForPermissions() {

        android.app.AlertDialog.Builder(this).setMessage(
            "It looks like you've turned off permissions required for this feature. " +
                    "It can be enabled under the application settings"
        ).setPositiveButton("GO TO SETTINGS")
        { _, _ ->
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
        }.show()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    companion object{
        private const val IMAGE_DIRECTORY = "MangaAppImageDirectory"
    }

}
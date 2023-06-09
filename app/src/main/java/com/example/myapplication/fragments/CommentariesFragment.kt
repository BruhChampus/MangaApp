package com.example.myapplication.fragments

import android.app.Dialog
import android.content.Context
import android.hardware.input.InputManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.R
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.Constants
import com.example.myapplication.data.dao.CommentaryDao
import com.example.myapplication.data.db.MangaDatabase
import com.example.myapplication.databinding.DialogEditBinding
import com.example.myapplication.databinding.FragmentCommentariesBinding
import com.example.myapplication.model.Comics
import com.example.myapplication.model.CommentaryEntity
import com.example.myapplication.recyclerviews.CommentaryAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


private val COMICS_ID = Constants.COMICS_ID
//private val comicsList = Constants.getComics()


class CommentariesFragment : Fragment() {

    private var comicsId: Int = 0
    private lateinit var binding: FragmentCommentariesBinding
    lateinit var comics: Comics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            comicsId = it.getInt(COMICS_ID)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCommentariesBinding.inflate(layoutInflater)

        val commentaryDao = MangaDatabase.getInstance(this.requireContext()).CommentaryDao()
        val comicsDao = MangaDatabase.getInstance(requireContext()).ComicsDao()

        //Fetching comics from DataBase by Id that we received from MainActivity
        lifecycleScope.launch {
            comicsDao.fetchComicsById(comicsId).collect() {
                if (it != null) {
                    comics = it
                }
            }
        }

        binding.btnSubmitComment.setOnClickListener {
            addCommentary(commentaryDao, comicsId)
        }


        //Fetching all commentaries nder this title
        lifecycleScope.launch {
            commentaryDao.fetchAllCommentaries(comicsId).collect() {
                val list = kotlin.collections.ArrayList(it)
                setupListOfCommentariesIntoRecyclerView(list, commentaryDao)
            }
        }

        return binding.root
    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }


    private fun getCommentaryDate(): String {
        val c = Calendar.getInstance()
        val dateTime = c.time
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)
        return date
    }


    //Inserting commentary in DataBase
    private fun addCommentary(commentaryDao: CommentaryDao, comicsId: Int) {
        val commentary = binding.etComment.text.toString()

        if (commentary.isNotEmpty()) {
            lifecycleScope.launch {
                commentaryDao.insert(
                    CommentaryEntity(
                        titleIdInWhich = comicsId,
                        commentary = commentary,
                        date = getCommentaryDate()
                    )
                )

                binding.etComment.text?.clear()
                binding.etComment.clearFocus()
                hideKeyboard()
            }
        } else {
            Toast.makeText(activity, "Comment cannot be blank", Toast.LENGTH_SHORT)
                .show()
        }
    }


    //Setting up how recycler view display commentaries
    private fun setupListOfCommentariesIntoRecyclerView(
        commentaryList: ArrayList<CommentaryEntity>,
        commentaryDao: CommentaryDao
    ) {
        if (commentaryList.isNotEmpty()) {
            val commentaryAdapter = CommentaryAdapter(
                commentaryList,
                { updateId ->
                    updateRecordDialog(updateId, commentaryDao)
                },
                { deleteId ->
                    deleteRecordAlertDialog(deleteId, commentaryDao)
                },
            )
            commentaryList.reverse()
            binding.rvComments.layoutManager = LinearLayoutManager(activity)
            binding.rvComments.adapter = commentaryAdapter
            binding.rvComments.visibility = View.VISIBLE
        } else {
            binding.rvComments.visibility = View.GONE
        }

    }


    //Updating commentary in DataBase
    /**
     * Getting commentary by it's id. Creating dialog in which we will edit our commentary. Binding edit text field in dialog
     * Prescribing what will happen when you click on the buttons
     */
    private fun updateRecordDialog(id: Int, commentaryDao: CommentaryDao) {

        val updateDialog = Dialog(requireContext(), R.style.ThemeOverlay_AppCompat_Dialog_Alert)
        val binding = DialogEditBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        lifecycleScope.launch {
            commentaryDao.fetchCommentaryById(comics.id, id).collect {
                if (it != null) {
                    binding.etEditComment.setText(it.commentary)
                }
            }
        }

        binding.tvSave.setOnClickListener {
            val commentary = binding.etEditComment.text.toString()
            if (commentary.isNotEmpty()) {
                lifecycleScope.launch {
                    commentaryDao.update(
                        CommentaryEntity(
                            commentaryId = id,
                            titleIdInWhich = comics.id,
                            commentary = commentary,
                            date = getCommentaryDate() + " (Edited)"
                        )
                    )
                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Comment cannot be blank",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }
        updateDialog.show()
    }



    //Deleting commentary from DataBase
    private fun deleteRecordAlertDialog(id: Int, commentaryDao: CommentaryDao) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Record")
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                commentaryDao.delete(CommentaryEntity(id))
                Toast.makeText(requireContext(), "Comment successfully deleted", Toast.LENGTH_SHORT)
                    .show()
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


    companion object {
        @JvmStatic
        fun newInstance(comicsId: Int) =
            CommentariesFragment().apply {
                arguments = Bundle().apply {
                    putInt(COMICS_ID, comicsId)
                }
            }
    }
}
package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.Constants
import com.example.myapplication.data.db.MangaDatabase
import com.example.myapplication.databinding.FragmentInformationBinding
import kotlinx.coroutines.launch


private const val COMICS_ID = Constants.COMICS_ID
//private val comicsList = Constants.getComics()




class InformationFragment : Fragment() {

    private var comicsId: Int = 0
    private lateinit var binding: FragmentInformationBinding





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

        binding = FragmentInformationBinding.inflate(layoutInflater)

        val description = binding.tvComicsDescription
        val tvShow = binding.tvShow
        val tvHide = binding.tvHide
        val comicsDao = MangaDatabase.getInstance(requireContext()).ComicsDao()


        //Fetching comics by id and binding it's description
        lifecycleScope.launch {
            comicsDao.fetchComicsById(comicsId).collect() {
                if(it != null) {
                    description.text = it.description

                    //Checking if comics description length > 6 words and if so, showing button "Show" and setting onClick listeners
                    if (description.text.length > 6) {
                        tvShow.visibility = View.VISIBLE
                        tvShow.setOnClickListener { showOnClick(tvShow, tvHide) }
                        tvHide.setOnClickListener { hideOnClick(tvShow, tvHide) }
                    }
                }
            }
        }


        return binding.root
    }


    //Method for button "Show"
    private fun showOnClick(show: View, hide: View) {
        show.setOnClickListener {
            binding.tvComicsDescription.maxLines = 100
            show.visibility = View.GONE
            hide.visibility = View.VISIBLE
        }
    }

    //Method for button "Hide"
    private fun hideOnClick(show: View, hide: View) {
        hide.setOnClickListener {
            binding.tvComicsDescription.maxLines = 4
            show.visibility = View.VISIBLE
            hide.visibility = View.GONE

        }
    }





    companion object {
        @JvmStatic
        fun newInstance(comicsId: Int) =
            InformationFragment().apply {
                arguments = Bundle().apply {
                    putInt(COMICS_ID, comicsId)
                }
            }
    }
}
package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.data.Constants
import com.example.myapplication.databinding.FragmentInformationBinding


private const val COMICS_ID = Constants.COMICS_ID
private val comicsList = Constants.getComics()


//Describes infromation fragment

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

       description.text =  bindFragmentDetails(comicsId)

        //Checking if comics description length > 8 words and if so, showing button "Show" and setting onClick listeners
        if (comicsList[comicsId].description.length > 8) {
            tvShow.visibility = View.VISIBLE
            tvShow.setOnClickListener { showOnClick(tvShow, tvHide) }
            tvHide.setOnClickListener { hideOnClick(tvShow, tvHide) }
        }
        return binding.root
    }




    //Methods for button "Show"
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


    //Getting comics description via it's id in comicsList
    private fun bindFragmentDetails(comicsId: Int): String {
        return comicsList[comicsId].description
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
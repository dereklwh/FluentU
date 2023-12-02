package com.example.group26.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.group26.FlashCardDeckActivity
import com.example.group26.ProgressActivity
import com.example.group26.QuizActivity
import com.example.group26.R
import com.example.group26.ResourcesActivity
import com.example.group26.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageButtons: Array<ImageButton>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupButtons()
        return binding.root
    }

    fun setupButtons(){
        imageButtons = arrayOf(
            binding.root.findViewById(R.id.quizButton),
            binding.root.findViewById(R.id.flashCardButton),
            binding.root.findViewById(R.id.progressButton),
            binding.root.findViewById(R.id.resourcesButton)
        )

        for(btn in imageButtons){
            btn.setOnClickListener(){
                val intent: Intent
                if(btn == imageButtons[0]){
                    intent = Intent(requireContext(), QuizActivity::class.java)
                }
                else if(btn == imageButtons[1]){
                    intent = Intent(requireContext(), FlashCardDeckActivity::class.java)
                }
                else if(btn == imageButtons[2]){
                    intent = Intent(requireContext(), ProgressActivity::class.java)
                }
                else{
                    intent = Intent(requireContext(), ResourcesActivity::class.java)
                }
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
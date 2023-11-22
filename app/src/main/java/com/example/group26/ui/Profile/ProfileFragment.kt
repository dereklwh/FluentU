package com.example.group26.ui.Profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.group26.R
import com.example.group26.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

	    val closeButton = binding.closeButton
	    closeButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val saveButton = binding.saveButton
        saveButton.setOnClickListener {
            saveProfileData(profileViewModel)
        }

        val savedProfileData = profileViewModel.getSavedProfileData()
        binding.editName.setText(savedProfileData.first)
        binding.editEmail.setText(savedProfileData.second)
        binding.editPhone.setText(savedProfileData.third)

        val savedGender = profileViewModel.getSavedGender()
        if (savedGender == "Male") {
            binding.maleButton.isChecked = true
        }
        else if (savedGender == "Female") {
            binding.femaleButton.isChecked = true
        }

        return binding.root
    }

    private fun saveProfileData(profileViewModel: ProfileViewModel) {
        val name = binding.editName.text.toString()
        val email = binding.editEmail.text.toString()
        val phone = binding.editPhone.text.toString()

        val radioGroup = binding.radioGroup
        val selectedGenderId = radioGroup.checkedRadioButtonId
        val gender = when (selectedGenderId) {
            R.id.maleButton -> "Male"
            R.id.femaleButton -> "Female"
            else -> ""
        }
        profileViewModel.saveProfileData(name, email, phone, gender)
        Toast.makeText(requireContext(), "Profile saved", Toast.LENGTH_SHORT).show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
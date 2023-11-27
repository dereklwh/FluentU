package com.example.group26.ui.Profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.group26.R
import com.example.group26.databinding.FragmentProfileBinding
import java.io.File
import androidx.core.content.FileProvider


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    private lateinit var tempImgUri: Uri
    private lateinit var myViewModel: ProfileViewModel
    private lateinit var cameraResult: ActivityResultLauncher<Intent>
    private val tempImgFileName = "xd_temp_img.jpg"
    private val PREFS_NAME = "MyPrefs"
    private lateinit var prefs: SharedPreferences
    private val FLAG_KEY = "flag"
    private var flag = false

    private var selectedPhotoUri: Uri? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        flag = prefs.getBoolean(FLAG_KEY, false)
        val imageProfile = binding.imageProfile

        Util.checkPermissions(this)

        val tempImgFile = File(requireContext().getExternalFilesDir(null), tempImgFileName)
        tempImgUri = FileProvider.getUriForFile(requireContext(), "com.example.group26", tempImgFile)

        val changePhotoButton = binding.ChangePhotoButton
        changePhotoButton.setOnClickListener {
            showImageSourceDialog()
        }


        val closeButton = binding.closeButton
        closeButton.setOnClickListener {
            flag = false
            val prefsEditor = prefs.edit()
            prefsEditor.putBoolean(FLAG_KEY, flag)
            prefsEditor.apply()
            Toast.makeText(requireContext(), "Photo removed", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        }


        val saveButton = binding.saveButton
        saveButton.setOnClickListener {

            if (selectedPhotoUri != null) {
                val bitmap = Util.getBitmap(this, selectedPhotoUri!!)
                myViewModel.userImage.value = bitmap
            }

            saveProfileData(profileViewModel)
            requireActivity().supportFragmentManager.popBackStack()
        }
        cameraResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = Util.getBitmap(this, tempImgUri)
                myViewModel.userImage.value = bitmap
            }
        }

        myViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        myViewModel.userImage.observe(viewLifecycleOwner, { it ->
            imageProfile.setImageBitmap(it)
        })
        if (tempImgFile.exists() && flag == true) {
            flag = true
            val bitmap = Util.getBitmap(this, tempImgUri)
            imageProfile.setImageBitmap(bitmap)
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

        flag = true
        val prefsEditor = prefs.edit()
        prefsEditor.putBoolean(FLAG_KEY, flag)
        prefsEditor.apply()
        Toast.makeText(requireContext(), "Profile saved", Toast.LENGTH_SHORT).show()
    }
    
    // added getContent, showImageSourceDialog, onCameraSelected, onGallerySelected
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
            uri: Uri? -> Log.d("UserProfile", "Gallery selected")
        if (uri != null) {
            selectedPhotoUri = uri
            val bitmap = Util.getBitmap(this, uri)
            myViewModel.userImage.value = bitmap
        }
    }

    private fun showImageSourceDialog() {
        val dialogFragment = ImageSourceDialog(this)
        dialogFragment.show(requireActivity().supportFragmentManager, "ImageSourceDialog")
    }

    fun onCameraSelected() {
        if (tempImgUri != null && cameraResult != null) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempImgUri)
            cameraResult.launch(intent)
        }
    }

    fun onGallerySelected() {
        getContent.launch("image/*")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
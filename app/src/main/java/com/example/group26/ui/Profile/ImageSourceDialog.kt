package com.example.group26.ui.Profile

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ImageSourceDialog(private val listener: ProfileFragment): DialogFragment() {
    interface ImageSourceDialogListener {
        fun onCameraSelected()
        fun onGallerySelected()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Pick Profile Picture").setItems(arrayOf("Open Camera", "Select from Gallery"))
        {_,
         which-> when (which) {
             0 -> listener.onCameraSelected()
             1 -> listener.onGallerySelected()
         }
        }
        return builder.create()
    }
}
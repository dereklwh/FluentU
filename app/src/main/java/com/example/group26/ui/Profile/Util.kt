package com.example.group26.ui.Profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

object Util {
    fun checkPermissions(fragment: ProfileFragment) {
        if (Build.VERSION.SDK_INT < 23) return
        if (ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                fragment.requireActivity(),
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), 0
            )
        }
    }

    fun getBitmap(fragment: ProfileFragment, imgUri: Uri): Bitmap {
        var bitmap = BitmapFactory.decodeStream(fragment.requireContext().contentResolver.openInputStream(imgUri))
        val matrix = Matrix()
        var ret = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        return ret
    }

    fun saveBitmapToFile(fragment: ProfileFragment, fileName: String, fileUri: Uri, rotation: Float) {
        val context = fragment.requireContext()
        val tempImgFile = File(context.getExternalFilesDir(null), fileName)

        FileOutputStream(tempImgFile).use { output ->
            getBitmap(fragment, fileUri).let { bitmap ->
                Matrix().apply { setRotate(rotation) }
                    .let { matrix ->
                        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                    }
                    .compress(Bitmap.CompressFormat.JPEG, 100, output)
            }
        }
    }
}
package com.example.takepicture

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.takepicture.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private var capturedImageUri: Uri? = null

    private fun captureImage() {
        this.capturedImageUri = createImageUri()
        takePictureLauncher.launch(this.capturedImageUri)
    }

    private fun loadImageIntoView(uri: Uri?) {
        uri?.let {
            binding.ivCapturedPicture.setImageURI(it)
        }
    }

    private fun createImageUri(): Uri? {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val filename = "IMG_$timestamp.jpg"
        val file = File(externalCacheDir, filename)

        return FileProvider.getUriForFile(this, "${packageName}.provider", file)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set up view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            result: Boolean ->
                if (result) loadImageIntoView(this.capturedImageUri)
                else Log.d("TakePicture", "Something went wrong with taking the picture")
        }

        binding.btnTakePicture.setOnClickListener {
            this.captureImage()
        }
    }

}
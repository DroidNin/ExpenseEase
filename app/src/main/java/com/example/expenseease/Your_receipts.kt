package com.example.expenseease

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

class Your_receipts : AppCompatActivity() {
    private val cameraRequestCode = 18
    private val images = mutableListOf<Bitmap>() // List to hold the images
    private lateinit var imageAdapter: ImageAdapter // Adapter for RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_your_receipts)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val captureButton = findViewById<Button>(R.id.appCompatButton)
        captureButton.setOnClickListener {
            openCamera()
            val recyclerRecp = findViewById<RecyclerView>(R.id.recycler_recp)
            imageAdapter = ImageAdapter(images)
            recyclerRecp.adapter = imageAdapter
            recyclerRecp.layoutManager = LinearLayoutManager(this)
        }
    }
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, cameraRequestCode)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequestCode && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                updateRecyclerView(imageBitmap)
                storeImage(imageBitmap)
            } else {
                Log.e("Camera", "Failed to receive image data")
            }
        }
    }



    private fun updateRecyclerView(image: Bitmap) {
        // Add image to your RecyclerView adapter
        images.add(image)
        imageAdapter.notifyDataSetChanged()
    }
    private fun storeImage(image: Bitmap) {
        val savedImageURI = saveImageToInternalStorage(image)
        val sharedPreferences = getSharedPreferences("MySharedPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("imageUri", savedImageURI.toString())
        editor.apply()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        // ContextWrapper provides a way to save images internally
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException){
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }


}

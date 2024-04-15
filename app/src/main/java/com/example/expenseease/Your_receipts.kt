package com.example.expenseease

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

class Your_receipts : AppCompatActivity() {
    private val cameraRequestCode = 18
    private val images = mutableListOf<Bitmap>() // List to hold the images
    private lateinit var imageAdapter: ImageAdapter // Adapter for RecyclerView
    private lateinit var recyclerRecp: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContentView(R.layout.activity_your_receipts)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerRecp = findViewById<RecyclerView>(R.id.recycler_recp)
        imageAdapter = ImageAdapter(images)
        recyclerRecp.adapter = imageAdapter
        recyclerRecp.layoutManager = LinearLayoutManager(this)

        // Load images from the database
        loadImages()

        setupCaptureButton()
    }
    private fun setupCaptureButton() {
        val captureButton = findViewById<Button>(R.id.appCompatButton)
        captureButton.setOnClickListener {
            openCamera()
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
        val imagePath = savedImageURI.toString()
        val imageEntity = ImageEntity(imagePath = imagePath)
        lifecycleScope.launch(Dispatchers.IO) {
            // Since insertImage is a suspend function, it must be called from within a coroutine or another suspend function
            AppDatabase.getDatabase(applicationContext).imageDao().insertImage(imageEntity)
        }
    }


    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val filename = "${UUID.randomUUID()}.png"
        val fos: FileOutputStream?
        try {
            fos = openFileOutput(filename, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Uri.fromFile(File(filesDir, filename))
    }
    private fun loadImages() {
        lifecycleScope.launch(Dispatchers.IO) {
            val imageList = AppDatabase.getDatabase(applicationContext).imageDao().getAllImages()
            withContext(Dispatchers.Main) {
                images.clear()
                imageList.forEach {
                    val bitmap = loadImageFromStorage(Uri.parse(it.imagePath))
                    images.add(bitmap)
                }
                imageAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun loadImageFromStorage(uri: Uri): Bitmap {
        return BitmapFactory.decodeFile(uri.path)
    }



}

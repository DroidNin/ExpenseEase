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
        imageAdapter = ImageAdapter(images){ position ->
            // This gets called when the delete button is clicked
            images.removeAt(position)
            imageAdapter.notifyItemRemoved(position)
            imageAdapter.notifyItemRangeChanged(position, images.size)
        }
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



    private fun updateRecyclerView(bitmap: Bitmap) {
        images.add(bitmap)
        imageAdapter.notifyDataSetChanged()
    }
    private fun storeImage(image: Bitmap) {
        val imageUri = saveImageToInternalStorage(image)
        val imageEntity = ImageEntity(imagePath = imageUri.toString())
        lifecycleScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(applicationContext).imageDao().insertImage(imageEntity)
        }
    }



    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val filename = "${UUID.randomUUID()}.png"  // Ensure unique filename
        val file = File(getExternalFilesDir(null), filename)  // You can use getFilesDir() for internal storage
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)  // PNG is lossless
        }
        return Uri.fromFile(file)
    }

    private fun loadImages() {
        lifecycleScope.launch(Dispatchers.IO) {
            val imageList = AppDatabase.getDatabase(applicationContext).imageDao().getAllImages()
            withContext(Dispatchers.Main) {
                images.clear()
                imageList.forEach { imageEntity ->
                    val bitmap = loadImageFromStorage(Uri.parse(imageEntity.imagePath))
                    if (bitmap != null) images.add(bitmap)
                }
                imageAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun loadImageFromStorage(uri: Uri): Bitmap? {
        return try {
            BitmapFactory.decodeFile(uri.path)
        } catch (e: Exception) {
            Log.e("LoadImage", "Error loading image", e)
            null
        }
    }



}

package com.example.expenseease

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 101
        private const val REQUEST_IMAGE_CAPTURE = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userdata: CardView = findViewById(R.id.userdata)
        userdata.setOnClickListener{
            val i = Intent(this,User_data::class.java)
            startActivity(i)
        }

        val receipt:CardView = findViewById(R.id.recp)
        receipt.setOnClickListener{
            val i = Intent(this,Your_receipts::class.java)
            startActivity(i)
        }

        val exp: LinearLayout = findViewById(R.id.linear_exp)

        exp.setOnClickListener {
            val i = Intent(this, Expense::class.java)
            startActivity(i)
            // Add the transition animation
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
          }
        val scanCard: CardView = findViewById(R.id.scan)
        scanCard.setOnClickListener {
            checkPermissionsAndOpenCamera()
        }

        val circularImageView: ImageView = findViewById(R.id.circularImageView)
        circularImageView.setOnClickListener {
            val drawerLayout: DrawerLayout? = findViewById(R.id.drawer_layout)
            drawerLayout?.openDrawer(GravityCompat.START)
        }
    }

    private fun checkPermissionsAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            Toast.makeText(this, "Camera permission is required to use this feature.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                processImageForText(it)
            }
        }
    }

    private fun processImageForText(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val options = TextRecognizerOptions.Builder().build()  // Use default options or configure as needed
        val recognizer: TextRecognizer = TextRecognition.getClient(options)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val resultText = visionText.text
                copyTextToClipboard(resultText)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error in recognizing text: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun copyTextToClipboard(text: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Scanned Text", text)
        clipboard.setPrimaryClip(clip)
        // Show a toast with the copied text
        Toast.makeText(this, "Text copied to clipboard: $text", Toast.LENGTH_LONG).show()
    }

}

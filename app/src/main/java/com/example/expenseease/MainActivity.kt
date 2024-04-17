package com.example.expenseease

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var menuButton: View
    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 101
        private const val REQUEST_IMAGE_CAPTURE = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateDisplay()
        displayUsername()


        menuButton = findViewById(R.id.menu_button)
        menuButton.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.open_drawer, R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout -> {
                    // Handle Item 1 click
                    showLogoutConfirmationDialog()
                    true
                }
                R.id.profile -> {
                    val i = Intent(this, Profile::class.java)
                    startActivity(i)
                    // Handle Item 2 click
                    true
                }
                R.id.privacy -> {
                    val i = Intent(this, Privacy::class.java)
                    startActivity(i)
                    // Handle Item 2 click
                    true
                }

                R.id.graphical -> {
                    val i = Intent(this, Graph::class.java)
                    startActivity(i)
                    // Handle Item 2 click
                    true
                }
                // Add more cases for other menu items
                else -> false
            }
        }



        // Get the passed income from the intent
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val income = sharedPreferences.getInt("income", 0) // Default to 0 if not found
        // Calculate the values based on the 50-30-20 rule
        val availableToUse = income * 0.50
        val savings = income * 0.30
        val investments = income * 0.20

        // Update the TextViews with the calculated values
        findViewById<TextView>(R.id.textView9).text = income.toString() // Displaying the income
        findViewById<TextView>(R.id.textView12).text = availableToUse.toString() // Displaying the amount available to use
        findViewById<TextView>(R.id.textView14).text = savings.toString() // Displaying the savings
        findViewById<TextView>(R.id.tv_invest).text = investments.toString() // Displaying the investments
        findViewById<TextView>(R.id.amount_left).text = availableToUse.toString() // Displaying the amount left, which is the same as available to use


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

//        val circularImageView: ImageView = findViewById(R.id.circularImageView)
//        circularImageView.setOnClickListener {
//            val drawerLayout: DrawerLayout? = findViewById(R.id.drawer_layout)
//            drawerLayout?.openDrawer(GravityCompat.START)
//        }

    }


    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { dialog, which ->
            // Handle logout action here, e.g., navigate to logout method
            performLogout()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun performLogout() {
        // Implement your logout logic here, such as clearing session or navigating to login screen
        // Example: Redirect to Login activity after logout
         val intent = Intent(this, Login::class.java)
         startActivity(intent)
         finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(navView)) {
                drawerLayout.closeDrawer(navView)
            } else {
                drawerLayout.openDrawer(navView)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayUsername() {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("current_user", "User") // Default to "No User" if not found
        findViewById<TextView>(R.id.textView6).text = "Hello $username !!"
    }
    override fun onResume() {
        super.onResume()
        updateDisplay()  // This ensures the display updates when returning from Expense Activity
    }
    private fun updateDisplay() {
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val availableToUse = sharedPreferences.getFloat("amountLeft", 0f)
        findViewById<TextView>(R.id.amount_left).text = availableToUse.toString()
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

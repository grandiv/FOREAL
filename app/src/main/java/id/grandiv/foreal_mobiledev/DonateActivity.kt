package id.grandiv.foreal_mobiledev

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DonateActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etLocation: EditText
    private lateinit var etFood: EditText
    private lateinit var etQuantity: EditText
    private lateinit var etExpdate: DatePicker
    private lateinit var btnDonate: Button

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation)

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@DonateActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        
        etName = findViewById(R.id.input_namedonation)
        etLocation = findViewById(R.id.input_locationdonation)
        etFood = findViewById(R.id.food_type)
        etQuantity = findViewById(R.id.food_quantity)
        etExpdate = findViewById<DatePicker>(R.id.idEdtDate)
        btnDonate = findViewById(R.id.donate_button)

        btnDonate.setOnClickListener {
            val sName = etName.text.toString().trim()
            val sLocation = etLocation.text.toString().trim()
            val sFood = etFood.text.toString().trim()
            val sQuantity = etQuantity.text.toString().trim()

            val day = etExpdate.dayOfMonth
            val month = etExpdate.month + 1
            val year = etExpdate.year

            val sExpdate = "$year-$month-$day"

            val userMap = hashMapOf(
                "Name" to sName,
                "Location" to sLocation,
                "Food" to sFood,
                "Quantity" to sQuantity,
                "Exp Date" to sExpdate
            )

            db.collection("Donations").document().set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Donated", Toast.LENGTH_SHORT).show()
                    etName.text.clear()
                    etLocation.text.clear()
                    etFood.text.clear()
                    etQuantity.text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Donation Failed", Toast.LENGTH_SHORT).show()
                }

        }
    }
}

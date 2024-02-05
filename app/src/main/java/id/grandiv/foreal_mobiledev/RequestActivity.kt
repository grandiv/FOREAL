package id.grandiv.foreal_mobiledev

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RequestActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etFullAddress: EditText
    private lateinit var etContactNumber: EditText

    private lateinit var tvDonator: TextView
    private lateinit var tvFoodReceived: TextView
    private lateinit var tvExpDateReceived: TextView

    private lateinit var btnRequest: Button

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@RequestActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        etName = findViewById(R.id.input_nameRequest)
        etFullAddress = findViewById(R.id.address_request)
        etContactNumber = findViewById(R.id.contact_numberRequest)
        btnRequest = findViewById(R.id.request_button)

        tvDonator = findViewById(R.id.popupDonator)
        tvFoodReceived = findViewById(R.id.popupFoodReceived)
        tvExpDateReceived = findViewById(R.id.popupExpDateReceived)

        btnRequest.setOnClickListener {
            val sName = etName.text.toString().trim()
            val sFullAddress = etFullAddress.text.toString().trim()
            val sContactNumber = etContactNumber.text.toString().trim()

            displayDonationData()

            val donator = tvDonator.text.toString().trim()
            val foodReceived = tvFoodReceived.text.toString().trim()
            val expDateReceived = tvExpDateReceived.text.toString().trim()

            val userMap = hashMapOf(
                "Name" to sName,
                "Full Address" to sFullAddress,
                "Contact Number" to sContactNumber,
                "Donator" to donator,
                "Food Received" to foodReceived,
                "Exp Date Received" to expDateReceived
            )

            db.collection("Recipients").document().set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Requested", Toast.LENGTH_SHORT).show()
                    etName.text.clear()
                    etFullAddress.text.clear()
                    etContactNumber.text.clear()

                }
                .addOnFailureListener {
                    Toast.makeText(this, "Request Failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showPopup(donator: String, foodReceived: String, expDateReceived: String) {
        val popupView = layoutInflater.inflate(R.layout.popup_donation_details, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupView.findViewById<TextView>(R.id.popupDonator).text = donator
        popupView.findViewById<TextView>(R.id.popupFoodReceived).text = foodReceived
        popupView.findViewById<TextView>(R.id.popupExpDateReceived).text = expDateReceived

        val closeButton = popupView.findViewById<Button>(R.id.closePopupButton)
        closeButton.setOnClickListener {
            popupWindow.dismiss()
        }

        popupWindow.showAtLocation(
            findViewById(android.R.id.content),
            Gravity.CENTER,
            0,
            0
        )
    }

    private fun displayDonationData() {
        val donationRef = db.collection("Donations")
        donationRef.get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val randomDocument = querySnapshot.documents.random()

                    val donator = randomDocument.getString("Name") ?: ""
                    val foodReceived = randomDocument.getString("Food") ?: ""
                    val expDateReceived = randomDocument.getString("Exp Date") ?: ""

                    tvDonator.text = donator
                    tvFoodReceived.text = foodReceived
                    tvExpDateReceived.text = expDateReceived

                    showPopup("Donator: $donator", "Food Received: $foodReceived", "Exp Date Received: $expDateReceived")
                } else {
                    Toast.makeText(this, "No donations available", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Request Failed", Toast.LENGTH_SHORT).show()
            }
    }

}
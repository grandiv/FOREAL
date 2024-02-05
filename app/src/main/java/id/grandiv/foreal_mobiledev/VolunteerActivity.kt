package id.grandiv.foreal_mobiledev

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class VolunteerActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etContactNumber: EditText
    private lateinit var spDelivery: Spinner

    private lateinit var tvRecipient: TextView
    private lateinit var tvFullAddress: TextView
    private lateinit var tvContactRecipient: TextView
    private lateinit var tvFoodReceived: TextView

    private lateinit var btnSubmit: Button

    private var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer)

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@VolunteerActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        etName = findViewById(R.id.input_nameVolunteer)
        etContactNumber = findViewById(R.id.input_numberVolunteer)
        spDelivery = findViewById(R.id.food_spinner)
        btnSubmit = findViewById(R.id.submit_button)

        tvRecipient = findViewById(R.id.popupRecipient)
        tvFullAddress = findViewById(R.id.popupFullAddress)
        tvContactRecipient = findViewById(R.id.popupContactRecipient)
        tvFoodReceived = findViewById(R.id.popupFoodReceived)

        displayDeliverySpinner()

        btnSubmit.setOnClickListener {
            val sName = etName.text.toString().trim()
            val sContactNumber = etContactNumber.text.toString().trim()
            val sDelivery = spDelivery.selectedItem.toString().trim()

            val selectedRecipientInfo = sDelivery.split(" | ")
            val recipient = selectedRecipientInfo[0].trim()
            val fulladdress = selectedRecipientInfo[1].trim()
            val contactrecipient = selectedRecipientInfo[2].trim()
            val foodreceived = selectedRecipientInfo[3].trim()



            val userMap = hashMapOf(
                "Name" to sName,
                "Contact Number" to sContactNumber,
                "Delivery" to sDelivery,
                "Recipient" to recipient,
                "Recipient's Address" to fulladdress,
                "Recipient's Contact" to contactrecipient,
                "Recipient's Food" to foodreceived
            )

            db.collection("Volunteers").document().set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Submitted", Toast.LENGTH_SHORT).show()
                    etName.text.clear()
                    etContactNumber.text.clear()
                    spDelivery.setSelection(0)

                    showPopup(recipient, fulladdress, contactrecipient, foodreceived)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Request Failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showPopup(recipient: String, fulladdress: String, contactrecipient: String, foodReceived: String) {
        val popupView = layoutInflater.inflate(R.layout.popup_delivery_details, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupView.findViewById<TextView>(R.id.popupRecipient).text = recipient
        popupView.findViewById<TextView>(R.id.popupFullAddress).text = fulladdress
        popupView.findViewById<TextView>(R.id.popupContactRecipient).text = contactrecipient
        popupView.findViewById<TextView>(R.id.popupFoodReceived).text = foodReceived

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

    private fun displayDeliverySpinner() {
        val deliveryRef = db.collection("Recipients")
        deliveryRef.get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val recipientList = mutableListOf<String>()

                    for (document in querySnapshot.documents) {
                        val recipient = document.getString("Name") ?: ""
                        val fullAddress = document.getString("Full Address") ?: ""
                        val contactRecipient = document.getString("Contact Number") ?: ""
                        val foodReceived = document.getString("Food Received") ?: ""

                        val formattedRecipientInfo = "$recipient | $fullAddress | $contactRecipient | $foodReceived"
                        recipientList.add(formattedRecipientInfo)
                    }

                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, recipientList)

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    spDelivery.adapter = adapter
                } else {
                    Toast.makeText(this, "No recipients available", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Request Failed", Toast.LENGTH_SHORT).show()
            }
    }
}
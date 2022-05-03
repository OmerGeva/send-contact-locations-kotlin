package com.example.omer_geva_drill_ii

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract

import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationServices
import java.util.Locale.*

class MainActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerAdapter? = null
    private var contacts = mutableListOf<String>()
    private var numbers = mutableListOf<String>()
    private var location: String? = null

    var cols = listOf<String>(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone._ID
    ).toTypedArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun btnGetContactPressed(view: View) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS), 111);
            Toast.makeText(this, "We need permission to access your contacts so we can send location.", Toast.LENGTH_SHORT).show()
        }
        else{
            getPhoneContacts()
        }
    }
    @SuppressLint("MissingPermission")
    fun btnGetLocationPressed(view: View?) {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
        }
        else {
            var fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            val task = fusedLocationProviderClient.lastLocation

            task.addOnSuccessListener {
                if(it != null){
                    var textView = findViewById<TextView>(R.id.locationText)
                    textView.text = "${getCityName(it.latitude, it.longitude)}"
                    location = "${getCityName(it.latitude, it.longitude)}"
                }
            }
        }
    }


    @SuppressLint("Range")
    private fun getPhoneContacts() {
        var from = listOf<String>(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER).toTypedArray()
        var to = intArrayOf(android.R.id.text1, android.R.id.text2)
        var rs = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        cols, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        rs?.moveToFirst();
        while (rs?.moveToNext() == true) {
            val number: String = rs.getString(rs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            var displayName: String = rs.getString(rs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            if(displayName == null || displayName == "" ) {
                displayName = "No Name"
            }
            contacts.add(displayName)
            numbers.add(number)
        }

        layoutManager = LinearLayoutManager(this)
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        if(location == null){
            btnGetLocationPressed(null)
        }
        adapter = RecyclerAdapter(contacts, numbers, location, this)
        recyclerView.adapter = adapter
        rs?.close()
    }

    private fun getCityName(lat: Double, long: Double): String {
        var geoCoder = Geocoder(this, getDefault())
        var address = geoCoder.getFromLocation(lat, long, 1)


        return "${address.get(0).getAddressLine(0)}"
    }
}

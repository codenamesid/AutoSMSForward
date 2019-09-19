package com.codenamesid.applicationseries.smsforward

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.material.snackbar.Snackbar

import java.util.Locale

import android.Manifest.permission.RECEIVE_SMS
import android.Manifest.permission.SEND_SMS

class MainActivity : AppCompatActivity() {
    private var receiver: SMSReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (ContextCompat.checkSelfPermission(this, RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, RECEIVE_SMS) && ActivityCompat.shouldShowRequestPermissionRationale(this, SEND_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this, arrayOf(RECEIVE_SMS, SEND_SMS), 101)
            }
        } else {
            startService()
        }

        val sharedPref = getSharedPreferences("store", Context.MODE_PRIVATE)
        val phoneNumber = sharedPref.getString("PHONE_NUMBER", null)

        if (phoneNumber != null) {
            (findViewById<View>(R.id.editText) as EditText).setText(phoneNumber)
        }
        /*receiver=new SMSReceiver();
        IntentFilter filter=new IntentFilter();
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(receiver,filter);*/
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startService()
            }
        }
    }

    fun startService() {
        val service = Intent(applicationContext, SMSService::class.java)
        startService(service)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    fun setForwardNumber(view: View) {

        var phoneNumber = (findViewById<View>(R.id.editText) as EditText).text.toString()

        if (Patterns.PHONE.matcher(phoneNumber).matches()) {
            val sharedPref = this.getSharedPreferences("store", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber, Locale.US.country)
            (findViewById<View>(R.id.editText) as EditText).setText(phoneNumber)
            editor.putString("PHONE_NUMBER", phoneNumber)
            editor.apply()
            val sb = Snackbar.make(findViewById(R.id.coordinatorLayout), "Forwarding Phone number set to $phoneNumber", 2000)
            sb.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (receiver != null) {
            Log.i("SMSReceiver", "Destroyed..")
            unregisterReceiver(receiver)
            receiver = null
        }
    }
}

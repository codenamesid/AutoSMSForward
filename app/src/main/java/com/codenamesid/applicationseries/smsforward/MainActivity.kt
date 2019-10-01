package com.codenamesid.applicationseries.smsforward

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private var receiver: SMSReceiver? = null
    private lateinit var viewModel: SMSViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        viewModel = ViewModelProviders.of(this).get(SMSViewModel::class.java)
        val adapter = SMSAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        var data = SMSDB.getDatabase(applicationContext).getSMSDAO().getAllSMS()
        subscribeUI(adapter)
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(this, permissions, 101)
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

    private fun subscribeUI(adapter: SMSAdapter) {
        viewModel.getSMSLiveData().observe(this, Observer { messages ->
            if (messages != null) adapter.submitList(messages)
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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

    private val permissions = arrayOf(android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.READ_SMS, android.Manifest.permission.SEND_SMS, android.Manifest.permission.READ_CALENDAR)
    private fun hasPermissions(): Boolean {
        for (perm in permissions) {
            if (ActivityCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }

        return true
    }


}

package com.codenamesid.applicationseries.smsforward

import android.content.Context
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import java.util.*
import java.util.regex.Pattern


class SettingsFragment:DialogFragment(){

    private var settingsEntryView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        settingsEntryView= inflater.inflate(R.layout.phone_entry,container,false)
        val sharedPref = context!!.getSharedPreferences("store", Context.MODE_PRIVATE)
        val phoneNumber = sharedPref.getString("PHONE_NUMBER", null)
        val filter = sharedPref.getString("FILTER_STR", "Apple ID")

        if ( settingsEntryView!=null) {
            if(!phoneNumber.isNullOrBlank()) {
                (settingsEntryView!!.findViewById<View>(R.id.editText) as EditText).setText(phoneNumber)
            }
            if(!filter.isNullOrBlank()){
                (settingsEntryView!!.findViewById<View>(R.id.filterText) as EditText).setText(filter)
            }
        }

        return settingsEntryView
    }
    fun setForwardNumber(): Boolean {
        val phoneNumberValidation= Pattern.compile("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*\$")
        var phoneNumber = (settingsEntryView!!.findViewById<View>(R.id.editText) as EditText).text.toString()

        return if (phoneNumberValidation.matcher(phoneNumber).matches()) {
            val sharedPref = context!!.getSharedPreferences("store", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber, Locale.US.country)
            (settingsEntryView!!.findViewById<View>(R.id.editText) as EditText).setText(phoneNumber)
            editor.putString("PHONE_NUMBER", phoneNumber)
            editor.apply()
            true
        }else{
            false
        }
    }
    fun setFilterString(): Boolean{
        var filter = (settingsEntryView!!.findViewById<View>(R.id.filterText) as EditText).text.toString()
        if(filter.isNotEmpty()){
            val sharedPref = context!!.getSharedPreferences("store", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("FILTER_STR", filter)
            editor.apply()
            return true
        }
        return false
    }

}
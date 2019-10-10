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

    private var phoneNumberEntryView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        phoneNumberEntryView= inflater.inflate(R.layout.phone_entry,container,false)
        val sharedPref = context!!.getSharedPreferences("store", Context.MODE_PRIVATE)
        val phoneNumber = sharedPref.getString("PHONE_NUMBER", null)

        if (phoneNumber != null && phoneNumberEntryView!=null) {
            (phoneNumberEntryView!!.findViewById<View>(R.id.editText) as EditText).setText(phoneNumber)
        }
        return phoneNumberEntryView
    }
    fun setForwardNumber(): Boolean {
        val phoneNumberValidation= Pattern.compile("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*\$")
        var phoneNumber = (phoneNumberEntryView!!.findViewById<View>(R.id.editText) as EditText).text.toString()
        return if (phoneNumberValidation.matcher(phoneNumber).matches()) {
            val sharedPref = context!!.getSharedPreferences("store", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber, Locale.US.country)
            (phoneNumberEntryView!!.findViewById<View>(R.id.editText) as EditText).setText(phoneNumber)
            editor.putString("PHONE_NUMBER", phoneNumber)
            editor.apply()
            true
        }else{
            false
        }

    }

}
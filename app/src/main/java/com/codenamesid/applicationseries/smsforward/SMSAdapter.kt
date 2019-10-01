package com.codenamesid.applicationseries.smsforward

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.sms_list_item.view.*


class SMSAdapter(val context: Context) : PagedListAdapter<SMS, SMSAdapter.SMSViewHolder>(SMSDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SMSViewHolder {
        return SMSViewHolder(context,LayoutInflater.from(context).inflate(R.layout.sms_list_item, parent, false));
    }

    override fun onBindViewHolder(holder: SMSViewHolder, position: Int) {
        var sms = getItem(position)
        if (sms == null) {
            holder.clear();
        } else {
            holder.bind(sms);
        }
    }


    class SMSViewHolder(val context: Context,view: View) : RecyclerView.ViewHolder(view) {
        var from = view.from
        var to = view.to
        var time = view.time
        var message = view.message
        var status = view.status

        fun clear() {
            from.text = null
            to.text = null
            time.text = null
            message.text = null
        }

        fun bind(sms: SMS) {
            //Log.i("SMSReceiver", sms.toString())
            from.text = this.context.getString(R.string.from, sms.from)
            to.text = this.context.getString(R.string.sent_to,sms.to)
            time.text = this.context.getString(R.string.at, sms.date.toString())
            message.text = this.context.getString(R.string.message, sms.message)
            status.text=this.context.getString(R.string.status ,if(sms.relayed)"Relayed" else "Not Relayed");
        }

    }
}
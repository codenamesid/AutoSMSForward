package com.codenamesid.applicationseries.smsforward

import androidx.recyclerview.widget.DiffUtil

class SMSDiffCallback :DiffUtil.ItemCallback<SMS>(){
    override fun areItemsTheSame(oldItem: SMS, newItem: SMS): Boolean {
       return oldItem.uid==newItem.uid
    }

    override fun areContentsTheSame(oldItem: SMS, newItem: SMS): Boolean {
        return oldItem==newItem
    }

}
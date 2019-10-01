package com.codenamesid.applicationseries.smsforward

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

class SMSViewModel constructor(application: Application):AndroidViewModel(application){
    private var smsLiveData: LiveData<PagedList<SMS>>
    init{
        var factory: DataSource.Factory<Int,SMS> = SMSDB.getDatabase(getApplication()).getSMSDAO().getAllSMS()
        val pagedListBuilder: LivePagedListBuilder<Int,SMS> = LivePagedListBuilder<Int,SMS>(factory,5)
        smsLiveData=pagedListBuilder.build();
    }
    fun getSMSLiveData()=smsLiveData
}
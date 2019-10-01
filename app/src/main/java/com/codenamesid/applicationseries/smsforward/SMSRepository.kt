package com.codenamesid.applicationseries.smsforward

import androidx.paging.DataSource
import io.reactivex.Completable

interface SMSRepository{
    fun insert(sms: SMS):Completable
    fun getAllSMS(): DataSource.Factory<Int,SMS>
}
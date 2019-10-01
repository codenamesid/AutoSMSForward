package com.codenamesid.applicationseries.smsforward

import androidx.paging.DataSource
import androidx.room.*

@Dao
interface SMSDAO{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(sms: SMS)


    @Update
    fun update(sms: SMS)

    @Query ("Select * from tbl_sms ORDER BY time DESC")
    fun getAllSMS():DataSource.Factory<Int,SMS>

}
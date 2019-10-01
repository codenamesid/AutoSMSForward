package com.codenamesid.applicationseries.smsforward

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SMS::class],version = 1,exportSchema = false)
abstract class SMSDB: RoomDatabase() {

    abstract fun getSMSDAO():SMSDAO

    companion object{
        @Volatile
        private var instance:SMSDB?=null;

        fun getDatabase(context: Context):SMSDB{
            val temp= instance;
            if(temp !=null){
                return temp;
            }else{
                synchronized(this) {
                    val temp1 = Room.databaseBuilder(context.applicationContext, SMSDB::class.java, "sms_db").build();
                    instance = temp1
                    return temp1
                }
            }
        }
    }
}
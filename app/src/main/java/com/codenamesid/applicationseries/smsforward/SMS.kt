package com.codenamesid.applicationseries.smsforward

import android.telephony.SmsMessage
import androidx.room.*
import java.util.*


@Entity(tableName = "tbl_sms")
data class SMS(
        @ColumnInfo(name = "message") val message: String,
        @ColumnInfo(name = "from") val from: String,
        @ColumnInfo(name = "to") val to: String,
        @ColumnInfo (name="relayed") var relayed:Boolean
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name =  "id")
    var uid: Int = 0
    @ColumnInfo(name = "time")
    @TypeConverters(DateConverter::class)
    var date: Date = Date()

    constructor(sms: SmsMessage, to: String, relayed: Boolean) : this(sms.displayMessageBody, sms.originatingAddress!!, to,relayed)

    override fun toString(): String {
        return "From: $from : $message to: $to at: $date Relayed: $relayed"
    }
}

class DateConverter {

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time!!.toLong()
    }
}
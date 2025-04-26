package com.do_big.diginotes.utils

import androidx.room.TypeConverter
import java.util.Date

object Converters {
    @JvmStatic
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @JvmStatic
    @TypeConverter
    fun datetoTimestamp(date: Date?): Long? {
        return date?.time
    }
}

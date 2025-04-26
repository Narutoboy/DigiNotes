package com.do_big.diginotes.utils

import java.text.SimpleDateFormat
import java.util.Date

object Utils {
    @JvmStatic
    fun formateDate(date: Date): String {
        val simpleDateFormat = SimpleDateFormat.getDateInstance() as SimpleDateFormat
        simpleDateFormat.applyPattern("EEE, d/MMM ")

        return simpleDateFormat.format(date)
    }
}

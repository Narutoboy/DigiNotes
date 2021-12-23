package com.do_big.diginotes.utils;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value){
        return  value==null?null:new Date(value);

    }
    @TypeConverter
    public static Long datetoTimestamp(Date date){
        return date==null?null:date.getTime();

    }

}

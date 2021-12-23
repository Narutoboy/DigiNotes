package com.do_big.diginotes.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
 public static String formateDate(Date date){
     SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
     simpleDateFormat.applyPattern("EEE, d/MMM ");

     return simpleDateFormat.format(date);
 }


}

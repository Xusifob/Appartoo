package com.appartoo.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by alexandre on 16-08-24.
 */
public class DateManager {

    public static String getFormattedDate(Long timestamp){

        if(String.valueOf(timestamp).length() > 10) {
            int uselessDigits = String.valueOf(timestamp).length() - 10;
            timestamp = (timestamp - (timestamp % ((long) Math.pow(10, uselessDigits))))/((long) Math.pow(10, uselessDigits));
        }

        Calendar now = Calendar.getInstance();

        Calendar messageTime = Calendar.getInstance();
        messageTime.setTimeInMillis(timestamp * 1000);

        SimpleDateFormat timeFormatString = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dateTimeFormatString = new SimpleDateFormat("EEE d MMM", Locale.getDefault());

        if(now.get(Calendar.DATE) == messageTime.get(Calendar.DATE)) {
            return timeFormatString.format(messageTime.getTime());
        } else {
            return dateTimeFormatString.format(messageTime.getTime());
        }
    }
}

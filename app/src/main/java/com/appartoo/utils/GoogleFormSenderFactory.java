package com.appartoo.utils;

import android.content.Context;

import org.acra.config.ACRAConfiguration;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderFactory;

/**
 * Created by alexandre on 16-08-31.
 */
public class GoogleFormSenderFactory implements ReportSenderFactory {

    public ReportSender create(Context context, ACRAConfiguration config) {

        return new GoogleFormSender();
    }
}

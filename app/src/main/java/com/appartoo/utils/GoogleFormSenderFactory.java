package com.appartoo.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import com.appartoo.R;
import com.appartoo.activity.LoginActivity;

import org.acra.config.ACRAConfiguration;
import org.acra.dialog.CrashReportDialog;
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

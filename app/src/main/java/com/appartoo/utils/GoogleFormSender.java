package com.appartoo.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.appartoo.activity.LoginActivity;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.acra.sender.ReportSenderFactory;
import org.acra.util.JSONReportBuilder;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by alexandre on 16-08-31.
 */
public class GoogleFormSender implements ReportSender {

    @Override
    public void send(final Context context, final CrashReportData report) throws ReportSenderException {

        Retrofit retrofitSender = new Retrofit.Builder()
                .baseUrl("https://docs.google.com/forms/d/e/1FAIpQLScH77O2_-FsMZipSpwqxlPYuUzv1bIXFbI1j1Gu1dvGnm_63w/")
                .build();

        GoogleServices googleServices = retrofitSender.create(GoogleServices.class);

        Call<ResponseBody> callback = googleServices.reportError(report.getProperty(ReportField.STACK_TRACE),
                "Brand: " + report.getProperty(ReportField.BRAND) + "\n" +
                "Phone model: " + report.getProperty(ReportField.PHONE_MODEL) + "\n" +
                "Android version: " + report.getProperty(ReportField.ANDROID_VERSION) + "\n" +
                "App version: " + report.getProperty(ReportField.APP_VERSION_CODE),
                report.getProperty(ReportField.LOGCAT),
                report.get(ReportField.USER_COMMENT),
                "Total memory size: " + report.getProperty(ReportField.TOTAL_MEM_SIZE) + "\n" +
                "Available memory size: " + report.getProperty(ReportField.AVAILABLE_MEM_SIZE) + "\n" +
                "Build: " + report.getProperty(ReportField.BUILD));

        callback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(!response.isSuccessful()) {
                    System.out.println(response.code());
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Report sent !");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}

package com.example.nk31001905.tabproject.util;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.Context;

/**
 * Created by nk91008743 on 30-09-2015.
 */
public class Email {
    public static void sendEmail(View v, Context c) {
        String[] TO = {"userg411@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            //startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."), SEND_MAIL_REQUEST);
            c.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.i("Finished sending email.", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(c,"There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.example.nk31001905.tabproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class SingleQuestion extends Activity {


    static final int SEND_MAIL_REQUEST = 1;

    private String variantLink;
    private String variantTitle;

    public final static String EXTRA_MESSAGE = "com.example.nk31001905.set";
    public final static String MY_ANSWERS = "com.example.nk31001905.set";

    private WebView questionWebView;
    private TextView questionTitle;
    private ScrollView myScrollView;
    private RadioGroup options;
    private Button next;
    private Button prev;
    private boolean checked = false;
    String myXml;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.single_question);

        questionWebView = (WebView) findViewById(R.id.webview);
        questionWebView.getSettings().setJavaScriptEnabled(true);

        questionWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        questionWebView.setLongClickable(false);


        Intent intent = getIntent();
        variantLink = intent.getStringExtra("variant_link");
        variantTitle = intent.getStringExtra("variant_title");

        //questionTitle.setText(variantTitle);
        //progressDialog = ProgressDialog.show(this, "Downloading","Please wait...");
        Thread mThread = new Thread() {
            @Override
            public void run() {
                questionWebView.loadUrl(variantLink);
                questionWebView.setWebViewClient(new WebViewClient());
                //progressDialog.dismiss();
            }
        };
        mThread.start();

        //new loadUrl().execute();

    }
    private class loadUrl extends AsyncTask<Void, Integer, String> {
        String xml;
        ProgressDialog progressDialog = new ProgressDialog(SingleQuestion.this);
        @Override
        protected String doInBackground(Void... url) {
            try {
                    for(int i=0;i<1000;i++)
                        Log.i("hi", ""+i);

            } catch(Exception e){
                e.printStackTrace();
            }
            return xml;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);
        }
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog.setMessage("Downloading source..");
            progressDialog.show();


        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //questionWebView.loadUrl(variantLink);
            //questionWebView.setWebViewClient(new WebViewClient());
            progressDialog.dismiss();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void sendEmail(View v) {
        String[] TO = {"userg411@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Вопрос по Физике");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."), SEND_MAIL_REQUEST);
            //startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            // Log.i("Finished sending email.", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    ;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  return true;
        //}

        return super.onOptionsItemSelected(item);
    }


}

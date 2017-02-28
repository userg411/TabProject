package com.example.nk31001905.tabproject;

import android.content.ContextWrapper;
import android.util.Log;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import android.content.Context;
import android.os.AsyncTask;
/**
 * Created by nk91008743 on 28/Sep/15.
 */
public class FileStore extends ContextWrapper{
    private static Context context;
    private static final String TAG = "FILESTORE";

    public FileStore(Context c){
        super(c);
    }


    public  void copyFromUrl(String url, String file, Context c)
    {
        new copyFromUrl().execute("");
    }
    public  void writeToFile(Context c){
        FileOutputStream outputStream;
        String filename = "myfile";
        String string = "Hello world!";

        try {
            outputStream =  c.openFileOutput(filename, c.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listFiles(){
        String [] files = context.fileList();
        Log.i(TAG, "listing files");
        for(String s:files)
            Log.i(TAG, s);
    }
    class copyFromUrl extends AsyncTask<String, Void, Void> {


        private Exception exception;
        protected Void doInBackground(String... urls) {
            try {
                URL website = new URL("http://www.nur.kz/906396-voditeley-ustroivshikh-drift-shou-v-alma.html");
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = context.openFileOutput("information2.html", Context.MODE_PRIVATE);
                fos.getChannel().transferFrom(rbc, 0, 1024);

                Log.i(TAG, "Wrote to file");
                listFiles();

            } catch (Exception e) {
                this.exception = e;
                Log.e(TAG,Log.getStackTraceString(e));


            }
            return null;
        }

        protected void onPostExecute() {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }


}

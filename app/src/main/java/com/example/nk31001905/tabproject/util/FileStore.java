package com.example.nk31001905.tabproject.util;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.ByteChannel;
import android.content.Context;
import android.os.AsyncTask;
/**
 * Created by nk91008743 on 28/Sep/15.
 */
public class FileStore {
    private static Context context;
    private static final String TAG = "FILESTORE";

    public FileStore(Context c){
        this.context = c;
    }
    public static boolean fileExists(String fileName){
        return context.getFileStreamPath(fileName).exists();
    }
    public static void listFiles(){
        String[] listOfFiles = context.getFilesDir().list();
        for(String file:listOfFiles)
            Log.d(TAG, file);
    }


    public  void copyFromUrl(String url, String file, Context c)
    {
        File f = c.getFileStreamPath(file);
        if(!f.exists())
            new copyFromUrl().execute(url, file);
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


    class copyFromUrl extends AsyncTask<String, Void, Void> {


        private Exception exception;
        protected Void doInBackground(String... params) {
            try {
                URL website = new URL(params[0]);
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = context.openFileOutput(params[1], Context.MODE_PRIVATE);

                long byteCount = 0;
                while(fos.getChannel().transferFrom(rbc, fos.getChannel().size(), 1024) > 0) {
                    byteCount+=1024;
                    if(byteCount>Long.MAX_VALUE)
                        break;
                };
                Log.i(TAG, "Wrote to file");

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

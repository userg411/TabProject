package com.example.nk31001905.tabproject;

import android.util.Log;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import android.content.Context;

/**
 * Created by nk91008743 on 28/Sep/15.
 */
public class FileStore {
    private static final String TAG = "FILESTORE";
    public  void copyFromUrl(String url, String file, Context c)
    {
        try {
            //System.setProperty("java.net.useSystemProxies", "true");
            URL website = new URL("http://www.azh.kz");
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            Log.i(TAG, "channel is open"+rbc.isOpen());
            FileOutputStream fos = c.openFileOutput("information", c.MODE_PRIVATE);
            //FileOutputStream fos = new FileOutputStream("information.html");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            Log.i( TAG,"Wrote to file");
        }
        catch(Exception e)
        {
            String err = (e.getMessage()==null)?"Write Failed":e.getMessage();
            Log.i(TAG,err);
        }
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


}

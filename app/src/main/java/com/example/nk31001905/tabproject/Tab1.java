 package com.example.nk31001905.tabproject;
 import com.example.nk31001905.tabproject.util.FileStore;
 import com.example.nk31001905.tabproject.util.Email;
/**
 * Created by nk31001905 on 12/May/15.
 */
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;



/**
 * Created by Edwin on 15/02/2015.
 */
public class Tab1 extends Fragment implements View.OnClickListener{




    private static boolean networkConnected=false;

    static final int SEND_MAIL_REQUEST = 1;

    private String subject = "physics";
    ArrayList<Variant> variants;
    ProgressDialog progressDialog;
    private Button sendMailButton;

    ArrayList<String> variantTitles;
    private LinearLayout variantsLayout;
    private ProgressDialog progress;
    LinearLayout.LayoutParams param;
    private int localVariantsNum = 0;

    private NumberPicker np;
    ArrayAdapter adapter;
    NetworkChangeReceiver receiver;
    IntentFilter filter;
    boolean gotFromServer = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        variants = new ArrayList<Variant>();

        View v = inflater.inflate(R.layout.tab_1, container, false);
        sendMailButton = (Button) v.findViewById(R.id.sendQuestion);
        sendMailButton.setOnClickListener(this);
        variantsLayout = (LinearLayout) v.findViewById(R.id.variantsLayout);
        param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(0, 0, 0, -10);
        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();

        initializeVariants();

        return v;
    }


    public void onResume(){
        super.onResume();
        getActivity().registerReceiver(receiver, filter);
    }
    public void onPause(){
        super.onResume();
        getActivity().unregisterReceiver(receiver);
    }
    public void onClick(View v)
    {
        switch(v.getId()){
            case R.id.sendQuestion:
                Email.sendEmail(v,getActivity());
                break;
        }
    }

    public void initializeVariants() {
        variants.clear();
        variantsLayout.removeAllViews();

        String [] shippedVariants = getResources().getStringArray(R.array.local_physics_ids);

        for(int i=0; i<shippedVariants.length;i++){
            String title = Arrays.asList(getResources().getStringArray(R.array.local_physics_titles)).get(i);
            String link = Arrays.asList(getResources().getStringArray(R.array.local_physics_links)).get(i);
            variants.add(new Variant(shippedVariants[i], title, link,true));
        }

        if(getActivity().getFilesDir().list().length>0){
            String startId = getString(R.string.start_id_physics);
            int counter = Integer.parseInt(startId.substring(1));
            String id,title, link;
            String[] listOfFiles = getActivity().getFilesDir().list();
            for(String fileName:listOfFiles){
                id = "p"+counter;
                title = fileName;
                link ="file:///data/data/com.example.example.nk31001905.tabproject/files/"+fileName;
                variants.add(new Variant(id, title, link, true));
            }
        }

        if (isNetworkAvailable()) {
            new XmlParser().execute();
        }
        else
        {
            Toast.makeText(getActivity(), R.string.no_internet_warning, Toast.LENGTH_SHORT).show();
            displayVariants();
        }


    }
    public void displayVariants(){
        for (int i = 0; i < variants.size(); i++) {
            Button b = new Button(getActivity());
            b.setText(variants.get(i).title);
            b.setLayoutParams(param);
            b.setOnClickListener(handleOnClick(b));
            b.setTag(R.id.VARIANT_LINK, variants.get(i).link);
            b.setTag(R.id.VARIANT_TITLE,variants.get(i).title);
            b.setTag(R.id.LOCAL_CONTENT, "true");
            variantsLayout.addView(b);
        }
    }

    View.OnClickListener handleOnClick(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SingleQuestion.class);
                String link = (String) button.getTag(R.id.VARIANT_LINK);
                if(!link.startsWith("file"))
                    link = "file:///data/data/com.example.example.nk31001905.tabproject/files/"+link;
                intent.putExtra("variant_link", (String) button.getTag(R.id.VARIANT_LINK));
                intent.putExtra("variant_title",(String)( button.getTag(R.id.VARIANT_TITLE)));
                if(button.getTag(R.id.LOCAL_CONTENT).equals("true")||isNetworkAvailable())
                    startActivity(intent);
                else
                    Toast.makeText(getActivity(),
                            getResources().getString(R.string.no_internet_warning), Toast.LENGTH_SHORT).show();

            }
        };
    }

 /*public void onPause() {
        unregisterReceiver(receiver);
    }
    */
    private class XmlParser extends AsyncTask<Void, Integer, String> {
        String xml;
        int progress_status = 0;


        @Override
        protected String doInBackground(Void... url) {
            try {
                // defaultHttpClient
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(getString(R.string.g_drive_link));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                xml = client.execute(get, responseHandler);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return xml;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "Подождите", "Идет загрузка...");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new ByteArrayInputStream(result.getBytes()));
                NodeList nodes = doc.getElementsByTagName(subject);

                for (int i = 0; i < nodes.getLength(); i++) {
                    Element element = (Element) nodes.item(i);

                    NodeList NodeId = element.getElementsByTagName("id");
                    Element id = (Element) NodeId.item(0);

                    NodeList NodeLink = element.getElementsByTagName("link");
                    Element link = (Element) NodeLink.item(0);

                    NodeList NodeTitle = element.getElementsByTagName("title");
                    Element title = (Element) NodeTitle.item(0);

                    Variant v = new Variant(id.getTextContent(), title.getTextContent(), link.getTextContent(), false);
                    if(!variants.contains(v)){
                        FileStore f = new FileStore(getActivity());
                        f.copyFromUrl(link.getTextContent(), "p"+title+".html", getActivity());
                        variants.add(v);
                    }
                    displayVariants();

                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            gotFromServer=true;
            progressDialog.dismiss();
        }
    }



   private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();
       if(activeInfo!=null && activeInfo.isConnected()){
            networkConnected = (activeInfo.getType() == ConnectivityManager.TYPE_WIFI)||(activeInfo.getType() == ConnectivityManager.TYPE_MOBILE);
           if(activeInfo.getType()==ConnectivityManager.TYPE_WIFI)
               Log.i(" kj",getString(R.string.wifi_connection));
           if(activeInfo.getType()==ConnectivityManager.TYPE_MOBILE)
               Log.i(" kj",getString(R.string.mobile_connection));
       }
       else {
           Log.i("sad",getString(R.string.no_internet_warning));
       }

       return activeInfo != null && activeInfo.isConnected();
    }

    private class NetworkChangeReceiver extends BroadcastReceiver {
        public void onReceive(final Context context, final Intent intent) {
            if(!gotFromServer)displayVariants();
        }
    }
}
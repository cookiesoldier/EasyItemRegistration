package com.example.martinvieth.easyitemregistration;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by Martin Vieth on 18-11-2015.
 * <p>
 * Tanken med denne klasse er at den skal stå for at sende og requeste data til/fra databasen, vi vil primært arbejde
 * med registreringsDTO objekter.
 */
public class databaseDAO {


    //String urlString = "http://78.46.187.172:4019";
    String urlString = "http://msondrup.dk/api/v1";
    String protString = "/?userID=56837dedd2d76438906140";

    URL url;


    public String itemList() throws IOException {

        //The url is the message sent to the server, we add  /items to complete the message to the server.
        //And the message is sent in the following lines.
        getItem(19);
        url = new URL(urlString + "/items" + protString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            int response = urlConnection.getResponseCode();
            Log.d("Server response ----->", "The response is: " + response);


            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            return readStream(in);

        } finally {
            urlConnection.disconnect();
        }


    }

    public String getItem(int itemNr) throws IOException {

        url = new URL(urlString + "/items/" + Integer.toString(itemNr)+protString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


        try {
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            urlConnection.connect();
            //Check til at se hvad serveren svarer på vores request
            int response = urlConnection.getResponseCode();
            Log.d("Server response ----->", "The response is: " + response);


            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            return readStream(in);

        } finally {
            urlConnection.disconnect();
        }

    }

    public boolean createItem(RegistreringsDTO data) throws IOException {

        //to add a new item we use the registrerings DTO that holds the data we wish to use.
        //First we are gonna build the message to send to the server.
    /*
        url = new URL(urlString + "/items?"
                +"itemheadline="+data.getItemHeadline()
                +"&itemdescription="+data.getBeskrivelse()
                +"&itemrecieved="+data.getRecieveDate()
                +"&itemdatafrom="+data.getDatingFrom()
                +"&itemdatingto="+data.getDatingTo()
                +"&donator="+data.getRefDonator()
                +"&producer="+data.getRefProducer()
                +"&postnummer="+data.getGeoArea()+protString);
        */
        int response = 0;
        JSONObject dublinCoreData = new JSONObject();

        try {
            dublinCoreData.put("itemheadline", data.getItemHeadline());
            dublinCoreData.put("itemdescription", data.getBeskrivelse());
            dublinCoreData.put("itemreceived",data.getRecieveDate());
            dublinCoreData.put("itemdatingfrom",data.getDatingFrom());
            dublinCoreData.put("itemdatingto",data.getDatingTo());
            dublinCoreData.put("donator",data.getRefDonator());
            dublinCoreData.put("producer",data.getRefProducer());
            dublinCoreData.put("postnummer",data.getGeoArea());
        } catch (JSONException e) {
            e.printStackTrace();

        }
        //Converting data to bytes
        byte[] dBData = dublinCoreData.toString().getBytes(StandardCharsets.UTF_8);
        int dBDataLength = dBData.length;

        url = new URL(urlString + "/items" + protString);
        Log.d("Sent to server ....>", "We sent:" + url.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {

            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);

            //Test til mathias database - virker!
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("charset", "utf-8");
            urlConnection.setRequestProperty("Content-Length", Integer.toString(dBDataLength));
            urlConnection.setUseCaches(false);

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.write(dBData);

            //test slut
            urlConnection.connect();
            response = urlConnection.getResponseCode();
            Log.d("Server response ----->", "The response is: " + response);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            Log.d("asd", readStream(in));


        } finally {
            urlConnection.disconnect();
            Log.d("Server response ----->", "The response is: " + response);
            if (response == 200 || response == 201) {
                return true;
            } else {
                return false;
            }

        }


    }

    public boolean updateItem(RegistreringsDTO data) throws IOException {


        //To update a item we simply take the new dublincore data and overwrite the excisting.
        //For picture it should be possible to get the excisting ones and add new pictures to these.
        //but not yet since database can't handle pictures atm.

        int response = 0;

        JSONObject dublinCoreData = new JSONObject();

        try {
            dublinCoreData.put("itemheadline", data.getItemHeadline());
            dublinCoreData.put("itemdescription", data.getBeskrivelse());
            dublinCoreData.put("itemreceived",data.getRecieveDate());
            dublinCoreData.put("itemdatingfrom",data.getDatingFrom());
            dublinCoreData.put("itemdatingto",data.getDatingTo());
            dublinCoreData.put("donator",data.getRefDonator());
            dublinCoreData.put("producer",data.getRefProducer());
            dublinCoreData.put("postnummer",data.getGeoArea());
        } catch (JSONException e) {
            e.printStackTrace();

        }
        //Converting data to bytes
        byte[] dBData = dublinCoreData.toString().getBytes(StandardCharsets.UTF_8);
        int dBDataLength = dBData.length;

        url = new URL(urlString + "/items/"+ data.getItemNr()+protString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {

            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);

            //Test til mathias database - virker!
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("charset", "utf-8");
            urlConnection.setRequestProperty("Content-Length", Integer.toString(dBDataLength));
            urlConnection.setUseCaches(false);

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.write(dBData);

            //test slut
            urlConnection.connect();
            response = urlConnection.getResponseCode();
            Log.d("Server response ----->", "The response is: " + response);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            Log.d("asd", readStream(in));

        } finally {
            urlConnection.disconnect();

            if (response == 200 || response == 201) {
                return true;
            } else {
                return false;
            }

        }


    }

    public void deleteItem(int itemNr) throws IOException {


        //To update a item we simply take the new dublincore data and overwrite the excisting.
        //For picture it should be possible to get the excisting ones and add new pictures to these.
        //but not yet since database can't handle pictures atm.
        url = new URL(urlString + "/items/" + Integer.toString(itemNr)+protString);

        int response = 0;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setDoInput(true);


            urlConnection.connect();
            response = urlConnection.getResponseCode();
            Log.d("Server response ----->", "The response is: " + response);

        } finally {
            urlConnection.disconnect();
        }


    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = r.readLine()) != null) {
            sb.append(line);
        }

        is.close();
        return sb.toString();
    }


}

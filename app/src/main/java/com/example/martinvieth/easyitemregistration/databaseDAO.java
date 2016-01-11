package com.example.martinvieth.easyitemregistration;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Martin Vieth on 18-11-2015.
 * <p/>
 * Tanken med denne klasse er at den skal stå for at sende og requeste data til/fra databasen, vi vil primært arbejde
 * med registreringsDTO objekter.
 */
public class databaseDAO {


    //String urlString = "http://78.46.187.172:4019";
    String urlString = "http://msondrup.dk/api/v1";
    String protString = "/?userID=56837dedd2d76438906140";
    Context frontPageActContext;
    URL url;

    public databaseDAO(Context c) {
        frontPageActContext = c;

    }


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

        url = new URL(urlString + "/items/" + Integer.toString(itemNr) + protString);
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
        int response = 0;
        JSONObject dublinCoreData = new JSONObject();

        try {
            dublinCoreData.put("itemheadline", data.getItemHeadline());
            dublinCoreData.put("itemdescription", data.getBeskrivelse());
            dublinCoreData.put("itemreceived", data.getRecieveDate());
            dublinCoreData.put("itemdatingfrom", data.getDatingFrom());
            dublinCoreData.put("itemdatingto", data.getDatingTo());
            dublinCoreData.put("donator", data.getRefDonator());
            dublinCoreData.put("producer", data.getRefProducer());
            dublinCoreData.put("postnummer", data.getGeoArea());
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
               //sendPicSound(data);
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
            dublinCoreData.put("itemreceived", data.getRecieveDate());
            dublinCoreData.put("itemdatingfrom", data.getDatingFrom());
            dublinCoreData.put("itemdatingto", data.getDatingTo());
            dublinCoreData.put("donator", data.getRefDonator());
            dublinCoreData.put("producer", data.getRefProducer());
            dublinCoreData.put("postnummer", data.getGeoArea());
        } catch (JSONException e) {
            e.printStackTrace();

        }
        //Converting data to bytes
        byte[] dBData = dublinCoreData.toString().getBytes(StandardCharsets.UTF_8);
        int dBDataLength = dBData.length;

        url = new URL(urlString + "/items/" + data.getItemNr() + protString);
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

            Log.d("update: ---->", readStream(in));

        } finally {
            urlConnection.disconnect();

            if (response == 200 || response == 201) {
                sendPicSound(data);
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
        url = new URL(urlString + "/items/" + Integer.toString(itemNr) + protString);

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

    private boolean sendPicSound(RegistreringsDTO dataDTO) throws IOException {
        String[] okTypes = {"image/png", "image/jpg", "image/jpeg",
                "audio/mp4", "audio/3gp", "audio/3GPP", "audio/aac", "audio/mp3"};
        int response = 0;
        try {
            for (Uri uri : dataDTO.getImages()) {


                //Så checker vi hvilken type filen er.
                ContentResolver cR = frontPageActContext.getContentResolver();
                String type = cR.getType(uri);
                //Vi check er om filens type er en af dem som databasen kan bruge.
                if (!Arrays.asList(okTypes).contains(type)) {
                    Log.d("Error:FileType: ", type);
                    return false;
                }
                //Gem billedet i byteArray
                InputStream fileInputStream = cR.openInputStream(uri);
                byte[] bFile = new byte[fileInputStream.available()];

                try {
                    //convert file into array of bytes
                    fileInputStream.read(bFile);
                    fileInputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Send filen til databasen

                url = new URL(urlString + "/items/" + dataDTO.getItemNr() + protString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {

                    urlConnection.setReadTimeout(10000 /* milliseconds */);
                    urlConnection.setConnectTimeout(15000 /* milliseconds */);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);

                    //Type
                    urlConnection.setRequestProperty("Content-Type", type);
                    urlConnection.setRequestProperty("charset", "utf-8");
                    Log.d("File Byte Size --->", Integer.toString(bFile.length));
                    urlConnection.setRequestProperty("Content-Length", Integer.toString(bFile.length));
                    urlConnection.setUseCaches(false);

                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                    wr.write(bFile);

                    //test slut
                    urlConnection.connect();
                    response = urlConnection.getResponseCode();
                    Log.d("Server response ----->", "The response is: " + response);

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    Log.d("picture ---->", readStream(in));

                    //check response kode
                } catch (Exception e) {
                    urlConnection.disconnect();
                    e.printStackTrace();

                }
            }
        } finally {

            if (response == 200 || response == 201) {
                return true;
            } else {
                return false;
            }

        }
    }
}

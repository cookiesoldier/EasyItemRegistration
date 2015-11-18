package com.example.martinvieth.easyitemregistration;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Martin Vieth on 18-11-2015.
 * <p>
 * Tanken med denne klasse er at den skal stå for at sende og requeste data til/fra databasen, vi vil primært arbejde
 * med registreringsDTO objekter.
 */
public class databaseDAO {


    String urlString = "http://78.46.187.172:4019";

    URL url;


    public String itemList() throws IOException {

        //The url is the message sent to the server, we add  /items to complete the message to the server.
        //And the message is sent in the following lines.
        url = new URL(urlString + "/items");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            String message = urlString + "/items";
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

        url = new URL(urlString + "/items/" + Integer.toString(itemNr));
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

    public void createItem(registreringsDTO data) throws IOException {

        //to add a new item we use the registrerings DTO that holds the data we wish to use.
        //First we are gonna build the message to send to the server.
        url = new URL(urlString + "/items?"
                +"itemheadline="+data.getItemHeadline()
                +"&itemdescription="+data.getBeskrivelse()
                +"&itemrecieved="+data.getRecieveDate()
                +"&itemdatafrom="+data.getDatingFrom()
                +"&itemdatingto="+data.getDatingTo()
                +"&donator="+data.getRefDonator()
                +"&producer="+data.getRefProducer()
                +"&postnummer="+data.getGeoArea());

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);


            urlConnection.connect();
            int response = urlConnection.getResponseCode();
            Log.d("Server response ----->", "The response is: " + response);


            //InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            // return readStream(in);

        } finally {
            urlConnection.disconnect();
        }


    }

    public void updateItem(registreringsDTO data) {


    }

    public void deleteItem() {

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

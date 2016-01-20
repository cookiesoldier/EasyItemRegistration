package com.example.martinvieth.easyitemregistration;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin Vieth on 18-01-2016.
 */
public class EIRApplication extends Application {

    private RegistreringsDTO savedData;
    private List<String> selectedImages = new ArrayList<>();

    /**
     * Denne metode skal checke om der i shared preferences er gemt noget data, hvis der er så sæt det ind i
     * savedData og selectedImages
     */
    public void checkForData() {
        SharedPreferences sp = getSharedPreferences("DTO", 0);

        JSONObject jsonData = new JSONObject();

        Log.d("CheckForData -->",sp.getString("data","no name defined"));
        try {
            jsonData = new JSONObject(sp.getString("data", "no name defined"));



        savedData = new RegistreringsDTO(
                jsonData.get("itemnr").toString(),
                jsonData.get("itemheadline").toString(),
                jsonData.get("itemdescription").toString(),
                jsonData.get("itemrecieved").toString(),
                jsonData.get("itemdatingfrom").toString(),
                jsonData.get("itemdatingto").toString(),
                jsonData.get("donator").toString(),
                jsonData.get("producer").toString(),
                jsonData.get("postnummer").toString(), new ArrayList<String>(), new ArrayList<Uri>());

            JSONArray images = jsonData.getJSONArray("images");
            int i = 0;
            while(images.opt(i) != null){
                selectedImages.add(images.opt(i).toString());
                i++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveEverything(){
        //Først gemmer vi registrerings DTO
        SharedPreferences sp = getSharedPreferences("DTO", 0);
        SharedPreferences.Editor mEdit1 = sp.edit();
        // Vi vil gerne gemme en liste af strings samt hvert punkt i savedData
        //Vi kunne gøre det med et JSONobject
        //Først tilføjer vi hvert af DTO'ens punkter til et Jsonobjekt
        JSONObject dataDTO =new JSONObject();
        try {
            if(savedData.getItemNr() != null){
                dataDTO.put("itemnr",savedData.getItemNr());
            }

            dataDTO.put("itemheadline",savedData.getItemHeadline());
            dataDTO.put("itemdescription",savedData.getBeskrivelse());
            dataDTO.put("itemrecieved",savedData.getRecieveDate());
            dataDTO.put("itemdatingfrom",savedData.getDatingFrom());
            dataDTO.put("itemdatingto",savedData.getDatingTo());
            dataDTO.put("donator",savedData.getRefDonator());
            dataDTO.put("producer",savedData.getRefProducer());
            dataDTO.put("postnummer",savedData.getGeoArea());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            dataDTO.put("images", new JSONArray(selectedImages));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mEdit1.putString("data",dataDTO.toString());
        mEdit1.commit();
        Log.d("JsonObject check --->", dataDTO.toString());

    }

    public List<String> getSelectedImages() {
        return selectedImages;
    }

    public void setSelectedImages(List<String> selectedImages) {
        this.selectedImages = selectedImages;
    }



   public RegistreringsDTO getSavedData(){
        return savedData;
    }

    public void setSavedData(RegistreringsDTO data){
        savedData = data;

    }



}

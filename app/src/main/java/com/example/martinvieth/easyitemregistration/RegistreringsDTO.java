package com.example.martinvieth.easyitemregistration;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.List;

/**
 * Created by Martin Vieth on 18-11-2015.
 * Tanken er at en RegistreringsDTO kommer til at indeholde de dublinCore information, billeder og lydfiler som skal sendes til databasen,
 * ved at lægge det hele ind i en DTO før vi prøver at sende det videre sikrer vi at der er bedre kontrol over hvad der bliver sendt og vi
 * sikrer os det rigtige bliver sendt. Det smart!
 */
public class RegistreringsDTO {




    private String itemNr = "-1";
    private String itemHeadline;
    private String beskrivelse;

    private  String recieveDate;
    private  String datingFrom;
    private  String datingTo;

    private  String refDonator;
    private  String refProducer;
    //er en small int??
    private String geoArea;

    private List<String> images;
    private List<Uri> audio;


    public RegistreringsDTO(String itemHeadline, String beskrivelse){
        this.itemHeadline = itemHeadline;
        this.beskrivelse = beskrivelse;
    }

    /**
     *
     * @param itemHeadline
     * @param beskrivelse
     * @param recieveDate
     * @param datingFrom
     * @param datingTo
     * @param refDonator
     * @param refProducer
     * @param geoArea
     * @param images
     */
    public RegistreringsDTO(String itemHeadline, String beskrivelse, String recieveDate,
                            String datingFrom, String datingTo, String refDonator,
                            String refProducer, String geoArea, List<String> images, List<Uri> audio){
        this.itemHeadline = itemHeadline;
        this.beskrivelse = beskrivelse;
        this.recieveDate = recieveDate;
        this.datingFrom = datingFrom;
        this.datingTo = datingTo;
        this.refDonator = refDonator;
        this.refProducer = refProducer;
        this.geoArea = geoArea;
        this.images = images;
        this.audio = audio;

    }

    /**
     * This one is for objects gotten from the database with itemNr and all.
     * @param itemNr
     * @param itemHeadline
     * @param beskrivelse
     * @param recieveDate
     * @param datingFrom
     * @param datingTo
     * @param refDonator
     * @param refProducer
     * @param geoArea
     * @param images
     */
    public RegistreringsDTO(String itemNr, String itemHeadline, String beskrivelse, String recieveDate,
                            String datingFrom, String datingTo, String refDonator,
                            String refProducer, String geoArea, List<String> images, List<Uri> audio){
        this.itemNr = itemNr;
        this.itemHeadline = itemHeadline;
        this.beskrivelse = beskrivelse;
        this.recieveDate = recieveDate;
        this.datingFrom = datingFrom;
        this.datingTo = datingTo;
        this.refDonator = refDonator;
        this.refProducer = refProducer;
        this.geoArea = geoArea;
        this.images = images;
        this.audio = audio;

    }
    public RegistreringsDTO(String itemNr,RegistreringsDTO data){
        this.itemNr = itemNr;
        this.itemHeadline = data.getItemHeadline();
        this.beskrivelse = data.getBeskrivelse();
        this.recieveDate = data.getRecieveDate();
        this.datingFrom = data.getDatingFrom();
        this.datingTo = data.getDatingTo();
        this.refDonator = data.getRefDonator();
        this.refProducer = data.getRefProducer();
        this.geoArea = data.getGeoArea();
        this.images = data.getImages();
        this.audio = data.getAudio();
    }


    public String getItemNr() {
        return itemNr;
    }
    public String getItemHeadline() {
        return itemHeadline;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public String getRecieveDate() {
        return recieveDate;
    }

    public String getDatingFrom() {
        return datingFrom;
    }

    public String getDatingTo() {
        return datingTo;
    }

    public String getRefDonator() {
        return refDonator;
    }

    public String getRefProducer() {
        return refProducer;
    }

    public String getGeoArea() {
        return geoArea;
    }

    public List<String> getImages() {
        return images;
    }

    public List<Uri> getAudio() { return audio; }

}

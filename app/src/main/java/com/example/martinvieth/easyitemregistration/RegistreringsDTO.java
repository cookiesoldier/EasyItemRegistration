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




  private String itemNr;
    private String itemHeadline;
    private String beskrivelse;
    //burde være et timestamp ifølge databasen
    private  String recieveDate;
    private  String datingFrom;
    private  String datingTo;

    private  String refDonator;
    private  String refProducer;
    //er en small int??
    private String geoArea;
    //bruges ikke endu
    private List<Uri> images;

    public RegistreringsDTO(String itemHeadline, String beskrivelse){
        this.itemHeadline = itemHeadline;
        this.beskrivelse = beskrivelse;
    }

    public RegistreringsDTO(String itemHeadline, String beskrivelse, String recieveDate,
                            String datingFrom, String datingTo, String refDonator,
                            String refProducer, String geoArea, List<Uri> images){
        this.itemHeadline = itemHeadline;
        this.beskrivelse = beskrivelse;
        this.recieveDate = recieveDate;
        this.datingFrom = datingFrom;
        this.datingTo = datingTo;
        this.refDonator = refDonator;
        this.refProducer = refProducer;
        this.geoArea = geoArea;
        this.images = images;
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
                            String refProducer, String geoArea, List<Uri> images){
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

    public List<Uri> getImages() {
        return images;
    }

}

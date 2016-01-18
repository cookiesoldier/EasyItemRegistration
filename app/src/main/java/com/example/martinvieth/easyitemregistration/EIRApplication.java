package com.example.martinvieth.easyitemregistration;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin Vieth on 18-01-2016.
 */
public class EIRApplication extends Application {

    private RegistreringsDTO savedData;
    private List<String> selectedImages = new ArrayList<>();




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

package com.example.martinvieth.easyitemregistration;

import android.app.Application;

/**
 * Created by Martin Vieth on 18-01-2016.
 */
public class EIRApplication extends Application {

    RegistreringsDTO savedData;

   public RegistreringsDTO getSavedData(){
        return savedData;
    }

    public void setSavedData(RegistreringsDTO data){
        savedData = data;

    }


}

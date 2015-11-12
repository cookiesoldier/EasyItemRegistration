package com.example.martinvieth.easyitemregistration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class FrontPageActivity extends Activity implements View.OnClickListener{

    ImageButton menuButton;
    ImageButton searchButton;
    ImageButton cameraFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_frontpage);


        menuButton = (ImageButton) findViewById(R.id.imageButtonMenuIcon);
        searchButton = (ImageButton) findViewById(R.id.imageButtonSearch);
        cameraFolder = (ImageButton) findViewById(R.id.imageButtonCamerafolder);


        menuButton.setImageResource(R.drawable.ic_burgermenu);
        searchButton.setImageResource(R.drawable.ic_search);
        cameraFolder.setImageResource(R.drawable.ic_camerafolder);

        cameraFolder.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if(v == cameraFolder){
            System.out.println("abc abc abc");
            startActivity(new Intent(FrontPageActivity.this, ImageGalleryActivity.class));

        }


    }
}

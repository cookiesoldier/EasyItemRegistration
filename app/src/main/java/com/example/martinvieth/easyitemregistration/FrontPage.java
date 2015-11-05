package com.example.martinvieth.easyitemregistration;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    ImageButton MenuButton;
    ImageButton SearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        MenuButton = (ImageButton) findViewById(R.id.imageButtonMenuIcon);
        SearchButton = (ImageButton) findViewById(R.id.imageButtonSearch);
        MenuButton.setImageResource(R.drawable.ic_burgermenu);
        SearchButton.setImageResource(R.drawable.ic_search);
    }

   
}

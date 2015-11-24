package com.example.martinvieth.easyitemregistration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemListActivity extends Activity  {


    ListView itemsListing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_item_list);
        Intent intentExtras = getIntent();
        itemsListing = (ListView) findViewById(R.id.lVItems);


        ArrayList<String> itemsParsed =intentExtras.getStringArrayListExtra("data");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, itemsParsed);

        // Assign adapter to ListView
        itemsListing.setAdapter(adapter);

        itemsListing.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) itemsListing.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();
                Intent intent = new Intent();
                intent.putExtra("seletedItem",itemValue);
                setResult(RESULT_OK, intent);

                finish();
            }

        });
    }



}
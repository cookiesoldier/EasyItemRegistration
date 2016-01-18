package com.example.martinvieth.easyitemregistration;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemListActivity extends Activity {


    ListView itemsListing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_item_list);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intentExtras = getIntent();

        //itemsListing = (ListView) findViewById(R.id.lVItems);

        ArrayList<String> itemsParsed = intentExtras.getStringArrayListExtra("data");
        final ListView listView = (ListView) findViewById(R.id.lVItems);
        listView.setAdapter(new CustomListAdapter2(this, itemsParsed));

            System.out.println("listens længde: " + itemsParsed.size());

            // Assign adapter to ListView


            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // ListView Clicked item index
                    int itemPosition = position;

                    // ListView Clicked item value
                    String itemValue = (String) listView.getItemAtPosition(position);

                    // Show Alert
                    Toast.makeText(getApplicationContext(),
                            "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                            .show();
                    Intent intent = new Intent();
                    intent.putExtra("seletedItem", itemValue);
                    setResult(RESULT_OK, intent);

                    finish();
                }

            });

    }
}


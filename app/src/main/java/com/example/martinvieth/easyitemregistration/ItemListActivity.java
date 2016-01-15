package com.example.martinvieth.easyitemregistration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpStatus;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ItemListActivity extends Activity {


    ListView itemsListing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_item_list);

        Intent intentExtras = getIntent();

        //itemsListing = (ListView) findViewById(R.id.lVItems);

        ArrayList<String> itemsParsed = intentExtras.getStringArrayListExtra("data");
        final ListView listView = (ListView) findViewById(R.id.lVItems);
        listView.setAdapter(new CustomListAdapter(this, itemsParsed));

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


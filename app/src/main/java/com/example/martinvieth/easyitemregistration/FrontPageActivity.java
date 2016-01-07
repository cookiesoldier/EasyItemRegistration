package com.example.martinvieth.easyitemregistration;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.media.MediaRecorder;
import android.media.MediaPlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FrontPageActivity extends Activity implements View.OnClickListener {

    public static final int IMAGE_CAPTURE = 42;
    public static final int IMAGE_SELECT = 43;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int ITEMLIST_CHOSEN = 100;
    final databaseDAO dataDAO = new databaseDAO();
    private Uri fileUri;

    ImageButton btnMenu;
    ImageButton btnSearch;

    ImageButton btnRecorder;
    ImageButton btnGalleryPhoto;
    ImageButton btnGotoCamera;
    ImageButton btnAccept;
    ImageButton getBtnSearch;

    ImageView photoThumb1;
    ImageView photoThumb2;
    ImageView photoThumb3;

    EditText edtItemHeadline;
    EditText edtBeskrivelse;
    EditText edtRecieveDate;
    EditText edtDatingFrom;
    EditText edtDatingTo;
    EditText edtRefDonator;
    EditText edtTextRefProducer;
    EditText edtGeoArea;


    //Int som vi bruger til at bestemme itemNR til opdatering af genstand, hvis den er -1 så opdaterer vi ikke men laver et nyt item istedet.
    int itemNrDeterminer;

    //De valgte billeder
    List<Bitmap> acceptedImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_frontpage);

        /*
        btnMenu = (ImageButton) findViewById(R.id.imageButtonMenuIcon);
        btnSearch = (ImageButton) findViewById(R.id.imageButtonSearch);
        btnMenu.setImageResource(R.drawable.ic_burgermenu);
        btnSearch.setImageResource(R.drawable.ic_search);
        */

        btnGalleryPhoto = (ImageButton) findViewById(R.id.imageButtonCamerafolder);
        btnAccept = (ImageButton) findViewById(R.id.imageButtonDone);

        btnGotoCamera = (ImageButton) findViewById(R.id.imageButtonCamera);
        btnGotoCamera.setOnClickListener(this);

        btnSearch = (ImageButton) findViewById(R.id.imageButtonSearch);
        btnSearch.setOnClickListener(this);

        btnRecorder = (ImageButton) findViewById(R.id.imageButtonRecorder);
        btnRecorder.setOnClickListener(this);

        photoThumb1 = (ImageView) findViewById(R.id.photoThumb);


        btnGalleryPhoto.setImageResource(R.drawable.ic_camerafolder);


        edtItemHeadline = (EditText) findViewById(R.id.EditTextItemHeadline);
        edtBeskrivelse = (EditText) findViewById(R.id.editTextBeskrivelse);
        edtRecieveDate = (EditText) findViewById(R.id.EditTextRecieveDate);
        edtDatingFrom = (EditText) findViewById(R.id.editTextDatingFrom);
        edtDatingTo = (EditText) findViewById(R.id.editTextDatingTo);
        edtRefDonator = (EditText) findViewById(R.id.editTextRef_Donator);
        edtTextRefProducer = (EditText) findViewById(R.id.editTextRef_Producer);
        edtGeoArea = (EditText) findViewById(R.id.editTextGeoArea);

        btnGalleryPhoto.setOnClickListener(this);
        btnAccept.setOnClickListener(this);


        Timestamp tsTemp = new Timestamp(System.currentTimeMillis());
        edtRecieveDate.setText(tsTemp.toString());
        edtDatingFrom.setText(tsTemp.toString());
        edtDatingTo.setText(tsTemp.toString());
        findViewById(R.id.imageButtonRecorder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FrontPageActivity.this, AudioRecorder.class));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu); // standard menuer
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

        if ( v == btnRecorder) {

            //TODO

        }


        if (v == btnGalleryPhoto) {


            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_SELECT);
        }
        if (v == btnGotoCamera) {

            Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(captureImageIntent, IMAGE_CAPTURE);
        }
        if (v == btnAccept) {

            new AsyncTask() {
                @Override
                protected Object doInBackground(Object... executeParametre) {
                    try {


                        if (dataDAO.createItem(getDataAndFiles(-1))) {
                            return "succes";
                        } else {
                            return "failed";
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "færdig!";  // <5>
                }

                @Override
                protected void onProgressUpdate(Object... progress) {

                }

                @Override
                protected void onPostExecute(Object result) {
                    //inform user item was added and delete the data and files so new can be added or if it failed
                    if (result.equals("succes")) {
                        deleteDataAndFiles();
                        Toast.makeText(getApplicationContext(), "Succes:Added item!!", Toast.LENGTH_LONG).show();
                    } else if (result.equals("failed")) {
                        Toast.makeText(getApplicationContext(), "Failed to add item!!", Toast.LENGTH_LONG).show();


                    }

                }
            }.execute(100);
        }

        //hvis vi trykker hurtigt kan vi starte 2 async tasks, nok ikke så godt. :)

        if (v == btnSearch) {

            new AsyncTask() {
                String items;

                @Override
                protected Object doInBackground(Object... executeParametre) {
                    try {
                        //   Log.d("Server response ----->", "The response" + (items = dataDAO.itemList()));

                        items = dataDAO.itemList();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "færdig!";  // <5>
                }

                @Override
                protected void onProgressUpdate(Object... progress) {

                }

                @Override
                protected void onPostExecute(Object result) {

                    Intent itemListActivity = new Intent(FrontPageActivity.this, ItemListActivity.class);
                    itemListActivity.putStringArrayListExtra("data", ItemListParse(items));
                    startActivityForResult(itemListActivity, ITEMLIST_CHOSEN);
                }
            }.execute(100);

        }
    }



    private ArrayList<String> ItemListParse(String items) {
        //Log.d("Parsing items -->", "Step1" + (items));
        String[] itemUnparsed;
        itemUnparsed = items.substring(3).split("detailsuri");
        //Log.d("Server response ----->", "Step2 " + itemUnparsed.length);
        ArrayList<String> itemsParsed = new ArrayList<>();
        for (int i = 1; i < itemUnparsed.length; i++) {
            String s = itemUnparsed[i];
            //  Log.d("Server response ----->", "Step2 " + itemUnparsed[i]);
            s = s.substring(13, s.length() - 4);
            s = s.replace("\"itemid\":", "");
            if (s.startsWith(",")) {
                s = s.substring(1, s.length());
            }
            s = s.replace(",\"itemheadline\":\"", " ");
            s = s.substring(0, s.length() - 1);
            //Log.d("Server response ----->", "Step3 " + s);
            itemsParsed.add(s);


        }
        Log.d("Server response ----->", "Step4 " + itemsParsed);
        return itemsParsed;


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        List<Bitmap> result = new ArrayList<>();
        switch (requestCode) {
            case IMAGE_CAPTURE:
                //Bitmap img = (Bitmap) data.getExtras().get("data");
                try {
                    result.add(MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri));
                } catch (IOException e) {
                    Log.d("Error", "Could not find image file in storage.");
                }
                photoThumb1.setImageBitmap(result.get(0));
                break;

            case IMAGE_SELECT:
                AssetFileDescriptor fileDS = null;
                try {
                    fileDS = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (fileDS != null) {
                    try {

                        result.add(BitmapFactory.decodeStream(fileDS.createInputStream()));
                    } catch (IOException e) {
                        Log.d("Error", "Could not load selected image(s)");
                    }
                }
                photoThumb1.setImageBitmap(result.get(0));
                acceptedImages.addAll(result);
                break;
            case ITEMLIST_CHOSEN:
                String p = data.getExtras().getString("seletedItem");
                Log.d("Server response ----->", "Step1000 " + p);
                //okay vi laver en variabel, hvis den er -1 så opretter vi et nyt item, ellers er den det itemNR vi opdaterer.
                itemNrDeterminer = Integer.parseInt(p.split(" ")[0]);
                insertDataFromChosenItem(itemNrDeterminer);
                break;
            default:
                Log.d("Error", "Der kom et svar i onActivityResult der ikke er taget højde for." + requestCode);
        }

    }

    private void insertDataFromChosenItem(final int itemNrDeterminer2) {

        new AsyncTask() {
            final int itemNr = itemNrDeterminer2;

            @Override
            protected Object doInBackground(Object... executeParametre) {
                try {
                    Log.d("reply database", "svar fra database." + dataDAO.getItem(itemNr));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "færdig!";
            }

            @Override
            protected void onProgressUpdate(Object... progress) {

            }

            @Override
            protected void onPostExecute(Object result) {


            }
        }.execute(100);

    }

    /**
     * Hugget fra http://developer.android.com/guide/topics/media/camera.html#saving-media
     */

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "EIR.Media");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("EIR.Media", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void deleteDataAndFiles() {
        edtItemHeadline.setText("");
        edtItemHeadline.setText("");
        edtBeskrivelse.setText("");
        edtRecieveDate.setText("");
        edtDatingFrom.setText("");
        edtDatingTo.setText("");
        edtRefDonator.setText("");
        edtTextRefProducer.setText("");
        edtGeoArea.setText("");
        acceptedImages.clear();

        Timestamp tsTemp = new Timestamp(System.currentTimeMillis());
        edtRecieveDate.setText(tsTemp.toString());
        edtDatingFrom.setText(tsTemp.toString());
        edtDatingTo.setText(tsTemp.toString());

    }

    private RegistreringsDTO getDataAndFiles(int itemNr) {

        RegistreringsDTO registrering;

        if (itemNr == -1) {
            registrering = new RegistreringsDTO(edtItemHeadline.getText().toString(),
                    edtBeskrivelse.getText().toString(),
                    edtRecieveDate.getText().toString(),
                    edtDatingFrom.getText().toString(),
                    edtDatingTo.getText().toString(),
                    edtRefDonator.getText().toString(),
                    edtTextRefProducer.getText().toString(),
                    edtGeoArea.getText().toString(),
                    acceptedImages);
        } else {
            registrering = new RegistreringsDTO(
                    Integer.toString(itemNr),
                    edtItemHeadline.getText().toString(),
                    edtBeskrivelse.getText().toString(),
                    edtRecieveDate.getText().toString(),
                    edtDatingFrom.getText().toString(),
                    edtDatingTo.getText().toString(),
                    edtRefDonator.getText().toString(),
                    edtTextRefProducer.getText().toString(),
                    edtGeoArea.getText().toString(),
                    acceptedImages);
        }


        return registrering;
    }

}

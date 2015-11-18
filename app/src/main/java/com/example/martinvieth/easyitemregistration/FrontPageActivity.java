package com.example.martinvieth.easyitemregistration;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FrontPageActivity extends Activity implements View.OnClickListener {

    public static final int IMAGE_CAPTURE = 42;
    public static final int IMAGE_SELECT = 43;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Uri fileUri;

    ImageButton btnMenu;
    ImageButton btnSearch;

    ImageButton btnSelectGalleryPhoto;
    ImageButton btnGotoCamera;
    ImageButton btnAccept;

    ImageView photoThumb1;
    ImageView photoThumb2;
    ImageView photoThumb3;

    EditText edtItemNr;
    EditText edtBetegnelse;
    EditText edtRecieveDate;
    EditText edtDatingFrom;
    EditText edtDatingTo;
    EditText edtDescription;
    EditText edtRefDonator;
    EditText edtTextRefProducer;
    EditText edtGeoArea;
    EditText edtItemSubjects;

    //De valgte billeder
    List<Bitmap> acceptedImages= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_frontpage);

        /*
        btnMenu = (ImageButton) findViewById(R.id.imageButtonMenuIcon);
        btnSearch = (ImageButton) findViewById(R.id.imageButtonSearch);
        btnMenu.setImageResource(R.drawable.ic_burgermenu);
        btnSearch.setImageResource(R.drawable.ic_search);
        */

        btnSelectGalleryPhoto = (ImageButton) findViewById(R.id.imageButtonCamerafolder);
        btnAccept = (ImageButton) findViewById(R.id.imageButtonDone);

        btnGotoCamera = (ImageButton) findViewById(R.id.imageButtonCamera);
        btnGotoCamera.setOnClickListener(this);

        photoThumb1 = (ImageView) findViewById(R.id.photoThumb);


        btnSelectGalleryPhoto.setImageResource(R.drawable.ic_camerafolder);




        edtItemNr = (EditText) findViewById(R.id.EditTextItemNr);
        edtBetegnelse = (EditText) findViewById(R.id.editTextBetegnelse);
        edtRecieveDate = (EditText) findViewById(R.id.EditTextRecieveDate);
        edtDatingFrom = (EditText) findViewById(R.id.editTextDatingFrom);
        edtDatingTo = (EditText) findViewById(R.id.editTextDatingTo);
        edtDescription = (EditText) findViewById(R.id.editTextDescription);
        edtRefDonator = (EditText) findViewById(R.id.editTextRef_Donator);
        edtTextRefProducer = (EditText) findViewById(R.id.editTextRef_Producer);
        edtGeoArea = (EditText) findViewById(R.id.editTextGeoArea);
        edtItemSubjects = (EditText) findViewById(R.id.editTextItemSubjects);

        btnSelectGalleryPhoto.setOnClickListener(this);
        btnAccept.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu); // standard menuer
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onClick(View v) {
        if (v == btnSelectGalleryPhoto) {
            /*
            System.out.println("abc abc abc");
            startActivity(new Intent(FrontPageActivity.this, ImageGalleryActivity.class));
            */

            /*
            Intent selectImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(selectImageIntent, IMAGE_SELECT);
            */

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), IMAGE_SELECT);
        }
        if (v == btnGotoCamera) {
            /*
            System.out.println("Der blev trykket på gotocamera");
            Intent gotocameraIntent = new Intent(this, CameraActivity2.class);
            System.out.println("Intent til kamera blev oprettet");
            startActivity(gotocameraIntent);
            */
            Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(captureImageIntent, IMAGE_CAPTURE);
        }
        if(v == btnAccept){
          registreringsDTO registrering =  new registreringsDTO(edtItemNr.getText().toString(),
                                                                edtBetegnelse.getText().toString(),
                                                                edtRecieveDate.getText().toString(),
                                                                edtDatingFrom.getText().toString(),
                                                                edtDatingTo.getText().toString(),
                                                                edtDescription.getText().toString(),
                                                                edtRefDonator.getText().toString(),
                                                                edtTextRefProducer.getText().toString(),
                                                                edtGeoArea.getText().toString(),
                                                                edtItemSubjects.getText().toString(),
                                                                acceptedImages);


        }
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
            default:
                Log.d("Error", "Der kom et svar i onActivityResult der ikke er taget højde for.");
        }

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

}

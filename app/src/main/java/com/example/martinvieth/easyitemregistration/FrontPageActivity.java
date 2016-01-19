package com.example.martinvieth.easyitemregistration;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FrontPageActivity extends Activity implements View.OnClickListener {

    public static final int IMAGE_CAPTURE = 42;
    public static final int IMAGE_SELECT = 43;
    public static final int AUDIO_CAPTURE = 44;
    public static final int AUDIO_SELECT = 45;
    public static final int MEDIA_TYPE_AUDIO = 3;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int ITEMLIST_CHOSEN = 100;
    //Context c = getApplicationContext();
    final DatabaseDAO2 dataDAO = new DatabaseDAO2(this);
    private Uri fileUri;
    Calendar myCalendar = Calendar.getInstance();
    ImageView btnSearch;

    ImageView btnRecorder;
    ImageView btnGalleryPhoto;
    ImageView btnGotoCamera;
    ImageView btnAccept;

    EditText edtItemHeadline;
    EditText edtBeskrivelse;
    EditText edtRecieveDate;
    EditText edtDatingFrom;
    EditText edtDatingTo;
    EditText edtRefDonator;
    EditText edtTextRefProducer;
    EditText edtGeoArea;

    TextView btnCancel;

    LinearLayout myGallery;

    private ProgressDialog progress;

    //Int som vi bruger til at bestemme itemNR til opdatering af genstand, hvis den er -1 så opdaterer vi ikke men laver et nyt item istedet.
    int itemNrDeterminer = -1;

    //De valgte billeder fra enten camera eller galleri
    List<String> selectedImages = new ArrayList<>();
    //
    //De valgt optagelser
    List<Uri> selectedAudio = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_frontpage);

        //Burde gøre så skærmen ikke kan rotere
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnGalleryPhoto = (ImageView) findViewById(R.id.imageButtonCamerafolder);
        btnAccept = (ImageView) findViewById(R.id.imageButtonDone);

        btnGotoCamera = (ImageView) findViewById(R.id.imageButtonCamera);
        btnGotoCamera.setOnClickListener(this);

        btnSearch = (ImageView) findViewById(R.id.imageButtonSearch);
        btnSearch.setOnClickListener(this);

        btnRecorder = (ImageView) findViewById(R.id.imageButtonRecorder);
        btnRecorder.setOnClickListener(this);

        btnCancel = (TextView) findViewById(R.id.imageButtonCancel);
        btnCancel.setOnClickListener(this);

        btnGalleryPhoto.setImageResource(R.mipmap.ic_photo_library);

        edtItemHeadline = (EditText) findViewById(R.id.EditTextItemHeadline);
        edtBeskrivelse = (EditText) findViewById(R.id.editTextBeskrivelse);
        edtRecieveDate = (EditText) findViewById(R.id.EditTextRecieveDate);
        edtDatingFrom = (EditText) findViewById(R.id.editTextDatingFrom);
        edtDatingTo = (EditText) findViewById(R.id.editTextDatingTo);
        edtRefDonator = (EditText) findViewById(R.id.editTextRef_Donator);
        edtTextRefProducer = (EditText) findViewById(R.id.editTextRef_Producer);
        edtGeoArea = (EditText) findViewById(R.id.editTextGeoArea);

        //Gør man ikke kan skrive i de 3 fleter med datoer fra datepicker.
        edtRecieveDate.setFocusable(false);
        edtDatingFrom.setFocusable(false);
        edtDatingTo.setFocusable(false);

        btnGalleryPhoto.setOnClickListener(this);
        btnAccept.setOnClickListener(this);
        edtRecieveDate.setOnClickListener(this);
        edtDatingFrom.setOnClickListener(this);
        edtDatingTo.setOnClickListener(this);

        myGallery = (LinearLayout) findViewById(R.id.mygallery);

    }


    @Override
    protected void onStart() {
        super.onStart();

        EIRApplication EIRapp = (EIRApplication) getApplication();
        EIRapp.checkForData();


    }

    protected void onResume() {
        super.onResume();
        dismissLoadingDialog();

        EIRApplication EIRapp = (EIRApplication) getApplication();
        if (EIRapp.getSelectedImages() != null && EIRapp.getSavedData() != null) {
            selectedImages = EIRapp.getSelectedImages();
            selectedImagesShow();
            RegistreringsDTO dataDTO = EIRapp.getSavedData();
            updateFieldsOnResume(dataDTO);
        } else {
            Log.d("No previous data found", "NADADADADA");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        EIRApplication EIRapp = (EIRApplication) getApplication();
        EIRapp.setSavedData(getDataAndFiles(itemNrDeterminer));
        EIRapp.setSelectedImages(selectedImages);

    }

    @Override
    protected void onStop() {
        super.onStop();

        EIRApplication EIRapp = (EIRApplication) getApplication();
        EIRapp.saveEverything();
    }

    private void updateFieldsOnResume(RegistreringsDTO dataDTO) {
        if (dataDTO.getItemNr() != null) {
            itemNrDeterminer = Integer.parseInt(dataDTO.getItemNr());
        } else {
            itemNrDeterminer = -1;
        }
        edtItemHeadline.setText(dataDTO.getItemHeadline());
        edtBeskrivelse.setText(dataDTO.getBeskrivelse());
        edtRecieveDate.setText(dataDTO.getRecieveDate());
        edtDatingFrom.setText(dataDTO.getDatingFrom());
        edtDatingTo.setText(dataDTO.getDatingTo());
        edtRefDonator.setText(dataDTO.getRefDonator());
        edtTextRefProducer.setText(dataDTO.getRefProducer());
        edtGeoArea.setText(dataDTO.getGeoArea());
    }


    public void selectedImagesShow() {
        myGallery.removeAllViews();

        for (String paths : selectedImages) {
            LinearLayout layout = new LinearLayout(getApplicationContext());
            layout.setLayoutParams(new LinearLayout.LayoutParams(250, 250));
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(220, 220));
            //imageView.getLayoutParams().width = 120;
            Picasso.with(this).load(paths).placeholder(R.drawable.ic_placeholder).fit().into(imageView);
            layout.addView(imageView);
            myGallery.addView(layout);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu); // standard menuer
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private void updateLabel(int label) {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMAN);

        if (label == 1) {
            edtRecieveDate.setText(sdf.format(myCalendar.getTime()));
            Log.d("editRecieveDate", edtRecieveDate.getText().toString());
        }
        if (label == 2) {
            edtDatingFrom.setText(sdf.format(myCalendar.getTime()));
            Log.d("editDatingFrom", edtDatingFrom.getText().toString());
        }
        if (label == 3) {
            edtDatingTo.setText(sdf.format(myCalendar.getTime()));
            Log.d("edtDatingTo", edtDatingTo.getText().toString());
        }
    }

    @Override
    public void onClick(View v) {

        if (v == btnCancel) {
            AlertDialog alertDialog = new AlertDialog.Builder(FrontPageActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Vil du ryde felterne");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteDataAndFiles();
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Annuller",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
        if (v == edtRecieveDate) {
            getSetDate(1);

            Log.d("edtRecieveData", edtRecieveDate.getText().toString());
        }
        if (v == edtDatingFrom) {
            getSetDate(2);
            Log.d("edtRecieveData", edtDatingFrom.getText().toString());
        }
        if (v == edtDatingTo) {
            getSetDate(3);
            Log.d("edtRecieveData", edtDatingTo.getText().toString());
        }

        if (v == btnRecorder) {

            Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            startActivityForResult(intent, AUDIO_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_AUDIO);

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
            if (edtItemHeadline.getText().length() > 0) {
                if (onDateCheck()) {
                    if (isNetworkAvailable()) {
                        new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object... executeParametre) {
                                try {
                                    if (itemNrDeterminer == -1) {
                                        if (dataDAO.createItem(getDataAndFiles(itemNrDeterminer))) {
                                            return "succes";
                                        } else {
                                            return "failed";
                                        }

                                    } else {

                                        if (dataDAO.updateItem(getDataAndFiles(itemNrDeterminer))) {
                                            return "succes";
                                        } else {
                                            return "failed";
                                        }
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
                                    Toast.makeText(getApplicationContext(), "Gemt succesfuldt", Toast.LENGTH_LONG).show();
                                } else if (result.equals("failed")) {
                                    Toast.makeText(getApplicationContext(), "Kunne ikke Gemme", Toast.LENGTH_LONG).show();


                                }
                                itemNrDeterminer = -1;

                            }
                        }.execute(100);

                    } else {
                        Toast.makeText(getApplicationContext(), "Der er ingen netværks forbindelse. Prøv igen!", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), " Fejl i Datoen: Ret venligst Datering Fra og Datering Til", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Indtast venligst overskrift!", Toast.LENGTH_SHORT).show();
            }
        }


        //hvis vi trykker hurtigt kan vi starte 2 async tasks, nok ikke så godt. :)


        if (v == btnSearch) {
            showLoadingDialog();
            if (isNetworkAvailable()) {
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
                        try {
                            System.out.print("trying...");
                            ArrayList<String> a = ItemListParse(items);
                            System.out.println("items : " + a.size());
                            itemListActivity.putStringArrayListExtra("data", a);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivityForResult(itemListActivity, ITEMLIST_CHOSEN);
                    }
                }.execute(100);

            } else {
                dismissLoadingDialog();
                Toast.makeText(getApplicationContext(), "Der er ingen netværks forbindelse. Prøv igen!", Toast.LENGTH_LONG).show();

            }

        }
    }

    private void getSetDate(final int labelNr) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(labelNr);
                Log.d("edtRecieveDate", edtRecieveDate.getText().toString());

            }

        };


        new DatePickerDialog(FrontPageActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();


    }

    private ArrayList<String> ItemListParse(String items) throws JSONException {

        Log.d("Parsing items -->", "Step1" + (items));
        //Vi prøver at gøre det med json objekter istedet.
        //først lægger vi string ind i et json object


        JSONArray jsonArrayData = new JSONArray(items);
        //System.out.println("ItemListParse(): jsonarray " + jsonArrayData.length());
        ArrayList<String> itemsParsed = new ArrayList<>();
        for (int i = 0; i < jsonArrayData.length(); i++) {
            final JSONObject dataPoint = jsonArrayData.getJSONObject(i);


            itemsParsed.add(dataPoint.get("itemid") + " " + dataPoint.get("itemheadline") + " " + dataPoint.get("defaultimage"));

            // itemsParsed.add(jsonArrayData.getJSONObject(i).toString());
            // Log.d("JsonObjects ----->", jsonArrayData.getJSONObject(i).toString());
        }


        return itemsParsed;


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case AUDIO_CAPTURE:
                selectedAudio.add(fileUri);
                Log.d("Audiorecording: ", data.getExtras().toString());
                Log.d("Audio-info ", fileUri.toString());
                break;

            case IMAGE_CAPTURE:
                selectedImages.add(fileUri.toString());

                selectedImagesShow();

                break;

            case IMAGE_SELECT:
                String abc = data.toString();
                Log.d("Pre multi img check-->", abc);
                if (data.getClipData() != null) {
                    ClipData clip = data.getClipData();
                    for (int i = 0; i < clip.getItemCount(); i++) {
                        ClipData.Item item = clip.getItemAt(i);
                        Uri uri = item.getUri();
                        //Indsæt uri i liste
                        Log.d("URI check ----->", uri.toString());
                        selectedImages.add(uri.toString());
                    }
                } else {
                    Log.d("URI check -----> ", data.getData().toString());
                    selectedImages.add(data.getData().toString());
                }
                selectedImagesShow();
                break;
            case ITEMLIST_CHOSEN:
                String p = data.getExtras().getString("seletedItem");
                Log.d("Choosen item  ----->", p);
                //okay vi laver en variabel, hvis den er -1 så opretter vi et nyt item, ellers er den det itemNR vi opdaterer.

                itemNrDeterminer = Integer.parseInt(p.split(" ")[0]);
                insertDataFromChosenItem(itemNrDeterminer);
                break;
            default:
                Log.d("Error", "Der kom et svar i onActivityResult der ikke er taget højde for." + requestCode);
        }

    }

    private void insertDataFromChosenItem(final int itemNrDeter) {

        new AsyncTask() {

            final int itemNr = itemNrDeter;
            String itemData;

            @Override
            protected Object doInBackground(Object... executeParametre) {
                try {
                    itemData = dataDAO.getItem(itemNr);
                    Log.d("reply database", "svar fra database." + itemData);
                    //Så skal vi sætte data ind i vores registreringsDTO som opbevarer den data vi arbejder med nu.


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
                deleteDataAndFiles();
                JSONObject jsonData = new JSONObject();
                try {
                    jsonData = new JSONObject(itemData);

                    itemNrDeterminer = itemNr;
                    edtItemHeadline.setText(jsonData.get("itemheadline").toString());
                    edtBeskrivelse.setText(jsonData.get("itemdescription").toString());
                    edtRecieveDate.setText(jsonData.get("itemreceived").toString());
                    edtDatingFrom.setText(jsonData.get("itemdatingfrom").toString());
                    edtDatingTo.setText(jsonData.get("itemdatingto").toString());
                    edtRefDonator.setText(jsonData.get("donator").toString());
                    edtTextRefProducer.setText(jsonData.get("producer").toString());
                    edtGeoArea.setText(jsonData.get("postnummer").toString());


                    JSONObject jsonImages = new JSONObject(jsonData.get("images").toString());

                    int i = 0;
                    while (jsonImages.opt("image_" + Integer.toString(i)) != null) {
                        JSONObject image = new JSONObject(jsonImages.opt("image_" + Integer.toString(i)).toString());
                        Log.d("test data url:", image.get("href").toString());
                        selectedImages.add(image.get("href").toString());

                        i++;
                    }
                    selectedImagesShow();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        File audioStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC), "EIR.Media");
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
        } else if (type == MEDIA_TYPE_AUDIO) {
            mediaFile = new File(audioStorageDir.getPath() + File.separator +
                    "aud_" + timeStamp + ".3gp");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void deleteDataAndFiles() {

        edtItemHeadline.setText("");
        edtBeskrivelse.setText("");
        edtRecieveDate.setText("");
        edtDatingFrom.setText("");
        edtDatingTo.setText("");
        edtRefDonator.setText("");
        edtTextRefProducer.setText("");
        edtGeoArea.setText("");
        itemNrDeterminer = -1;
        selectedImages.clear();
        selectedAudio.clear();
        myGallery.removeAllViews();
    }

    private RegistreringsDTO getDataAndFiles(int itemNr) {


        RegistreringsDTO registrering;

        if (itemNr == -1) {
            registrering = new RegistreringsDTO(
                    edtItemHeadline.getText().toString(),
                    edtBeskrivelse.getText().toString(),
                    edtRecieveDate.getText().toString(),
                    edtDatingFrom.getText().toString(),
                    edtDatingTo.getText().toString(),
                    edtRefDonator.getText().toString(),
                    edtTextRefProducer.getText().toString(),
                    edtGeoArea.getText().toString(),
                    selectedImages, selectedAudio);
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
                    selectedImages, selectedAudio);
        }


        return registrering;
    }


    public void showLoadingDialog() {

        if (progress == null) {
            progress = new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Wait while Loading...");
        }
        progress.show();
    }


    public void dismissLoadingDialog() {

        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    public boolean onDateCheck() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;
        Date date1 = null;

        try {
            date = sdf.parse(edtDatingFrom.getText().toString());
            date1 = sdf.parse(edtDatingTo.getText().toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null && date1 != null) {
            Log.d(edtDatingFrom.getText().toString(), Long.toString(date.getTime()));
            Log.d(edtDatingTo.getText().toString(), Long.toString(date1.getTime()));

            if (date.getTime() <= date1.getTime()) {
                return true;
            }
            return false;
        } else return true;


    }

    /*
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    */

    public boolean isNetworkAvailable() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;

    }
}



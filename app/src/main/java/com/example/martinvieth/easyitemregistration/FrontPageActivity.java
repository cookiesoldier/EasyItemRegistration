package com.example.martinvieth.easyitemregistration;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaRecorder;
import android.media.MediaPlayer;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
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

    LinearLayout myGallery;

    private ProgressDialog progress;


    TextView textView7;

    //Int som vi bruger til at bestemme itemNR til opdatering af genstand, hvis den er -1 så opdaterer vi ikke men laver et nyt item istedet.
    int itemNrDeterminer = -1;

    //De valgte billeder fra enten camera eller galleri
    List<String> selectedImages = new ArrayList<>();
    //De viste billeder fra galleri eller
    List<Bitmap> shownImages = new ArrayList<>();
    //
    //De valgt optagelser
    List<Uri> selectedAudio = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_frontpage);

        btnGalleryPhoto = (ImageButton) findViewById(R.id.imageButtonCamerafolder);
        btnAccept = (ImageButton) findViewById(R.id.imageButtonDone);




        btnGotoCamera = (ImageButton) findViewById(R.id.imageButtonCamera);
        btnGotoCamera.setOnClickListener(this);

        btnSearch = (ImageButton) findViewById(R.id.imageButtonSearch);
        btnSearch.setOnClickListener(this);

        btnRecorder = (ImageButton) findViewById(R.id.imageButtonRecorder);
        btnRecorder.setOnClickListener(this);

        photoThumb1 = (ImageView) findViewById(R.id.photoThumb);
        photoThumb2 = (ImageView) findViewById(R.id.photoThumb2);
        photoThumb3 = (ImageView) findViewById(R.id.photoThumb3);

        btnGalleryPhoto.setImageResource(R.drawable.ic_camerafolder);

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

        myGallery = (LinearLayout)findViewById(R.id.mygallery);
       /* findViewById(R.id.imageButtonRecorder).setOnClickListener(new View.OnClickListener() {
            @Override
        }
           public void onClick(View v) {
                startActivity(new Intent(FrontPageActivity.this, AudioRecorder.class));
            }
        }); */
    }


    public void selectedImagesShow(){

        for(String paths: selectedImages){
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

    Calendar myCalendar = Calendar.getInstance();

    private void updateLabel(int label) {

        String myFormat = "yyyy/MM/dd"; //In which you need put here
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
            }.execute(100);
        }
        if (edtItemHeadline.getText().length() != 0) {
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
                        Toast.makeText(getApplicationContext(), "Succes:Added item!!", Toast.LENGTH_LONG).show();
                    } else if (result.equals("failed")) {
                        Toast.makeText(getApplicationContext(), "Failed to add item!!", Toast.LENGTH_LONG).show();


                    }
                    itemNrDeterminer = -1;

                }
            }.execute(100);
        } else {
            Toast.makeText(getApplicationContext(), "Mangler Overskrift!!", Toast.LENGTH_LONG).show();
        }


        //hvis vi trykker hurtigt kan vi starte 2 async tasks, nok ikke så godt. :)


        if (v == btnSearch)

        {
            showLoadingDialog();
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
        System.out.println("ItemListParse(): jsonarray " + jsonArrayData.length());
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
                Log.d("Audioshit ", fileUri.toString());
                break;

            case IMAGE_CAPTURE:
                selectedImages.add(fileUri.toString());
                shownImages.clear();
                updatePhotoThump();
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
                shownImages.clear();
                updatePhotoThump();
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
                    shownImages.clear();

                    JSONObject jsonImages = new JSONObject(jsonData.get("images").toString());

                    int i = 0;
                    while (jsonImages.opt("image_" + Integer.toString(i)) != null) {
                        JSONObject per = new JSONObject(jsonImages.opt("image_" + Integer.toString(i)).toString());
                        Log.d("test data url:", per.get("href").toString());
                        selectedImages.add(per.get("href").toString());

                        i++;
                    }
                    selectedImagesShow();
                    updatePhotoThump();
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
        photoThumb1.setImageDrawable(null);
        photoThumb2.setImageDrawable(null);
        photoThumb3.setImageDrawable(null);
        itemNrDeterminer = -1;
        shownImages.clear();
        selectedImages.clear();
        selectedAudio.clear();
        myGallery.removeAllViews();
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

    /**
     * Denne metode tager billederne fra shownimages og lægger dem ind i de 3 photothump
     */
    public void updatePhotoThump() {
        Log.d("updateThump images: ", Integer.toString(selectedImages.size()));

        if (selectedImages.size() == 1) {
            Picasso.with(this).load(selectedImages.get(selectedImages.size() - 1)).placeholder(R.drawable.ic_placeholder).fit().into(photoThumb1);

            //photoThumb1.setImageBitmap(shownImages.get(0));
        } else if (selectedImages.size() == 2) {
            Picasso.with(this).load(selectedImages.get(selectedImages.size() - 1)).placeholder(R.drawable.ic_placeholder).fit().into(photoThumb1);
            Picasso.with(this).load(selectedImages.get(selectedImages.size() - 2)).placeholder(R.drawable.ic_placeholder).fit().into(photoThumb1);

            //photoThumb1.setImageBitmap(shownImages.get(0));
            // photoThumb2.setImageBitmap(shownImages.get(1));
        } else if (selectedImages.size() >= 3) {
            Picasso.with(this).load(selectedImages.get(selectedImages.size() - 1)).placeholder(R.drawable.ic_placeholder).fit().into(photoThumb1);
            Picasso.with(this).load(selectedImages.get(selectedImages.size() - 2)).placeholder(R.drawable.ic_placeholder).fit().into(photoThumb2);
            Picasso.with(this).load(selectedImages.get(selectedImages.size() - 3)).placeholder(R.drawable.ic_placeholder).fit().into(photoThumb3);
            //photoThumb1.setImageBitmap(shownImages.get(0));
            // photoThumb2.setImageBitmap(shownImages.get(1));
            // photoThumb3.setImageBitmap(shownImages.get(2));
        }

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

    protected void onResume() {
        dismissLoadingDialog();
        super.onResume();
    }
}

package com.example.martinvieth.easyitemregistration;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ClipData;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.media.MediaRecorder;
import android.media.MediaPlayer;

import java.io.File;
import java.io.FileNotFoundException;
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
    List<Uri> selectedImages = new ArrayList<>();
    //De viste billeder
    List<Bitmap> shownImages = new ArrayList<>();

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

        btnGalleryPhoto.setOnClickListener(this);
        btnAccept.setOnClickListener(this);
        edtRecieveDate.setOnClickListener(this);
        edtDatingFrom.setOnClickListener(this);
        edtDatingTo.setOnClickListener(this);



        Timestamp tsTemp = new Timestamp(System.currentTimeMillis());
        edtRecieveDate.setText(tsTemp.toString());
        edtDatingFrom.setText(tsTemp.toString());
        edtDatingTo.setText(tsTemp.toString());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu); // standard menuer
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    Calendar myCalendar = Calendar.getInstance();



    private void updateLabel(int label) {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMAN);

        if (label == 2) {
            edtDatingFrom.setText(sdf.format(myCalendar.getTime()));
        }
        if (label == 1) {

            edtRecieveDate.setText(sdf.format(myCalendar.getTime()));
        }
        if (label == 3) {
            edtDatingTo.setText(sdf.format(myCalendar.getTime()));
        }
    }

        @Override
    public void onClick(View v) {

            //String myFormat = "dd/MM/yyyy"; //In which you need put here
            //SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMAN);

        if (v == edtDatingFrom) {
            getSetDate(2);

        }
            if (v == edtRecieveDate) {
                getSetDate(1);

            }
            if (v == edtDatingTo) {
                getSetDate(3);
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
        //Den åbner tastatur op når appen åbnes, måske knapt så godt.

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

    private void getSetDate( final int labelNr) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(labelNr);

            }

        };


        new DatePickerDialog(FrontPageActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();





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
                selectedImages.add(fileUri);
                shownImages.clear();
                bitMapAdd();
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
                        selectedImages.add(uri);
                    }
                } else {
                    Log.d("URI check -----> ", data.getData().toString());
                    selectedImages.add(data.getData());
                    //shownImages.addAll(result);
                }
                shownImages.clear();
                bitMapAdd();
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
        shownImages.clear();

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
                    shownImages);
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
                    shownImages);
        }


        return registrering;
    }

    /**
     * Denne metode lægger de 3 først billeder URI's fra selectedimages ind i showimages som bitmaps
     *
     */
    public void bitMapAdd() {
        AssetFileDescriptor fileDS = null;
        int runs = 3;
        if (selectedImages.size() < 3) {
            runs = selectedImages.size();
        }
        Log.d("Nr of runs:  ", Integer.toString(runs));
        for (int x = 0; x < runs; x++) {
            try {
                /*
                tjek bitmaps størrelse, derefter downsize den til noget vi er sikre på at arbejde med.
                InputStream input = cr.openInputStream(url);
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                input.close();
                */
                Log.d("Pree add selected", selectedImages.get(0).toString());
                shownImages.add(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImages.get(selectedImages.size()-1-x)));

            } catch (IOException e) {
                Log.d("Error", "Could not find image file in storage.");
                e.printStackTrace();
            }


        }
        Log.d("size of runs", Integer.toString(runs));
        updatePhotoThump();
    }

    /**
     * Denne metode tager billederne fra shownimages og lægger dem ind i de 3 photothump
     */
    public void updatePhotoThump() {
        Log.d("updateThump images: ", Integer.toString(shownImages.size()));

            if(shownImages.size() == 1) {
                photoThumb1.setImageBitmap(shownImages.get(0));
            }else if(shownImages.size() == 2){
                photoThumb1.setImageBitmap(shownImages.get(0));
                photoThumb2.setImageBitmap(shownImages.get(1));
            }else if(shownImages.size() >= 3){
                photoThumb1.setImageBitmap(shownImages.get(0));
                photoThumb2.setImageBitmap(shownImages.get(1));
                photoThumb3.setImageBitmap(shownImages.get(2));
            }


    }
}

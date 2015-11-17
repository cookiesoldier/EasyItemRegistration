package com.example.martinvieth.easyitemregistration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

public class CameraActivity2 extends Activity implements View.OnClickListener {

    public static final int IMAGE_CAPTURE = 69;
    private ImageView iv;
    private ImageButton btnTakePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);

        System.out.println("Kamera blev forsøgt åbnet");

        btnTakePhoto = (ImageButton) findViewById(R.id.btnTakePhoto);
        btnTakePhoto.setOnClickListener(this);

        iv = (ImageView) findViewById(R.id.photoView);

    }

    @Override
    public void onClick(View v) {
        if(v == btnTakePhoto) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(Environment.getExternalStorageDirectory(), "/test9.jpg");
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(cameraIntent, IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;
        if(requestCode == IMAGE_CAPTURE) {
            //Bitmap img = (Bitmap) data.getExtras().get("data");
            System.out.println("---------------->"+Environment.getExternalStorageDirectory().toString());
            Log.d("ostemad", "------------------->"+Environment.getExternalStorageDirectory().toString());
            Bitmap img = (Bitmap) BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/test9.jpg");
            iv.setImageBitmap(img);
        }
    }
}

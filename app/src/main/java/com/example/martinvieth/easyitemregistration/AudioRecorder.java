package com.example.martinvieth.easyitemregistration;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.os.Environment;
import android.widget.Button;
import android.view.View;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//import static com.example.martinvieth.easyitemregistration.AudioRecorder.getOutputMediaFile;


public class AudioRecorder extends Activity implements View.OnClickListener {

    Button btnStartRecord;
    Button btnStopRecord;
    Button btnSave;
    Button btnPlay;
    Button btnStop;

    ImageView imgView;
    ListView recordList;
    TextView audioTitelTxt;
    EditText editAudioTitel;

    final String LOG_TAG = "AudioRecordTest";
    String mFileName = null;
    String uniqueName = String.valueOf(System.currentTimeMillis());
    //String uniqueName = String.valueOf(editAudioTitel.getText());
    MediaRecorder mRecorder = null;
    MediaPlayer mPlayer = null;
    private Uri fileUri;
    private File root;
    private ArrayList<File> audioList = new ArrayList<File>();

    //Eventuel mulighed for at pause nuværende optagelse, således
    //der kan optages ét langt lydklip, i stedet for flere små

    public ArrayList<File> getfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].getName().endsWith(".3gp")) {
                    audioList.add(listFile[i]);
                    getfile(listFile[i]);
                }
            }
        }
        return audioList;
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnStartRecord) {
            Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            startRecording();
            imgView.setImageResource(R.mipmap.record);
            getfile(root);
        }

        if (v == btnStopRecord) {
            Toast.makeText(getApplicationContext(), "Recording was successful", Toast.LENGTH_LONG).show();
            stopRecording();
            imgView.setImageResource(R.mipmap.play);
        }

        if (v == btnStop) {
            stopPlaying();
            imgView.setImageResource(R.mipmap.play);
        }

        if (v == btnSave) {
            //startActivity(new Intent(AudioRecorder.this, FrontPageActivity.class));
        }

        if (v == btnPlay) {
            boolean playing = true;
            onPlay(playing);

            if (playing) {
                btnPlay.setText("Pause");
            } else {
                btnPlay.setText("Play");
            }
            playing = !playing;
            imgView.setImageResource(R.mipmap.pause);
          /*  startPlaying();
            btnPlay.setText("Pause");

            if (btnPlay.getText() == "Pause") {
                imgView.setImageResource(R.mipmap.play);
                mPlayer.pause();
                btnPlay.setText("Play");
            }

            else {
                imgView.setImageResource(R.mipmap.pause);
                mPlayer.start();
                btnPlay.setText("Pause");
            }
        }*/
        }
    }


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_audio_recorder);

        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        getfile(root);

        btnStartRecord = (Button) findViewById(R.id.btnStartRecord);
        btnStartRecord.setOnClickListener(this);

        btnStopRecord = (Button) findViewById(R.id.btnStopRecord);
        btnStopRecord.setOnClickListener(this);

        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);

        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(this);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        imgView = (ImageView) findViewById(R.id.imgRPP);
        audioTitelTxt = (TextView) findViewById(R.id.audioTitelTxt);
        editAudioTitel = (EditText) findViewById(R.id.editAudioTitel);

        recordList = (ListView) findViewById(R.id.recordList);
        recordList.setClickable(true);
        ArrayAdapter<File> arrayAdapter = new ArrayAdapter<File>(this, android.R.layout.simple_list_item_1, audioList);
        recordList.setAdapter(arrayAdapter);

    }

    public AudioRecorder() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audio"+uniqueName+".3gp";
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}

 /*  private static getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
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
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "REC_" + timeStamp + ".3gp");
        return mediaFile;
    }

    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
*/
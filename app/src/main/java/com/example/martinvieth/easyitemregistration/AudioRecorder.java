package com.example.martinvieth.easyitemregistration;

import android.app.Activity;
import android.content.Intent;
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



/*
Denne klasse er inspireret af http://developer.android.com/guide/topics/media/audio-capture.html
OBS: Klassen bruges ikke, da denne kan erstattes med blot et intent-kald, hvorfor klassen ikke er færdig
Oprettet af Peter
 */
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
    public static final int AUDIO_SELECT = 66;
    String mFileName = null;
    String uniqueName = String.valueOf(System.currentTimeMillis());
    MediaRecorder mRecorder = null;
    MediaPlayer mPlayer = null;
    private Uri fileUri;
    private File root;
    private ArrayList<File> audioList = new ArrayList<File>();
    ArrayList<Uri> selectedAudio = new ArrayList<>();

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
            mPlayer.pause();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnStartRecord) {
            Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
            startRecording();
            imgView.setImageResource(R.mipmap.record);
            getfile(root);
        }

        if (v == btnStopRecord) {
            Toast.makeText(getApplicationContext(), "Recording was successful", Toast.LENGTH_SHORT).show();
            stopRecording();
            imgView.setImageResource(R.mipmap.play);
        }

        if (v == btnStop) {
            stopPlaying();
            imgView.setImageResource(R.mipmap.play);
        }

        if (v == btnSave) {
            //TODO
        }

        if (v == btnPlay) {
            imgView.setImageResource(R.mipmap.pause);
            startPlaying();
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
        mFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString();
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
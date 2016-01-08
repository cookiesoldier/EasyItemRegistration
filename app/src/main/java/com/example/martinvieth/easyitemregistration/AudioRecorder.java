package com.example.martinvieth.easyitemregistration;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.os.Environment;
import android.widget.Button;
import android.view.View;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.IOException;


public class AudioRecorder extends Activity implements View.OnClickListener {

    Button btnStartRecord;
    Button btnStopRecord;
    Button btnSave;
    Button btnPlay;
    Button btnStop;
    Button btnPause;

    ImageView imgView;

    final String LOG_TAG = "AudioRecordTest";
    String mFileName = null;
    String uniqueName = String.valueOf(System.currentTimeMillis());
    MediaRecorder mRecorder = null;
    MediaPlayer   mPlayer = null;



    @Override
    public void onClick(View v) {

        if (v == btnStartRecord){
            Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            startRecording();
            imgView.setImageResource(R.mipmap.recordpressed);
        }

        if (v == btnStopRecord){
            Toast.makeText(getApplicationContext(), "Recording was successful", Toast.LENGTH_LONG).show();
            stopRecording();
            imgView.setImageResource(R.mipmap.play);
        }

        if (v == btnPlay){
            startPlaying();
            imgView.setImageResource(R.mipmap.pause);
        }

        if (v == btnStop){
            stopPlaying();
            imgView.setImageResource(R.mipmap.play);
        }

        if (v == btnSave){

        }

        if (v == btnPause){
            mPlayer.pause();
            imgView.setImageResource(R.mipmap.play);
        }

    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_audio_recorder);


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

        btnPause = (Button) findViewById(R.id.btnPause);
        btnPause.setOnClickListener(this);

        imgView = (ImageView) findViewById(R.id.imgRPP);

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


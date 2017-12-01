package com.android.fisewatchlauncher.function.micro;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.fisewatchlauncher.R;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import rx.functions.Action1;


/**
 * Created by mare on 2017/8/19 0019.
 */

public class RecorderWavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_wav);
        RxPermissions.getInstance(RecorderWavActivity.this)
                .requestEach(Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if (permission.name.equals(Manifest.permission.RECORD_AUDIO)) {
                            Log.i("permissions", Manifest.permission.RECORD_AUDIO + "：" + permission.granted);
                        }
                        if (permission.name.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Log.i("permissions", Manifest.permission.WRITE_EXTERNAL_STORAGE + "：" + permission.granted);
                        }
                    }
                });
    }


    public void recordAudio(View v){
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recorded_audio.wav";
        int color = getResources().getColor(R.color.colorPrimaryDark,getTheme());
        int requestCode = 0;
        AndroidAudioRecorder.with(this)
                // Required
                .setFilePath(filePath)
                .setColor(color)
                .setRequestCode(requestCode)

                // Optional
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_48000)
                .setAutoStart(true)
                .setKeepDisplayOn(true)

                // Start recording
                .record();
    }

}

package com.android.fisewatchlauncher.function.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/16
 * @time 16:48
 */
public class MediaPlayerManager {

    private MediaPlayerManager() {

    }

    private static class SingletonHolder {
        private static final MediaPlayerManager INSTANCE = new MediaPlayerManager();
    }

    public static MediaPlayerManager instance() {
        return SingletonHolder.INSTANCE;
    }


    public void start(Context ctx, int res) {
        prepare(ctx, res);
        pause();
    }

    public void pause() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            return;
        }
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private MediaPlayer mediaPlayer;

    private void prepare(Context ctx, int res) {
        try {
            mediaPlayer = MediaPlayer.create(ctx, res);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();       //异步的方式加载音乐文件
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {  //异步加载完音乐文件后会回调此
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();               //准备好之后才能start
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

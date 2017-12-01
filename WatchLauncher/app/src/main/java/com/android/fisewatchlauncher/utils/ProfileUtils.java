package com.android.fisewatchlauncher.utils;

import android.content.Context;
import android.media.AudioManager;

import com.android.fisewatchlauncher.KApplication;

/**
 * project : WatchLauncher
 * author : ChenJP
 * date : 2017/10/31  11:28
 * describe : 情景模式工具类
 * 为 1 表示震动加响铃
 * 为 2 表示响铃
 * 为 3 表示震动
 * 为 4 表示静音
 */

public class ProfileUtils {

    public static final int    MODE_VIBRATE_AND_RING = 1;
    public static final int    MODE_RING             = 2;
    public static final int    MODE_VIBRATE          = 3;
    public static final int    MODE_SILENCE          = 4;
    public static final String SP_KEY_PROFILE_MODE   = "sp_key_profile_mode";

    public static void saveProfileMode(String content) {
        SPUtils.getInstance().put(SP_KEY_PROFILE_MODE, Integer.parseInt(content));
    }

    public static int getProfileMode() {
        return SPUtils.getInstance().getInt(SP_KEY_PROFILE_MODE);
    }

    public static void setProfile(String content) {
        int profileMode = Integer.parseInt(content);
        setProfile(profileMode);
    }
    public static void setProfile(int mode) {
        AudioManager localAudioManager = (AudioManager) KApplication.sContext.getSystemService(Context.AUDIO_SERVICE);
        switch (mode) {
            case MODE_VIBRATE_AND_RING:
                localAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                localAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                        AudioManager.VIBRATE_SETTING_ON);
                localAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                        AudioManager.VIBRATE_SETTING_ON);
                break;
            case MODE_RING:
                localAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                localAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                        AudioManager.VIBRATE_SETTING_OFF);
                localAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                        AudioManager.VIBRATE_SETTING_OFF);
                break;
            case MODE_VIBRATE:
                localAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                localAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                        AudioManager.VIBRATE_SETTING_ON);
                localAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                        AudioManager.VIBRATE_SETTING_ON);
                break;
            case MODE_SILENCE:
                localAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                localAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                        AudioManager.VIBRATE_SETTING_OFF);
                localAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                        AudioManager.VIBRATE_SETTING_OFF);
                break;
            default:
                break;
        }
    }
}

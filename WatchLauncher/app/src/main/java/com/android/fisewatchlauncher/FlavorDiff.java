package com.android.fisewatchlauncher;

import android.content.Intent;

import com.android.fisewatchlauncher.acty.EaseContactListActivity;
import com.android.fisewatchlauncher.acty.FiseAppsListActivity;
import com.android.fisewatchlauncher.acty.FiseChatMainActivity;
import com.android.fisewatchlauncher.acty.PhoneContactorActivity;
import com.android.fisewatchlauncher.constant.FileConstant;
import com.android.fisewatchlauncher.holder.ActivityTarget;
import com.android.fisewatchlauncher.utils.SPUtils;
import com.android.fisewatchlauncher.utils.WalkRouteNaviActivity;

/**
 * @author mare
 * @Description:TODO 针对渠道进行的差异化处理(方便改渠道名字)
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/2
 * @time 19:45
 */
public class FlavorDiff {

    public static final String SP_KEY_BLUE_FOX_BACKGROUND = "sp_key_blue_fox_background";

    /**
     * 首页各子碎片字符串id
     *
     * @return
     */
    public static Integer[] getHomePageTitles() {
        switch (BuildConfig.FLAVOR) {
            case "FISE_WSD"://拨号，联系人，短信，相机，心率，计步，设置，清除，谷歌play，应用
                return new Integer[]{
                        R.string.app_dialer, R.string.app_contact,
                        R.string.app_mms, R.string.app_camera,
                        R.string.app_heart_rate, R.string.app_stepcounter,
                        R.string.app_settings, R.string.app_clear,
                        R.string.app_playstore,R.string.all_apps
                };
            case "HT_790"://电话 微聊 相机 故事 讲英语 计步 设置
                return new Integer[]{
                        R.string.function_phonebook, R.string.function_wetchat,
                        R.string.function_camera, R.string.function_story,
                        R.string.function_english_study, R.string.function_step, R.string.function_settings
                };
            case "BLUE_FOX"://电话 微信 相机 故事 讲英语 计步 设置 图片 录音 视频
                return new Integer[]{
                        R.string.function_phonebook, R.string.function_wetchat,
                        R.string.function_camera, R.string.function_story,
                        R.string.function_english_study, R.string.function_step, R.string.function_settings,
                        R.string.function_img, R.string.function_recorder, R.string.function_video
                };
            case "ANTI"://电话 微聊 相机 故事 讲英语 计步 设置
            default:
                return new Integer[]{
                        R.string.function_phonebook, R.string.function_wetchat,
                        R.string.function_camera, R.string.function_story,
                        R.string.function_english_study, R.string.function_step, R.string.function_settings
                };
        }
    }

    /**
     * 首页各子碎片跳转
     *
     * @return
     */
    public static ActivityTarget[] getActivityTargets() {
        switch (BuildConfig.FLAVOR) {
            case "FISE_WSD"://拨号，联系人，短信，相机，心率，计步，设置，清除，谷歌play，应用
                return new ActivityTarget[]{
                        new ActivityTarget(Intent.ACTION_MAIN, null, "android.intent.action.DIAL", Intent.FLAG_ACTIVITY_SINGLE_TOP),
                        new ActivityTarget(Intent.ACTION_MAIN, null, "android.intent.category.APP_CONTACTS", 0),
                        new ActivityTarget(Intent.ACTION_MAIN, null, "android.intent.category.APP_MESSAGING", 0),
                        new ActivityTarget("com.mediatek.camera", "com.android.camera.CameraLauncher", null, 0),
                        new ActivityTarget("com.fise.heartrate", "com.fise.heartrate.MainActivity", null, 0),
                        new ActivityTarget("cn.com.fise.fisestepcounter", "cn.com.fise.fisestepcounter.view.MainActivity", null,0),
                        new ActivityTarget("com.android.settings", "com.android.settings.Settings", null, 0),
                        new ActivityTarget(null, null, "fise.action.ACTION_TOGGLE_RECENTS", 0),
                        new ActivityTarget("com.android.vending", "com.android.vending.AssetBrowserActivity", null, 0),
                        new ActivityTarget(KApplication.sContext.getPackageName(), FiseAppsListActivity.class.getName(), null, 0),
                };
            case "HT_790"://电话 微聊 相机 故事 讲英语 计步 设置
                return new ActivityTarget[]{
                        new ActivityTarget(KApplication.sContext.getPackageName(), PhoneContactorActivity.class.getName(), null, Intent.FLAG_ACTIVITY_NO_ANIMATION),
                        new ActivityTarget(KApplication.sContext.getPackageName(), FiseChatMainActivity.class.getName(), null, Intent.FLAG_ACTIVITY_NO_ANIMATION),
                        new ActivityTarget("com.mediatek.camera", "com.android.camera.CameraLauncher", null, Intent.FLAG_ACTIVITY_NEW_TASK),
                        new ActivityTarget(KApplication.sContext.getPackageName(), WalkRouteNaviActivity.class.getName(), null, Intent.FLAG_ACTIVITY_NO_ANIMATION),
                        new ActivityTarget(Intent.ACTION_MAIN, null, null, 0),
                        new ActivityTarget("cn.com.fise.fisestepcounter", "cn.com.fise.fisestepcounter.view.MainActivity", null, Intent.FLAG_ACTIVITY_NEW_TASK),
                        new ActivityTarget("com.android.settings", "com.android.settings.FiseSettingActivity", null, Intent.FLAG_ACTIVITY_NEW_TASK),
//                new ActivityTarget("com.fise.heartrate", "com.fise.heartrate.TestActivity", null, Intent.FLAG_ACTIVITY_NEW_TASK)

                };
            case "BLUE_FOX"://电话 微信 相机 故事 讲英语 计步 设置 图片 录音 视频
                return new ActivityTarget[]{
                        new ActivityTarget(KApplication.sContext.getPackageName(), PhoneContactorActivity.class.getName(), null, Intent.FLAG_ACTIVITY_NO_ANIMATION),
                        new ActivityTarget(KApplication.sContext.getPackageName(), FiseChatMainActivity.class.getName(), null, Intent.FLAG_ACTIVITY_NO_ANIMATION),
                        new ActivityTarget("com.mediatek.camera", "com.android.camera.CameraLauncher", null, Intent.FLAG_ACTIVITY_NEW_TASK),
                        new ActivityTarget(KApplication.sContext.getPackageName(), WalkRouteNaviActivity.class.getName(), null, Intent.FLAG_ACTIVITY_NO_ANIMATION),
                        new ActivityTarget(Intent.ACTION_MAIN, null, null, 0),
                        new ActivityTarget("cn.com.fise.fisestepcounter", "cn.com.fise.fisestepcounter.view.MainActivity", null, Intent.FLAG_ACTIVITY_NEW_TASK),
                        new ActivityTarget("com.android.settings", "com.android.settings.FiseSettingActivity", null, Intent.FLAG_ACTIVITY_NEW_TASK),
                        new ActivityTarget(Intent.ACTION_MAIN, null, null, 0),//图片
                        new ActivityTarget(Intent.ACTION_MAIN, null, null, 0),//录音
                        new ActivityTarget(Intent.ACTION_MAIN, null, null, 0)//视频

                };
            case "ANTI"://电话 微聊 相机 故事 讲英语 计步 设置
            default:
                return new ActivityTarget[]{
                        new ActivityTarget(KApplication.sContext.getPackageName(), PhoneContactorActivity.class.getName(), null, Intent.FLAG_ACTIVITY_NO_ANIMATION),
                        new ActivityTarget(KApplication.sContext.getPackageName(), EaseContactListActivity.class.getName(), null, Intent.FLAG_ACTIVITY_NO_ANIMATION),
                        new ActivityTarget("com.mediatek.camera", "com.android.camera.CameraLauncher", null, Intent.FLAG_ACTIVITY_NEW_TASK),
                        new ActivityTarget(KApplication.sContext.getPackageName(), WalkRouteNaviActivity.class.getName(), null, Intent.FLAG_ACTIVITY_NO_ANIMATION),
                        new ActivityTarget(Intent.ACTION_MAIN, null, null, 0),
                        new ActivityTarget("cn.com.fise.fisestepcounter", "cn.com.fise.fisestepcounter.view.MainActivity", null, Intent.FLAG_ACTIVITY_NEW_TASK),
                        new ActivityTarget("com.android.settings", "com.android.settings.FiseSettingActivity", null, Intent.FLAG_ACTIVITY_NEW_TASK),
//                new ActivityTarget("com.fise.heartrate", "com.fise.heartrate.TestActivity", null, Intent.FLAG_ACTIVITY_NEW_TASK)

                };
        }
    }

    /**
     * 首页各子碎片图标id
     *
     * @return
     */
    public static Integer[] getHomePageIcons() {
        switch (BuildConfig.FLAVOR) {
            case "FISE_WSD"://拨号，联系人，短信，相机，心率，计步，设置，清除，谷歌play，应用
                return new Integer[]{
                        R.drawable.icon_dialer, R.drawable.icon_contacts,
                        R.drawable.icon_mms, R.drawable.icon_camera, R.drawable.heart_rate,
                        R.drawable.icon_stepcounter, R.drawable.icon_settings,
                        R.drawable.icon_apps_clear,R.drawable.play_store,R.drawable.icon_all_apps
                };
            case "HT_790"://电话 微聊 相机 故事 讲英语 计步 设置
                return new Integer[]{
                        R.drawable.bg_function_phonebook, R.drawable.bg_function_wetchat,
                        R.drawable.bg_function_camera, R.drawable.bg_function_story, R.drawable.bg_function_english_talking,
                        R.drawable.bg_function_step, R.drawable.bg_function_settings//, R.drawable.qrcode_example
                };
            case "BLUE_FOX"://电话 微信 相机 故事 讲英语 计步 设置 图片 录音 视频
                return new Integer[]{
                        R.drawable.blue_fox_function_phone, R.drawable.blue_fox_function_weichat,
                        R.drawable.blue_fox_function_camera, R.drawable.blue_fox_function_story, R.drawable.blue_fox_function_english,
                        R.drawable.blue_fox_function_step_counter, R.drawable.blue_fox_function_setting, R.drawable.blue_fox_function_picture,
                        R.drawable.blue_fox_function_recoder, R.drawable.blue_fox_function_video
                };

            case "ANTI"://电话 微聊 相机 故事 讲英语 计步 设置
            default:
                return new Integer[]{
                        R.drawable.bg_function_phonebook, R.drawable.bg_function_wetchat,
                        R.drawable.bg_function_camera, R.drawable.bg_function_story, R.drawable.bg_function_english_talking,
                        R.drawable.bg_function_step, R.drawable.bg_function_settings//, R.drawable.qrcode_example
                };
        }
    }

    /**
     * 首页各子碎片背景图片
     *
     * @return
     */
    public static Integer[] getHomePageBg() {
        switch (BuildConfig.FLAVOR) {
            case "FISE_WSD"://拨号，联系人，短信，相机，心率，计步，设置，清除，谷歌play，应用
                return new Integer[]{
                        R.drawable.default_wallpaper, R.drawable.default_wallpaper,
                        R.drawable.default_wallpaper, R.drawable.default_wallpaper, R.drawable.default_wallpaper,
                        R.drawable.default_wallpaper, R.drawable.default_wallpaper, R.drawable.default_wallpaper,
                        R.drawable.default_wallpaper, R.drawable.default_wallpaper,
                };
            case "HT_790"://电话 微聊 相机 故事 讲英语 计步 设置
                return new Integer[]{
                        R.drawable.blue_fox_function_background0, R.drawable.blue_fox_function_background0,
                        R.drawable.blue_fox_function_background0, R.drawable.blue_fox_function_background0, R.drawable.blue_fox_function_background0,
                        R.drawable.blue_fox_function_background0, R.drawable.blue_fox_function_background0
                };
            case "BLUE_FOX"://电话 微信 相机 故事 讲英语 计步 设置 图片 录音 视频
                return new Integer[]{
                        R.drawable.blue_fox_function_background0, R.drawable.blue_fox_function_background0,
                        R.drawable.blue_fox_function_background0, R.drawable.blue_fox_function_background0, R.drawable.blue_fox_function_background0,
                        R.drawable.blue_fox_function_background0, R.drawable.blue_fox_function_background0, R.drawable.blue_fox_function_background0,
                        R.drawable.blue_fox_function_background0, R.drawable.blue_fox_function_background0
                };

            case "ANTI"://电话 微聊 相机 故事 讲英语 计步 设置
            default:
                return new Integer[]{
                        R.drawable.blue_fox_function_background0, R.drawable.blue_fox_function_background0,
                        R.drawable.blue_fox_function_background0, R.drawable.blue_fox_function_background0, R.drawable.blue_fox_function_background0,
                        R.drawable.blue_fox_function_background0, R.drawable.blue_fox_function_background0
                };
        }
    }

    /**
     * 根据简称找对应的pageTitle
     *
     * @param shortName
     * @return
     */
    public static String getPageTitleByShort(String shortName) {
        return null;
    }

    public static String getDownloadUrl() {
//        http://api.xcloudtech.com/
        switch (BuildConfig.FLAVOR) {
            case "HT_790"://电话 微聊 相机 故事 讲英语 计步 设置

                return "";
            case "BLUE_FOX"://电话 微信 相机 故事 讲英语 计步 设置 图片 录音 视频
            case "ANTI"://电话 微聊 相机 故事 讲英语 计步 设置
            default:
                return "http://api.xcloudtech.com/";
        }
    }

    public static String getBondUrl(String imei) {
        switch (BuildConfig.FLAVOR) {
            case "HT_790"://电话 微聊 相机 故事 讲英语 计步 设置
                return imei;
            case "BLUE_FOX"://电话 微信 相机 故事 讲英语 计步 设置 图片 录音 视频
            case "ANTI"://电话 微聊 相机 故事 讲英语 计步 设置
            default:
                return FileConstant.QR_IMEI_PREFIX_ANTI + imei;
        }
    }

    public static int getBlueFoxBackground(){
        int backgroundIndex = SPUtils.getInstance().getInt(SP_KEY_BLUE_FOX_BACKGROUND, 0);
        switch (backgroundIndex){
            default:
                return R.drawable.blue_fox_function_background0;
            case 1:
                return R.drawable.blue_fox_function_background1;
            case 2:
                return R.drawable.blue_fox_function_background2;
        }
    }
}

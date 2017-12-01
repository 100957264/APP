package com.android.fisewatchlauncher.function.time;

import android.content.Intent;
import android.text.TextUtils;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.constant.ReceiverConstant;
import com.android.fisewatchlauncher.utils.LogUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/15
 * @time 10:07
 */
public class LocalUtils {
    //    en_US zh_CN zh_TW es_ES pt_BR ru_RU fr_FR de_DE tr_TR vi_VN ms_MY in_ID th_TH it_IT ar_EG hi_IN bn_IN ur_PK fa_IR pt_PT nl_NL el_GR hu_HU tl_PH
    // ro_RO cs_CZ ko_KR km_KH iw_IL my_MM pl_PL es_US bg_BG hr_HR lv_LV lt_LT sk_SK uk_UA de_AT da_DK fi_FI nb_NO sv_SE en_GB hy_AM zh_HK et_EE ja_JP
    // kk_KZ sr_RS sl_SI ca_ES
    //    0:英文,1:简体中3:葡萄牙4:西班牙5:德文8:越南语7.土耳其9.俄罗斯10.
    /***
     * 中文
     */
    private static final String LANGUAGE_ZH = "zh";
    /**
     * 简体中文
     */
    private static final String LANGUAGE_COUNTRY_ZHCN = "CN";
    /**
     * 繁体中文
     */
    private static final String LANGUAGE_COUNTRY_ZHTW = "TW";
    /**
     * 英文
     */
    private static final String LANGUAGE_EN = "en";
    /**
     * 美式英文
     */
    private static final String LANGUAGE_EN_COUNTRY_US = "US";
//0:英文 ,1:简体中 3:葡萄牙 4:西班牙 5:德文 8:越南语 7.土耳其 9.俄罗斯 10.

    /**
     * 英文
     **/
    private static final int FLAG_ENGLISH_US = 0;
    /**
     * 简体中
     **/
    private static final int FLAG_ZH_CN = 1;
    /**
     * 简体繁文
     **/
    private static final int FLAG_ZH_TW = 2;
    /**
     * 葡萄牙语
     **/
    private static final int FLAG_PT_PT = 3;
    /**
     * 西班牙语
     */
    private static final int FLAG_ES_ES = 4;
    /**
     * 德文
     */
    private static final int FLAG_DE_DE = 5;
    /**
     * 意大利语
     */
    private static final int FLAG_IT_IT = 6;
    /**
     * 土耳其
     */
    private static final int FLAG_TR_TR = 7;
    /**
     * 越南语
     */
    private static final int FLAG_VI_VN = 8;
    /**
     * 俄语
     */
    private static final int FLAG_RU_RU = 9;
    /**
     * 法语
     */
    private static final int FLAG_FR_FR = 10;

    private static String[] int2StringArray(int flag) {
        String[] lanageCountry = new String[2];
        switch (flag) {
            case FLAG_ENGLISH_US:
                lanageCountry[0] = LANGUAGE_EN;
                lanageCountry[1] = LANGUAGE_EN_COUNTRY_US;
                break;
            case FLAG_ZH_CN:
                lanageCountry[0] = LANGUAGE_ZH;
                lanageCountry[1] = LANGUAGE_COUNTRY_ZHCN;
                break;
            case FLAG_ZH_TW:
                lanageCountry[0] = LANGUAGE_ZH;
                lanageCountry[1] = LANGUAGE_COUNTRY_ZHTW;
                break;
            case FLAG_PT_PT:
            case FLAG_ES_ES:
            case FLAG_DE_DE:
            case FLAG_IT_IT:
            case FLAG_TR_TR:
            case FLAG_VI_VN:
            case FLAG_RU_RU:
            case FLAG_FR_FR:
                lanageCountry[0] = LANGUAGE_EN;
                lanageCountry[1] = LANGUAGE_EN_COUNTRY_US;
                break;
            default:
                lanageCountry[0] = LANGUAGE_EN;
                lanageCountry[1] = LANGUAGE_EN_COUNTRY_US;
                break;

        }
        return lanageCountry;
    }

    private void setLanuage(String lanuage){

    }

    private void setCountry(String country){

    }

    public static void setLocales(String content){
        if (TextUtils.isEmpty(content)) {
            return;
        }
        String[] locaes = TextUtils.split(content,GlobalSettings.MSG_CONTENT_SEPERATOR);
        setLanuageCountry(locaes[0]);
        setTimeZone(locaes[1]);
    }

    private static void setTimeZone(String zone) {
        //do nothing
    }

    public static void setLanuageCountry(String lanuageCountry){
        if (TextUtils.isEmpty(lanuageCountry)) {
            return;
        }
        int flag = Integer.parseInt(lanuageCountry);
        String[] launages = int2StringArray(flag);
//        List<String> cmds = Arrays.asList(new String[]{
//            "setprop persist.sys.language "+ launages[0],"setprop persist.sys.country "+ launages[1]
//        });
//        boolean isRoot = ShellUtils.checkRootPermission();
//        ShellUtils.execCommand(cmds,isRoot,false);
        List<String> cmds = Arrays.asList(launages);
        String  cmd = TextUtils.join("_",cmds);
        LogUtils.e(cmd);
        Intent languageIntent=   new Intent(ReceiverConstant.ACTION_CMD_SET_LOCAL_LANGUAGE);
        languageIntent.putExtra(ReceiverConstant.EXTRA_SET_LOCAL_LANGUAGE,cmd);
        KApplication.sContext.sendBroadcast(languageIntent);
    }

}

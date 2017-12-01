package com.android.fisewatchlauncher.function.phone;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.entity.dao.PhoneContractor;
import com.android.fisewatchlauncher.prenster.dao.PhoneContactorDaoUtils;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.UnicodeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/18
 * @time 20:52
 */
public class ContactorsUtils {

    /**
     * 解析PHB格式的电话本
     *
     * @param content
     */
    public static void paresePHB(String content) {
        List<PhoneContractor> phbPhoneContractors = getContactorList(content);
        if (phbPhoneContractors == null) return;
        LogUtils.e("Bottom5Contactors : " + phbPhoneContractors.toString());
        PhoneContactorDaoUtils.instance().updateBottom5Contactor(phbPhoneContractors);
    }

    @Nullable
    private static List<PhoneContractor> getContactorList(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        String[] numNames = TextUtils.split(content,GlobalSettings.MSG_CONTENT_SEPERATOR);
        int numLen = numNames.length / 2;
        List<PhoneContractor> phbPhoneContractors = new ArrayList<>();
        PhoneContractor phbContacotr;
        String num, unicodeName;
        LogUtils.d(Arrays.toString(numNames));
        for (int i = 0; i < numLen; i++) {
            num = numNames[i * 2];
            unicodeName = numNames[i * 2 + 1];
            phbContacotr = new PhoneContractor(UnicodeUtils.unicodeNo0xuToString(unicodeName), num);
            phbPhoneContractors.add(phbContacotr);
        }
        return phbPhoneContractors;
    }

    /**
     * 解析PHB2格式的电话本
     *
     * @param content
     */
    public static void paresePHB2(String content) {
        List<PhoneContractor> phbPhoneContractors = getContactorList(content);
        if (phbPhoneContractors == null) return;
        LogUtils.e("Top5Contactors : " + phbPhoneContractors.toString());
        PhoneContactorDaoUtils.instance().updateTop5Contactor(phbPhoneContractors);
    }

    /**
     * 解析PHL格式的电话本
     *
     * @param content
     */
    public static void paresePHL(String content) {
        List<PhoneContractor> phbPhoneContractors = getContactorList(content);
        if (phbPhoneContractors == null) return;
        LogUtils.e("PHL Contactors : " + phbPhoneContractors.toString());
        PhoneContactorDaoUtils.instance().updateAllPhoneContactor(phbPhoneContractors);
    }
}

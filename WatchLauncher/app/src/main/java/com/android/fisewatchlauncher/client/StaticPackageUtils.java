package com.android.fisewatchlauncher.client;

import android.text.TextUtils;

import com.android.fisewatchlauncher.client.helper.stickpackage.AbsStickPackageHelper;
import com.android.fisewatchlauncher.client.helper.stickpackage.BaseStickPackageHelper;
import com.android.fisewatchlauncher.client.helper.stickpackage.SpecifiedStickPackageHelper;
import com.android.fisewatchlauncher.client.helper.stickpackage.StaticLenStickPackageHelper;
import com.android.fisewatchlauncher.client.helper.stickpackage.VariableLenStickPackageHelper;

import java.nio.ByteOrder;

/**
 * Created by user on 2017/9/8.
 */

public class StaticPackageUtils {
    private StaticPackageUtils() {
    }

    private static class SingletonHolder {
        private static final StaticPackageUtils INSTANCE = new StaticPackageUtils();
    }

    public static StaticPackageUtils instance() {
        return SingletonHolder.INSTANCE;
    }

    public AbsStickPackageHelper getSpecial() {
        StickPackageBean bean = new StickPackageBean();
        bean.setHead(GlobalSettings.MSG_PREFIX_ESCAPE);
        bean.setTail(GlobalSettings.MSG_SUFFIX_ESCAPE);
        return getStickPackageHelper(StickPackageType.SPECIAL_HEAD_TAIL, bean);
    }

    public AbsStickPackageHelper getConfirmedLenl(int len) {
        StickPackageBean bean = new StickPackageBean();
        bean.setLen(len);
        return getStickPackageHelper(StickPackageType.CONFIRMED_LEN, bean);
    }

    public AbsStickPackageHelper getDynamicLenl(int len, boolean isSmallPort, int lenOffset, int contentOffset) {
        StickPackageBean bean = new StickPackageBean();
        bean.setLen(len);
        bean.setSmallPort(isSmallPort);
        bean.setLenOffset(lenOffset);
        bean.setContentOffset(contentOffset);
        return getStickPackageHelper(StickPackageType.DYNAMIC_LEN, bean);
    }

    private AbsStickPackageHelper getStickPackageHelper(StickPackageType type, StickPackageBean bean) {
        AbsStickPackageHelper stickPackageHelper = null;
        switch (type) {
            case NONE:
//                selectNone();
                stickPackageHelper = new BaseStickPackageHelper();
                break;
            case SPECIAL_HEAD_TAIL:
                String head = bean.getHead();
                String tail = bean.getTail();
                if (!TextUtils.isEmpty(head) || !TextUtils.isEmpty(tail)) {
                    stickPackageHelper = new SpecifiedStickPackageHelper(head.getBytes(), tail.getBytes());
                }
                break;
            case CONFIRMED_LEN:
                int len = bean.getLen();
                stickPackageHelper = new StaticLenStickPackageHelper(len);
                break;
            case DYNAMIC_LEN:
                boolean isSmallPort = bean.isSmallPort;
                ByteOrder byteOrder = isSmallPort ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
                int lenSize = bean.getLen();
                int lenIndex = bean.getLenOffset();
                int offset = bean.getContentOffset();
                stickPackageHelper = new VariableLenStickPackageHelper(byteOrder, lenSize, lenIndex, offset);
                break;
        }
        return stickPackageHelper;
    }

    public enum StickPackageType {
        NONE, SPECIAL_HEAD_TAIL, CONFIRMED_LEN, DYNAMIC_LEN
    }

}

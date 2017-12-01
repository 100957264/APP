package com.android.fisewatchlauncher.function.photo;

import android.text.TextUtils;

import com.android.fisewatchlauncher.BuildConfig;
import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.constant.FileConstant;
import com.android.fisewatchlauncher.utils.FileUtils;
import com.android.fisewatchlauncher.utils.LogUtils;

import java.io.File;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/10
 * @time 18:55
 */
public class QrCodeUtils {

    /**
     * TODO 解析来自服务器的二维码图片信息
     *
     * @param contentBytes
     */
    public static void parseCID(byte[] contentBytes) {
        if (null == contentBytes) {
            return;
        }
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return;
        }
        String path = getQrPath(imei);
        LogUtils.e("解析二维码图片信息 要存放的路径 " + path);
        boolean isSuccess = false;
        try {
            isSuccess = FileUtils.writeFile(path, contentBytes);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("二维码解析异常 " + e);
            FileUtils.deleteFile(path);//解析错误就删除数据
        } finally {
            LogUtils.e("解析二维码图片信息 " + (isSuccess ? "成功" : "失败"));
        }

        //TODO 通知更新二维码界面
    }

    /**
     * 获取二维码文件完整路径
     *
     * @param imei
     * @return
     */
    private static String getQrPath(String imei) {
        return FileConstant.QR_TEMP_OUTPUT_PATH + getQRName(imei);
    }

    private static String getQRName(String imei) {
        return FileConstant.QR_TEMP_OUTPUT_PATH_PREFIX + BuildConfig.FLAVOR + "_" + imei +
                FileConstant.QR_TEMP_OUTPUT_PATH_SUFFIX;
    }

    /**
     * 获取二维码文件
     *
     * @param name
     * @return
     */
    public static File getTempFile(String name) {
        return makeTempFile(FileConstant.QR_TEMP_OUTPUT_PATH, getQRName(name));
    }

    public static File makeTempFile(String saveDir, String qrName) {
        final File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, qrName);
    }

}

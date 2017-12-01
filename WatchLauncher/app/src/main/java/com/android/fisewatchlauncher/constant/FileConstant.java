package com.android.fisewatchlauncher.constant;


import com.android.fisewatchlauncher.utils.SDCardUtils;

import java.io.File;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/12
 * @time 10:19
 */
public class FileConstant {

    public static final String SDCARD_PATH = SDCardUtils.getSDCardPath() +
            File.separator;
    public static final String FILE_OUTPUT_PATH_BASE = SDCARD_PATH + "com.fise.launcher/";

    public static final String DB_OUTPUT_PATH_BASE = FILE_OUTPUT_PATH_BASE + "db/";
    /**拍照后原图回存入此路径下**/
    /**
     * 抓拍相片保存的位置路径
     **/
    public static final String PHOTO_CAPTURE_OUTPUT_PATH = FILE_OUTPUT_PATH_BASE + "rcapture/";

    /**
     * 二维码临时裁剪图片路径
     **/
    public static final String QR_TEMP_OUTPUT_PATH = FILE_OUTPUT_PATH_BASE + "qr/";
    /**
     * 二维码临时裁剪图片后缀
     **/
    public static final String QR_TEMP_OUTPUT_PATH_SUFFIX = ".jpg";

    /**
     * 防走丢二维码生成字符串的前缀
     **/
    public static final String QR_IMEI_PREFIX_ANTI = "https://api.xcloudtech.com/d?IMEI=";

    /**
     * 二维码临时裁剪图片前缀
     **/
    public static final String QR_TEMP_OUTPUT_PATH_PREFIX = "qr_";

    /**
     * 微聊文件相关路径
     **/
    public static final String WETCHAT_FILE_PATH = FILE_OUTPUT_PATH_BASE + "wetchat/";

    /**
     * 对讲文件相关路径
     **/
    public static final String TALKING_FILE_PATH = FILE_OUTPUT_PATH_BASE + "talking/";

    /**
     * 对讲录音文件相关路径
     **/
    public static final String TALKING_RECORD_FILE_PATH = TALKING_FILE_PATH + "record/";
    /**
     * 对讲录音文件相关路径
     **/
    public static final String TALKING_DOWNLOAD_FILE_PATH = TALKING_FILE_PATH + "download/";

    /**
     * 微聊语音文件上传路径
     **/
    public static final String WETCHAT_VOICE_UPLOAD_PATH = WETCHAT_FILE_PATH + "upload/";

    /**
     * 微聊语音文件下载路径
     **/
    public static final String WETCHAT_VOICE_DOWNLOAD_PATH = WETCHAT_FILE_PATH + "download/";
    public static final String VOICE_SUFFIX_AMR = ".amr";
    /**
     * 语音测试文件完整路径
     */
    public static final String VOICE_TEST_NAME = WETCHAT_VOICE_UPLOAD_PATH + "test_voice" + VOICE_SUFFIX_AMR;

    /**
     * Crash日志文件收集路径
     */
    public static final String FUNCTION_CRASH_FILE_DIR = FILE_OUTPUT_PATH_BASE + "crash/";

    //=================================兼容家家关爱====================================
    public static final String WATCH_LAUNCHER_PATH_BASE = SDCARD_PATH + "watchlauncher" + File.separator;

    /**
     * 白名单文件路径
     **/
    public static final String WHITE_LIST_DIR_PATH = WATCH_LAUNCHER_PATH_BASE + "number/";

    /**
     * 白名单文件名字
     */
    public static final String WHITE_LIST_FILE_PATH = WHITE_LIST_DIR_PATH + "number.txt";

    /**
     * 功能禁用状态存储文件路径
     */
    public static final String FUNCTION_FORBIDDEN_STATUS_PATH_DIR = WATCH_LAUNCHER_PATH_BASE + "forbid/";
    /**
     * 功能禁用状态文件名字
     */
    public static final String FUNCTION_FORBIDDEN_STATUS_PATH_FILE = FUNCTION_FORBIDDEN_STATUS_PATH_DIR + "forbidden.txt";

    /**
     * 功能绑定状态存储文件路径
     */
    public static final String FUNCTION_BIND_STATUS_PATH_DIR = WATCH_LAUNCHER_PATH_BASE + "bind/";
    /**
     * 功能绑定状态文件名字
     */
    public static final String FUNCTION_BIND_STATUS_PATH_FILE = FUNCTION_BIND_STATUS_PATH_DIR + "bind.txt";

}

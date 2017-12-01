package com.android.fisewatchlauncher.constant;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/31
 * @time 11:29
 */
public class ReceiverConstant {
    public static final String ACTION_STEP = "cn.com.fise.broadcast.CURRENT_STEP";
    public static final String EXTRA_STEP_COUNT = "CURRENT_STEP";

    /**
     * 隐藏来电去电界面
     */
    public static final String ACTION_HIDE_INCALL_UI = "com.fise.broadcast.ACTION_LISTEN";

    /**
     * 定位开始闹钟
     **/
    public static final String LOCATION_START = "com.fise.intent.ACTION_ALARM_LOCATION_START";
    /**
     * 定位停止闹钟
     **/
    public static final String LOCATION_STOP = "com.fise.intent.ACTION_ALARM_LOCATION_STOP";
    /**
     * 普通闹钟
     **/
    public static final String COMMON_CLOCK = "com.fise.intent.ACTION_ALARM_COMMON_CLOCK";
    /**
     * 上课禁用闹钟
     **/
    public static final String CLASS_FORBIDEN = "com.fise.intent.ACTION_ALARM_CLASS_FORBIDEN";
    /**
     * 固定频率定时上传闹钟
     **/
    public static final String CONFIRMED_FREQUENCY_UPLOAD = "com.fise.intent.ACTION_ALARM_CONFIRMED_FREQUENCY_UPLOAD";

    /**
     * 关机指令
     **/
    public static final String ACTION_CMD_POWER_OFF = "com.android.fise.ACTION_SHUTDOWN";

    /**
     * 设置语言指令
     **/
    public static final String ACTION_CMD_SET_LOCAL_LANGUAGE = "com.android.fise.ACTION_SET_LOCAL_LANGUAGE";
    public static final String EXTRA_SET_LOCAL_LANGUAGE = "SET_LOCAL_LANGUAGE";

    /**
     * 恢复出厂设置广播
     **/
    public static final String ACTION_CMD_RESET_FACTORY = "com.fise.intent.ACTION_RESET_PHONE";

    /**
     * 设备开始绑定广播
     **/
    public static final String ACTION_CMD_BIND_DEVICE = "net.wecare.watch_launcher.ACTION_BIND";
    /**
     * 设备开始解绑广播
     **/
    public static final String ACTION_CMD_UNBIN_DEVICE = "net.wecare.watch_launcher.ACTION_UNBIND";

    /**
     * 找手机测试广播
     **/
    public static final String ACTION_CMD_TEST_FIND_PHONE = "com.android.action.fise.test.findphone";

    /**
     * 恢复出厂设置测试广播
     **/
    public static final String ACTION_CMD_TEST_RESET_PHONE = "com.android.action.fise.test.resetphone";

    /**
     * 添加联系人测试广播
     **/
    public static final String ACTION_CMD_TEST_CONTACTOR = "com.android.action.fise.test.contactor";

    public static final String EXTRA_TEST_CONTACTOR_NICK = "nick";
    public static final String EXTRA_TEST_CONTACTOR_NUM = "num";

    /**
     * 白名单测试广播
     **/
    public static final String ACTION_CMD_TEST_WHITELIST = "com.android.action.fise.test.whitelist";

    /**
     * 语音上传下载测试广播
     **/
    public static final String ACTION_CMD_TEST_VOICE_UPLOAD = "com.android.action.fise.test.voice.upload";
    public static final String EXTRA_CMD_TEST_VOICE = "path";
    /**
     * 图片上传下载测试广播
     **/
    public static final String ACTION_CMD_TEST_IMG = "com.android.action.fise.test.img";
    public static final String EXTRA_CMD_TEST_IMG = "rcapture";

    /**
     * 拨打电话测试广播
     **/
    public static final String ACTION_CMD_TEST_PHONE_CALL = "com.android.action.fise.test.voice.phonecall";

    /**
     * 定位测试广播
     **/
    public static final String ACTION_CMD_TEST_LOCATION = "com.android.action.fise.test.location";
    public static final String EXTRA_TEST_FLAG_LOCATION_STATE = "location";
    /**
     * 闹钟测试广播
     **/
    public static final String ACTION_CMD_TEST_CLOCK = "com.android.action.fise.test.clock";
    public static final String EXTRA_TEST_FLAG_CLOCK = "clock";

    /**
     * WIFI测试广播
     **/
    public static final String ACTION_CMD_TEST_WIFI= "com.android.action.fise.test.wifi";
    public static final String EXTRA_TEST_FLAG_WIFI= "wifi";

    /**
     * 环信测试广播
     **/
    public static final String ACTION_CMD_TEST_EASE= "com.android.action.fise.test.ease";
    public static final String EXTRA_TEST_FLAG_EASE= "ease";

    /****短信相关****/
    public static final String ACTION_SMS_RELATIVE = "com.android.action.fise.sms.relative";
    public static final String EXTRA_SMS_FLAG = "flag_state";
    public static final String EXTRA_SMS_SEND_NUM = "flag_send_num";
    public static final int EXTRA_SMS_SEND_FLAG = 1000;//短信是否发送成功
    public static final int EXTRA_SMS_DELIVERY_FLAG = 1001;//短信发送是否被收到

}

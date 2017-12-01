package com.android.fisewatchlauncher.parser;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.android.fisewatchlauncher.BuildConfig;
import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.acty.FindPhoneDialog;
import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.client.TcpClient;
import com.android.fisewatchlauncher.client.msg.MsgType;
import com.android.fisewatchlauncher.constant.FileConstant;
import com.android.fisewatchlauncher.constant.ReceiverConstant;
import com.android.fisewatchlauncher.entity.msg.TcpMsg;
import com.android.fisewatchlauncher.entity.weather.Weather;
import com.android.fisewatchlauncher.event.WhiteListEvent;
import com.android.fisewatchlauncher.function.alarm.AlarmTimer;
import com.android.fisewatchlauncher.function.alarm.RemindUtils;
import com.android.fisewatchlauncher.function.location.LocationUploadManager;
import com.android.fisewatchlauncher.function.phone.ContactorsUtils;
import com.android.fisewatchlauncher.function.phone.PhoneCallUtils;
import com.android.fisewatchlauncher.function.photo.QrCodeUtils;
import com.android.fisewatchlauncher.function.step.StepUtils;
import com.android.fisewatchlauncher.function.talking.TalkingUtils;
import com.android.fisewatchlauncher.function.time.LocalUtils;
import com.android.fisewatchlauncher.function.weather.WeatherPresenter;
import com.android.fisewatchlauncher.function.wetchat.EaseMobManager;
import com.android.fisewatchlauncher.function.wifi.WifiSyncManager;
import com.android.fisewatchlauncher.manager.PreferControler;
import com.android.fisewatchlauncher.prenster.dao.CenterSettingsUtils;
import com.android.fisewatchlauncher.utils.AppUtils;
import com.android.fisewatchlauncher.utils.ArrayUtils;
import com.android.fisewatchlauncher.utils.CaptureUtils;
import com.android.fisewatchlauncher.utils.FileUtils;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.PhoneUtils;
import com.android.fisewatchlauncher.utils.ProfileUtils;
import com.android.fisewatchlauncher.utils.SilenceTimeUtils;
import com.android.fisewatchlauncher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.nio.charset.Charset;
import java.util.Arrays;

import static com.android.fisewatchlauncher.client.msg.MsgType.REMIND;
import static com.android.fisewatchlauncher.client.msg.MsgType.TK;
import static com.android.fisewatchlauncher.utils.RegexUtils.getMatchsCount;

/**
 * 处理接收的信息
 * Created by fanyang on 2017/8/8.
 */
public class MsgRecService {

    private MsgRecService() {

    }

    private static class SingletonHolder {
        private static final MsgRecService INSTANCE = new MsgRecService();
    }

    public static MsgRecService instance() {
        return SingletonHolder.INSTANCE;
    }

    public void handleRecvMsg(Context ctx, TcpMsg msg) {
        ParseSourceDataBytes parseBytes = null;
        try {
            parseBytes = new ParseSourceDataBytes(msg).invoke();
        } catch (Exception e) {
            LogUtils.e("handleRecvMsg " + e);
        } finally {
            if (null == parseBytes) {
                LogUtils.e("消息解析异常!!!!!! ");
                return;
            }
        }
        if (parseBytes.isNullResult()) return;
        final String headerType = parseBytes.getHeaderType();
        String content = parseBytes.getContent();
        byte[] contentBytes = parseBytes.getContentBytes();
        switch (headerType) {
            case MsgType.LK:
                // do nothing;
                break;
            case MsgType.PING:
                // do nothing;
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                int flag = Integer.parseInt(content);
                boolean isBond = flag == 1;
                LogUtils.e("isBond " + isBond);
                PreferControler.instance().setDeviceBindState(isBond);
                FileUtils.writeFile(FileConstant.FUNCTION_BIND_STATUS_PATH_FILE, String.valueOf(flag));
                if (BuildConfig.FLAVOR.equals("HT_790")) {//该设备是主动触发的所以不用回复
                    MsgParser.instance().sendMsgByType(MsgType.PING);
                }
                break;
            case MsgType.UD:
                // do nothing;
                break;
            case MsgType.UD_CDMA:
                // do nothing;
                break;
            case MsgType.UD2:
                // do nothing;
                break;
            case MsgType.UD_CDMA2:
                // do nothing;
                break;
            case MsgType.AL:

                break;
            case MsgType.AL_CDMA:

                break;
            case MsgType.UP:
                break;
            case MsgType.UP2:
                break;
            case MsgType.ALARM:
                //TODO 待实现
                //终端产生警情后发送报警信息到平台,
                // 若终端没有收到回复,则定时上报直到收到报警确认为止.
                break;
            case MsgType.Time:
                if (TextUtils.isEmpty(content)) return;
//                +8,2017:9:17,16-46-5
//                long currentTime = Long.parseLong(content);
                //EventBus.getDefault().post(new TimeUpdateEvent(currentTime));
                break;
            case MsgType.LGZONE:
                if (TextUtils.isEmpty(content)) return;

                break;
            case MsgType.WT:
                WeatherPresenter.parseWeather(Weather.CmdType.WT, content);
                break;
            case MsgType.WT2:
                WeatherPresenter.parseWeather(Weather.CmdType.WT2, content);
                break;
            case MsgType.WEA:
                WeatherPresenter.parseWeather(Weather.CmdType.WEA, content);
                break;
            case MsgType.img:
                break;
            case MsgType.VOICE:
                break;
            case MsgType.UPLOAD://上传位置时间间隔设置
                MsgParser.instance().sendMsgByType(MsgType.UPLOAD);
                LocationUploadManager.instance().parseInteraval(content);
                break;
            /*中心号码设置*/
            case MsgType.CENTER:
                MsgParser.instance().sendMsgByType(MsgType.CENTER);
                //存入数据库
                LogUtils.e("MsgType.CENTER" + content);
                GlobalSettings.instance().setCenterNum(content);
                break;
            case MsgType.PW:
                CenterSettingsUtils.instance().updateSMSControlPwd(content);
                MsgParser.instance().sendMsgByType(MsgType.PW);
                break;
            case MsgType.CALL:
                MsgParser.instance().sendMsgByType(MsgType.CALL);
                PhoneUtils.call(content);
                break;
            case MsgType.MONITOR:
                MsgParser.instance().sendMsgByType(MsgType.MONITOR);
                PhoneCallUtils.monitorCall(content);
                break;
            case MsgType.SOS:
                MsgParser.instance().sendMsgByType(headerType);
                if (TextUtils.isEmpty(content)) return;
                int count = getMatchsCount(GlobalSettings.MSG_CONTENT_SEPERATOR, content);
                LogUtils.e(headerType + " , " + content);
                if (count != 1) {
                    LogUtils.e("SOS格式不对 必须二个");
                    return;
                }
                String[] sosNums = TextUtils.split(content, GlobalSettings.MSG_CONTENT_SEPERATOR);
                CenterSettingsUtils.instance().updateSOSNum(sosNums[0], sosNums[1], "");
                break;
            case MsgType.SOS2:
                MsgParser.instance().sendMsgByType(headerType);//回复SOS2
                if (TextUtils.isEmpty(content)) return;
                int sos2Count = getMatchsCount(GlobalSettings.MSG_CONTENT_SEPERATOR, content);
                LogUtils.e(headerType + " , " + content);
                if (sos2Count != 2) {
                    LogUtils.e("SOS格式不对 必须三个");
                    return;
                }
                String[] sos2Nums = TextUtils.split(content, GlobalSettings.MSG_CONTENT_SEPERATOR);
                //String[] sos2Num = content.split(GlobalSettings.MSG_CONTENT_SEPERATOR);// len = 1 数组越界
                CenterSettingsUtils.instance().updateSOSNum(sos2Nums[0], sos2Nums[1], sos2Nums[2]);

                break;
            case MsgType.IP:
//                [SG*8800000015*0014*IP,113.81.229.9,5900]
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                String[] ipAndPort = TextUtils.split(content, GlobalSettings.MSG_CONTENT_SEPERATOR);
                GlobalSettings.instance().setIP(ipAndPort[0]);
                GlobalSettings.instance().setPort(Integer.parseInt(ipAndPort[1]));
                TcpClient.instance().stopClient();
                TcpClient.instance().startClient();
                break;
            case MsgType.FACTORY:
                MsgParser.instance().sendMsgByType(MsgType.FACTORY);
                LogUtils.i("恢复出厂设置ing....");
                PreferControler.instance().setDeviceBindState(false);//先通知解绑再恢复出厂设置
                KApplication.sContext.sendBroadcast(new Intent(ReceiverConstant.ACTION_CMD_RESET_FACTORY));
                break;
            case MsgType.LZ:
                MsgParser.instance().sendMsgByType(MsgType.LZ);
                LocalUtils.setLocales(content);
                break;
            case MsgType.SOSSMS:
                CenterSettingsUtils.instance().updateSMSSwitch(content);
                MsgParser.instance().sendMsgByType(MsgType.SOSSMS);
                break;
            case MsgType.LOWBAT:
                MsgParser.instance().sendMsgByType(MsgType.LOWBAT);
                CenterSettingsUtils.instance().updateLowBatSwitch(content);
                break;
            case MsgType.VERNO:
                String verno = AppUtils.getAppVersionName();
                MsgParser.instance().sendMsgByType(MsgType.VERNO, verno);
                break;
            case MsgType.RESET://重启TCP Client(终端接收到指令后重启，终端在后台重启，不会表现出来)
                TcpClient.instance().stopClient();
                TcpClient.instance().startClient();
                MsgParser.instance().sendMsgByType(headerType);
                break;
            case MsgType.CR:
                LogUtils.d("MsgType.CR");
                MsgParser.instance().sendMsgByType(MsgType.CR);
                AlarmTimer.setLocationAlarmStart(KApplication.sContext);
                AlarmTimer.setLocationAlarmStop(KApplication.sContext);
                break;
            case MsgType.POWEROFF:
                MsgParser.instance().sendMsgByType(MsgType.POWEROFF);
                KApplication.sContext.sendBroadcast(new Intent(ReceiverConstant.ACTION_CMD_POWER_OFF));
                break;
            case MsgType.REMOVE://摘掉报警开关
                MsgParser.instance().sendMsgByType(headerType);
                CenterSettingsUtils.instance().updateTakenOffSwitch(content);
                break;
            case MsgType.REMOVESMS://摘掉短信报警开关(和楼上的属于并列关系)
                MsgParser.instance().sendMsgByType(headerType);
                CenterSettingsUtils.instance().updateTakenOffSMSSwitch(content);
                break;
            case MsgType.PEDO:
                MsgParser.instance().sendMsgByType(headerType);
                StepUtils.parseStepSwitch(content);
                break;
            case MsgType.WALKTIME:
                MsgParser.instance().sendMsgByType(MsgType.ANY);
                StepUtils.parseStepWalkingTime(content);
                break;
            case MsgType.ANY://do nothing
                break;
            case MsgType.SLEEPTIME://翻转检测时间段设置
                MsgParser.instance().sendMsgByType(MsgType.ANY);
                StepUtils.parseTurnOverTimes(content);
                break;
            case MsgType.SILENCETIME:
                MsgParser.instance().sendMsgByType(MsgType.SILENCETIME);
                SilenceTimeUtils.initSilenceTime(SilenceTimeUtils.getSilenceTimeContent(), content);
                break;
            case MsgType.FIND:
                LogUtils.e("查找手机。。。");
                MsgParser.instance().sendMsgByType(MsgType.FIND);
                FindPhoneDialog.pull(ctx);
                break;
            case MsgType.FLOWER:
                break;
            case REMIND:
                MsgParser.instance().sendMsgByType(headerType);
                try {
                    RemindUtils.parseAndSaveAlarm(content);
                    RemindUtils.setNearAlarm();
                    ToastUtils.showShortSafe("闹钟设置成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.e("闹钟解析失败 " + e);
                }
                break;
            case TK:
                TalkingUtils.parseTkData(content, contentBytes);
                break;
            case MsgType.TKQ:
                break;
            case MsgType.WHITELIST2:
                MsgParser.instance().sendMsgByType(headerType);
                EventBus.getDefault().post(new WhiteListEvent(WhiteListEvent.Type.WHITELIST2, content));
                break;
            case MsgType.MESSAGE://对讲的表情消息
                //存入数据库
                content = new String(contentBytes, GlobalSettings.instance().getChaset());
                LogUtils.e("收到对讲表情Uncode " + content);
                MsgParser.instance().sendMsgByType(headerType);
                TalkingUtils.parseEmotionMessage(content);
                break;
            case MsgType.WHITELIST1:
                MsgParser.instance().sendMsgByType(headerType);
                EventBus.getDefault().post(new WhiteListEvent(WhiteListEvent.Type.WHITELIST1, content));
                break;
            case MsgType.PHB://设置电话本
                MsgParser.instance().sendMsgByType(MsgType.PHB);
                ContactorsUtils.paresePHB(content);

                break;
            case MsgType.PHB2://设置电话本 2
                MsgParser.instance().sendMsgByType(MsgType.PHB2);
                ContactorsUtils.paresePHB2(content);
                break;
            case MsgType.PHL://设置电话本+白名单
                MsgParser.instance().sendMsgByType(headerType);
                ContactorsUtils.paresePHL(content);
                break;
            case MsgType.profile:
                MsgParser.instance().sendMsgByType(headerType);
                ProfileUtils.saveProfileMode(content);
                ProfileUtils.setProfile(content);
                break;
            case MsgType.FALLDOWN:
                break;
            case MsgType.rcapture:
                MsgParser.instance().sendMsgByType(headerType);//先回复
                LogUtils.d("MsgType.rcapture: start capture");
                CaptureUtils.instance().capture(); //拍照完自动上传
                break;
            case MsgType.hrtstart:
//                HRUtils.parseAndHandle(content);
                break;
            case MsgType.heart:
                break;
            case MsgType.WIFI_SYNC://Wifi同步功能
                MsgParser.instance().sendMsgByType(headerType);
                WifiSyncManager.instance().parseWifiSync(content);
                break;
            case MsgType.EASE://获取环信账号
                EaseMobManager.instance().parseEase(content);
                break;
            case MsgType.MEMBERS://获取环信好友列表
                EaseMobManager.instance().parseEaseFriends(content);
                break;
            case MsgType.TALK://环信视频语音通话授权接口
                EaseMobManager.instance().parseConversation(content);
                break;
            case MsgType.DLT://终端自动解绑指令
                //do nothing
                break;
            case MsgType.CID://生成绑定所需的二维码
                QrCodeUtils.parseCID(contentBytes);
                break;

            default:
                LogUtils.e("解析异常 ： " + (msg == null ? "msg = null " : msg.getSourceDataString()));
                break;
        }
    }

    private class ParseSourceDataBytes {
        private boolean isNullResult;
        private TcpMsg msg;
        private String headerType;
        private String content;
        private byte[] contentBytes;

        public ParseSourceDataBytes(TcpMsg msg) {
            this.msg = msg;
        }

        boolean isNullResult() {
            return isNullResult;
        }

        public String getHeaderType() {
            return headerType;
        }

        public String getContent() {
            return content;
        }

        public byte[] getContentBytes() {
            return contentBytes;
        }

        public ParseSourceDataBytes invoke() {
            byte[] sourceDataBytes = msg.getSourceDataBytes();//原生字节数组
            int sourceDataLen = sourceDataBytes.length;
            LogUtils.e("sourceDataBytes " + Arrays.toString(sourceDataBytes));
            int indexComma = MsgParser.instance().indexOfStrInBytes(sourceDataBytes, GlobalSettings.MSG_CONTENT_SEPERATOR);//第一个逗号的索引值
            LogUtils.e("indexComma " + indexComma);
            int contentBytesStart = 0;
            int contentBytesIndexEnd = 0;
            boolean hasContent = indexComma >= 0;
            byte[] headerBytes;
            int headerBytesStart, headerBytesEnd;
            if (!hasContent) {//没找到逗号
                contentBytes = null;
                headerBytesStart = 1;
                headerBytesEnd = sourceDataLen - 2;// 从1到length-2位置
            } else {
                contentBytesStart = indexComma + 1;
                contentBytesIndexEnd = sourceDataLen - 2;
                contentBytes = new byte[contentBytesIndexEnd - contentBytesStart + 1];
                //实现将数组复制到其他数组中，把从索引0开始的contentBytes.length个数据复制到目标的索引为0的位置上
                System.arraycopy(sourceDataBytes, contentBytesStart, contentBytes, 0, contentBytes.length);
                headerBytesStart = 1;
                headerBytesEnd = indexComma - 1;
            }
            headerBytes = new byte[headerBytesEnd - headerBytesStart + 1];
            LogUtils.e("contentBytes " + Arrays.toString(contentBytes));
            System.arraycopy(sourceDataBytes, headerBytesStart, headerBytes, 0, headerBytes.length);
            LogUtils.e("headerBytes " + Arrays.toString(headerBytes));
            String charsetName = GlobalSettings.instance().getConfig().getCharsetName();
            String headerDataString = new String(headerBytes, Charset.forName(charsetName));
            LogUtils.e("headerDataString " + headerDataString);
            String[] segment = MsgParser.instance().getRecvHeaderSegments(headerDataString);
            if (ArrayUtils.isEmpty(segment)) {
                LogUtils.e("头部解析为空...");
                isNullResult = true;
                return this;
            }
            LogUtils.d("segment " + Arrays.toString(segment));//[product , id , len , content]
            if (segment.length != 4) {
                LogUtils.e("头部解析格式错误....");
                isNullResult = true;
                return this;
            }
            headerType = segment[segment.length - 1];
            String lenStr = segment[2];
            int len = Integer.parseInt(lenStr, 16);
            LogUtils.i("len " + len);
            LogUtils.i("headerType " + headerType);
            if (TextUtils.isEmpty(headerType)) {
                LogUtils.e("头部类型解析为空...");
                isNullResult = true;
                return this;
            }
            headerType = headerType.trim();
            if (null != contentBytes) {
                content = new String(contentBytes, Charset.forName(charsetName));
            }
            isNullResult = false;
            return this;
        }
    }
}

package com.android.fisewatchlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.android.fisewatchlauncher.acty.FindPhoneDialog;
import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.client.msg.MsgType;
import com.android.fisewatchlauncher.constant.FileConstant;
import com.android.fisewatchlauncher.constant.ReceiverConstant;
import com.android.fisewatchlauncher.entity.dao.PhoneContractor;
import com.android.fisewatchlauncher.entity.msg.TcpMsg;
import com.android.fisewatchlauncher.entity.wifi.WifiSyncBean;
import com.android.fisewatchlauncher.function.alarm.AlarmEntity;
import com.android.fisewatchlauncher.function.alarm.AlarmTimer;
import com.android.fisewatchlauncher.function.alarm.RemindUtils;
import com.android.fisewatchlauncher.function.media.MediaTransformUtils;
import com.android.fisewatchlauncher.function.wifi.WifiSyncManager;
import com.android.fisewatchlauncher.parser.MsgParser;
import com.android.fisewatchlauncher.prenster.dao.PhoneContactorDaoUtils;
import com.android.fisewatchlauncher.utils.FileIOUtils;
import com.android.fisewatchlauncher.utils.FileUtils;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.PhoneUtils;
import com.android.fisewatchlauncher.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.android.fisewatchlauncher.client.msg.MsgType.TK;
import static com.android.fisewatchlauncher.constant.ReceiverConstant.EXTRA_TEST_FLAG_LOCATION_STATE;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/19
 * @time 20:12
 */
public class TestReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtils.e(action);
        switch (action) {
            case ReceiverConstant.ACTION_CMD_TEST_FIND_PHONE:
                ToastUtils.showShort("查找手机");
                FindPhoneDialog.pull(context);
                break;

            case ReceiverConstant.ACTION_CMD_TEST_RESET_PHONE:
                ToastUtils.showShort("恢复出厂设置");
                break;

            case ReceiverConstant.ACTION_CMD_TEST_CONTACTOR:
                ToastUtils.showShort("联系人设置");
                PhoneContractor contractor;
                List<PhoneContractor> contractors = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    contractor = new PhoneContractor("mare" + i, String.valueOf(13189705780L + i));
                    contractors.add(contractor);
                }
                PhoneContactorDaoUtils.instance().updateAllPhoneContactor(contractors);
                break;

            case ReceiverConstant.ACTION_CMD_TEST_WHITELIST:
                ToastUtils.showShort("白名单设置");
                FindPhoneDialog.pull(context);
                break;
            case ReceiverConstant.ACTION_CMD_TEST_VOICE_UPLOAD:
                ToastUtils.showShort("语音下载测试");
//                String srcPath = FileConstant.WETCHAT_VOICE_UPLOAD_PATH + SRC_DATA;
//                String path = FileConstant.WETCHAT_VOICE_UPLOAD_PATH + trans2Upload;
                String talkingPath = FileConstant.TALKING_DOWNLOAD_FILE_PATH +
                        "talking" + ".rd";//原生字节数组
                String talkingUpload = FileConstant.TALKING_DOWNLOAD_FILE_PATH +
                        "talkingUpload" + ".rd";//转成要上传的字节数组
                String talkingUploadParseTmp = FileConstant.TALKING_DOWNLOAD_FILE_PATH +
                        "talkingUploadParseTmp" + ".rd";//再把要上传的转成要显示的字节数组 保存到临时文件
                String talkingUploadParse = FileConstant.TALKING_DOWNLOAD_FILE_PATH +
                        "talkingUploadParse" + ".amr";//再转成amr文件
//                byte[] srcBytes = MediaTransformUtils.getBytesRaw(R.raw.talking);
                String srcTalkingPath = intent.getStringExtra(ReceiverConstant.EXTRA_CMD_TEST_VOICE);
                byte[] srcBytes = MediaTransformUtils.file2bytes(srcTalkingPath);
                String srcBuffer = Arrays.toString(srcBytes);
                FileUtils.writeFile(talkingPath, srcBuffer);
                byte[] trans2UploadData = MediaTransformUtils.getDataUpload(srcBytes);
                String buffer = Arrays.toString(trans2UploadData);
                FileUtils.writeFile(talkingUpload, buffer);

                byte[] talkingUploadParseBytes = MediaTransformUtils.getDataDisplay(trans2UploadData);//上传的转成要显示的字节数组
                String talkingUploadParseTmpBuffer = Arrays.toString(talkingUploadParseBytes);
                FileUtils.writeFile(talkingUploadParseTmp, talkingUploadParseTmpBuffer);//把要上传的转成要显示的字节数组 保存到临时文件

                FileUtils.writeFile(talkingUploadParse, talkingUploadParseBytes);//将要显示的字节数组写到音频文件
                MsgParser.instance().sendMsgBytesByType(TK, trans2UploadData, new TcpMsg.SendCallBack() {
                    @Override
                    public void onSuccessSend(TcpMsg msg) {

                    }

                    @Override
                    public void onErrorSend(TcpMsg msg) {

                    }
                });
                break;
            case ReceiverConstant.ACTION_CMD_TEST_IMG:
                ToastUtils.showShort("图片上传下载测试");
                String srcImgPath = intent.getStringExtra(ReceiverConstant.EXTRA_CMD_TEST_IMG);
                LogUtils.e("要测试的图片路径 " + srcImgPath);
                byte[] srcImgBytes = FileIOUtils.readFile2BytesByMap(srcImgPath);
                byte[] imgUploadBytes = MediaTransformUtils.getDataUpload(srcImgBytes);
                byte[] imgDisplayBytes = MediaTransformUtils.getDataDisplay(imgUploadBytes);
                String imgSrcPath = FileConstant.PHOTO_CAPTURE_OUTPUT_PATH + "imgSrcPath" + ".rd";//原生图片字节数组
                String imgUploadPath = FileConstant.PHOTO_CAPTURE_OUTPUT_PATH + "imgUploadPath" + ".rd";//要上传的图片字节数组
                String imgDisplayPath = FileConstant.PHOTO_CAPTURE_OUTPUT_PATH + "imgDisplayPath" + ".rd";//要要显示的图片字节数组
                String srcImgName = srcImgPath.substring(srcImgPath.lastIndexOf("/") + 1);
                LogUtils.e("srcImgName " + srcImgName);
                String imgSuffix = srcImgName.substring(srcImgName.indexOf(".") + 1);//截取图片格式
                LogUtils.e("imgSuffix " + imgSuffix);
                String imgDownloadPath = FileConstant.PHOTO_CAPTURE_OUTPUT_PATH + "imgDownloadPath" + "." + imgSuffix;//要对比的图片文件

                String imgSrcBuffer = Arrays.toString(srcImgBytes);
                String imgUploadBuffer = Arrays.toString(imgUploadBytes);
                String imgDisplayBuffer = Arrays.toString(imgDisplayBytes);

                FileUtils.writeFile(imgSrcPath, imgSrcBuffer);//原生 字符串到文件
                FileUtils.writeFile(imgUploadPath, imgUploadBuffer);//上传 字符串到文件
                FileUtils.writeFile(imgDisplayPath, imgDisplayBuffer);//显示 字符串到文件
                FileUtils.writeFile(imgDownloadPath, imgDisplayBytes);//写入 字节数组到图片
                break;
            case ReceiverConstant.ACTION_CMD_TEST_LOCATION:
                ToastUtils.showShort("定位测试");
                int locationFlag = intent.getIntExtra(EXTRA_TEST_FLAG_LOCATION_STATE, 0);
                switch (locationFlag) {
                    case 0://3分钟后取消定位指令
//                        FunctionLocManager.instance().onStop();
                        AlarmTimer.setLocationAlarmStop(context);//3分钟后取消
                        break;
                    case 1://开始定位指令
//                        FunctionLocManager.instance().satrtSingleLoacte();
                        AlarmTimer.setLocationAlarmStart(context);
                        break;
                    case 2://开始固定频率定位
                        AlarmTimer.startConfirmedFrequencyUpload(context);
                        break;
                    case 3://取消固定频率定位
                        AlarmTimer.cancelAlarmTimer(context, new AlarmEntity(AlarmEntity.Type.CONFIRMED_FREQUENCY_UPLOAD));
                        break;
                    case 4://立即取消定位指令
                        AlarmTimer.cancelAlarmTimer(context, new AlarmEntity(AlarmEntity.Type.LocateStop));
                        AlarmTimer.cancelAlarmTimer(context, new AlarmEntity(AlarmEntity.Type.LocateStart));
                        break;
                    default:
                        break;
                }

                break;

            case ReceiverConstant.ACTION_CMD_TEST_PHONE_CALL:
                ToastUtils.showShort("拨打电话测试");
                PhoneUtils.call("13189705783");
                break;

            case ReceiverConstant.ACTION_CMD_TEST_CLOCK:
                String clockStr = intent.getStringExtra(ReceiverConstant.EXTRA_TEST_FLAG_CLOCK);
//                String clockStr = "10:12-1-3-0100000,11:03-1-3-0110010,14:03-1-3-0111110";
                ToastUtils.showShort("闹钟测试");
                RemindUtils.parseAndSaveAlarm(clockStr);
                break;
            case ReceiverConstant.ACTION_CMD_TEST_WIFI:
                String wifis = intent.getStringExtra(ReceiverConstant.EXTRA_TEST_FLAG_WIFI);
                ToastUtils.showShort("WIFI测试");
                String[] wifiPwds = TextUtils.split(wifis, GlobalSettings.MSG_CONTENT_SEPERATOR);
//                WifiSyncBean bean = new WifiSyncBean("9D_3F", "12345678");
                WifiSyncBean bean = new WifiSyncBean(wifiPwds[0], wifiPwds[1]);
                LogUtils.e(bean.toString());
                WifiSyncManager.instance().startSync(bean);
                break;
            case ReceiverConstant.ACTION_CMD_TEST_EASE:
                String easeFlag = intent.getStringExtra(ReceiverConstant.EXTRA_TEST_FLAG_EASE);
                ToastUtils.showShort("环信测试");
                switch (easeFlag) {
                    case "friends":
                        MsgParser.instance().sendMsgByType(MsgType.MEMBERS);//获取好友列表
                        break;
                }
                break;
        }

    }
}

package com.android.fisewatchlauncher.function.talking;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.constant.FileConstant;
import com.android.fisewatchlauncher.constant.ReceiverConstant;
import com.android.fisewatchlauncher.entity.dao.WetMessage;
import com.android.fisewatchlauncher.entity.msg.TcpMsg;
import com.android.fisewatchlauncher.function.media.MediaTransformUtils;
import com.android.fisewatchlauncher.listener.TalkingMsgParseCallback;
import com.android.fisewatchlauncher.manager.StaticManager;
import com.android.fisewatchlauncher.parser.MsgParser;
import com.android.fisewatchlauncher.prenster.dao.TalkingDaoUtils;
import com.android.fisewatchlauncher.utils.DigitalConvert;
import com.android.fisewatchlauncher.utils.FileIOUtils;
import com.android.fisewatchlauncher.utils.FileUtils;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.MediaManager;
import com.android.fisewatchlauncher.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.android.fisewatchlauncher.client.msg.MsgType.TK;

/**
 * @author mare
 * @Description:TODO 对讲辅助工具类
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/10/18
 * @time 11:29
 */
public class TalkingUtils {
    private static final String src = "http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg";

    public static List<WetMessage> buildTalkingData() {
        List<WetMessage> messageChatInfos = new ArrayList<>();
        WetMessage txtMsg = new WetMessage();//文字消息
        txtMsg.setConverstationType(WetMessage.ConverstationType.SINGLE);
        txtMsg.setMessageDirection(WetMessage.MessageDirection.REC);
        txtMsg.setMessageContentType(WetMessage.MessageContentType.TXT);
        txtMsg.setTimestamp(1508551178000l);
        txtMsg.setRecvStatus(WetMessage.RecvStatus.UNREAD);
        txtMsg.setMsgContent("亲，欢迎使用沸石聊天界面...");
        txtMsg.setHeaderUrl(src);
        messageChatInfos.add(txtMsg);

        WetMessage imgMsg = new WetMessage();//图片消息
        String tupian = "http://tupian.enterdesk.com/2014/mxy/11/2/1/12.jpg";
        imgMsg.setConverstationType(WetMessage.ConverstationType.SINGLE);
        imgMsg.setMessageDirection(WetMessage.MessageDirection.SEND);
        imgMsg.setMessageContentType(WetMessage.MessageContentType.IMG);
        imgMsg.setTimestamp(1508551212000l);
        imgMsg.setSendStatus(WetMessage.SendStatus.SENDING);
        imgMsg.setHeaderUrl(tupian);
        imgMsg.setImgUrl("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1508729544&di=d0cd753058fecc187073bf263c96d719&src=http://img3.duitang.com/uploads/blog/201510/10/20151010224914_s2F3j.thumb.700_0.jpeg");
        messageChatInfos.add(imgMsg);

        WetMessage audioMsg = new WetMessage();//音频消息
        audioMsg.setConverstationType(WetMessage.ConverstationType.SINGLE);
        audioMsg.setMessageDirection(WetMessage.MessageDirection.REC);
        audioMsg.setMessageContentType(WetMessage.MessageContentType.AUDIO);
        audioMsg.setRecvStatus(WetMessage.RecvStatus.UNREAD);
        audioMsg.setTimestamp(1508551356000l);
        audioMsg.setAudioPath("http://www.trueme.net/bb_midi/welcome.wav");
        audioMsg.setHeaderUrl(src);
        messageChatInfos.add(audioMsg);
//
//        WetMessage videoMsg = new WetMessage();//视频消息
//        videoMsg.setConverstationType(WetMessage.ConverstationType.SINGLE);
//        videoMsg.setMessageDirection(WetMessage.MessageDirection.SEND);
//        videoMsg.setMessageContentType(WetMessage.MessageContentType.VIDEO);
//        videoMsg.setSendStatus(WetMessage.SendStatus.FAILURE);
//        videoMsg.setTimestamp(1508552143000l);
//        videoMsg.setVideoUrl("");
//        videoMsg.setHeaderUrl(tupian);
//        messageChatInfos.add(videoMsg);

//        WetMessage timeLineMsg = new WetMessage();//音频消息
//        timeLineMsg.setConverstationType(WetMessage.ConverstationType.SINGLE);
//        timeLineMsg.setShowTimeLine(true);//时间线
//        timeLineMsg.setMessageDirection(WetMessage.MessageDirection.SEND);//时间线没有发送接收状态
//        videoMsg.setMessageContentType(WetMessage.MessageContentType.AUDIO);
//        timeLineMsg.setSendStatus(WetMessage.SendStatus.SENDING);//时间线没有发送接收状态
//        timeLineMsg.setTimestamp(1508552246000l);
//        timeLineMsg.setAudioPath("http://www.trueme.net/bb_midi/welcome.wav");
//        messageChatInfos.add(timeLineMsg);

        return messageChatInfos;
    }

    public static String getTimeLineString(long timestamp) {
        return TimeUtils.format(timestamp, "MM月dd日 HH:mm:ss");
    }

    public static WetMessage buildFakeReply() {
        WetMessage message = new WetMessage();
        message.setConverstationType(WetMessage.ConverstationType.SINGLE);
        message.setMessageDirection(WetMessage.MessageDirection.REC);
        message.setMessageContentType(WetMessage.MessageContentType.TXT);
        message.setRecvStatus(WetMessage.RecvStatus.READ);
        message.setMsgContent("这是模拟消息回复");
        message.setHeaderUrl("http://tupian.enterdesk.com/2014/mxy/11/2/1/12.jpg");
        return message;
    }

    /**
     * TODO 解析来自服务器的语音消息
     *
     * @param contentBytes
     * @param callback
     */
    public static void parseTalkingDataFromServer(byte[] contentBytes, final TalkingMsgParseCallback callback) {
        byte[] dataDisplay = null;
        try {
            dataDisplay = MediaTransformUtils.getDataDisplay(contentBytes);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("getDataDisplay " + e);
        }
        String path = FileConstant.TALKING_DOWNLOAD_FILE_PATH +
                TimeUtils.getCurrentTime() + FileConstant.VOICE_SUFFIX_AMR;
        boolean isSuccess = false;
        try {
            LogUtils.e("parseTalkingDataFromServer " + path);
//            LogUtils.e("contentBytes " + Arrays.toString(contentBytes));
            Log.e("TalkingUtils", "TalkingUtils contentBytes " + Arrays.toString(contentBytes));
            Log.e("TalkingUtils", "TalkingUtils dataDisplay " + Arrays.toString(dataDisplay));
            isSuccess = FileUtils.writeFile(path, contentBytes);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("语音解析异常 " + e);
            FileUtils.deleteFile(path);//解析错误就删除语音数据
        } finally {
            callback.onParseResult(isSuccess, path);
        }
    }

    public static void uploadTalkingData2Server(final WetMessage message, TcpMsg.SendCallBack callBack) {
        byte[] srcBytes = MediaTransformUtils.file2bytes(message.getAudioPath());
//        byte[] srcBytes = MediaTransformUtils.getBytesRaw(R.raw.test_voice);
        byte[] trans2UploadData = MediaTransformUtils.getDataUpload(srcBytes);
        MsgParser.instance().sendMsgBytesByType(TK, trans2UploadData, callBack);
    }

    /**
     * 将测试文件字符串转成音频文件
     *
     * @param testtestPath
     */
    public static void readByteStringFromFile(String testtestPath) {
        String talkingUploadParseBuffer = FileIOUtils.readFile2String(testtestPath);
        String talkingUploadParseString = talkingUploadParseBuffer.substring(1, talkingUploadParseBuffer.length() - 2);//去掉"["和"]"
        String[] talkingUploadParseArray = TextUtils.split(talkingUploadParseString, ",");
        ByteBuffer parseBuffer = ByteBuffer.allocate(talkingUploadParseArray.length);
        byte temp;
        for (String str : talkingUploadParseArray) {
            if ("[".equals(str)
                    || "]".equals(str)
                    || ",".equals(str)
                    || TextUtils.isEmpty(str)) continue;
            LogUtils.e("str " + str);
            int parseIntData = Integer.valueOf(str.trim());
            temp = (byte) parseIntData;
            parseBuffer.put(temp);
        }
        parseBuffer.flip();
        int parseBufferLimit = parseBuffer.limit();
        byte[] talkingUploadParseBytes = new byte[parseBufferLimit];
        parseBuffer.get(talkingUploadParseBytes);
    }

    /**
     * TODO 解析 TK数据
     *
     * @param content
     * @param contentBytes
     */
    public static void parseTkData(final String content, final byte[] contentBytes) {
        if (null == contentBytes) {
            return;
        }
        if (content.trim().length() == 1) {//平台回复[CS*YYYYYYYYYY*LEN*TK,接收结果]
            int lastResult = Integer.parseInt(content);
            LogUtils.e("上次语音对讲接收状态 " + lastResult);
        } else {//[CS*YYYYYYYYYY*LEN*TK,AMR 格式音频数据]

            TalkingUtils.parseTalkingDataFromServer(contentBytes, new TalkingMsgParseCallback() {
                @Override
                public void onParseResult(boolean isSuccess, String audioPath) {
                    if (isSuccess) {

//                        testVoice(audioPath, contentBytes);//调试用的
                        WetMessage audioMsg = new WetMessage();//音频消息
                        audioMsg.setConverstationType(WetMessage.ConverstationType.SINGLE);
                        audioMsg.setMessageDirection(WetMessage.MessageDirection.REC);
                        audioMsg.setMessageContentType(WetMessage.MessageContentType.AUDIO);
                        audioMsg.setRecvStatus(WetMessage.RecvStatus.UNREAD);
                        audioMsg.setTimestamp(System.currentTimeMillis());
                        audioMsg.setAudioPath(audioPath);
                        audioMsg.setHeaderUrl(src);

                        //保存到数据库
                        TalkingDaoUtils.instance().updateMessage(audioMsg);
                        //EventBus通知桌面微聊图标小红点(对讲UI会消除红点)
                        updateWetChatBage();
                        //更新到消息界面
                        EventBus.getDefault().post(audioMsg);

                    }
                    MsgParser.instance().sendMsgByType(TK, String.valueOf(isSuccess ? 1 : 0));
                }
            });
        }
    }

    private static void testVoice(String audioPath, byte[] contentBytes) {
        String talkingPath = FileConstant.TALKING_DOWNLOAD_FILE_PATH +
                "parseTalking" + ".rd";
        String srcBuffer = Arrays.toString(contentBytes);
        FileUtils.writeFile(talkingPath, srcBuffer);//调试保存录音到文件
        MsgParser.instance().sendMsgBytesByType(TK, contentBytes);
        Intent testIntent = new Intent(ReceiverConstant.ACTION_CMD_TEST_VOICE_UPLOAD);
        testIntent.putExtra(ReceiverConstant.EXTRA_CMD_TEST_VOICE, audioPath);
        KApplication.sContext.sendBroadcast(testIntent);
    }

    public static void parseEmotionMessage(String content) {
        String recvMsg = DigitalConvert.hexNone0x2String(content);
        LogUtils.e("收到对讲表情信息 " + recvMsg);

        WetMessage txtMsg = new WetMessage();//文字消息
        txtMsg.setConverstationType(WetMessage.ConverstationType.SINGLE);
        txtMsg.setMessageDirection(WetMessage.MessageDirection.REC);
        txtMsg.setMessageContentType(WetMessage.MessageContentType.TXT);
        txtMsg.setTimestamp(System.currentTimeMillis());
        txtMsg.setRecvStatus(WetMessage.RecvStatus.UNREAD);
        txtMsg.setMsgContent(recvMsg);
        txtMsg.setHeaderUrl(src);

        //添加到数据库
        TalkingDaoUtils.instance().updateMessage(txtMsg);
        //EventBus通知桌面微聊图标小红点(对讲UI会消除红点)
        updateWetChatBage();
        //EventBus通知对讲UI
        EventBus.getDefault().post(txtMsg);

    }

    private static void updateWetChatBage() {
        StaticManager.instance().putHomeBage("WetChat");
        MediaManager.findDevice(R.raw.ququ, false);
    }
}

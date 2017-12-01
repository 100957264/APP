package com.android.fisewatchlauncher.function.wetchat;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.acty.EaseContactListActivity;
import com.android.fisewatchlauncher.acty.VideoEaseCallActivity;
import com.android.fisewatchlauncher.acty.VoiceEaseCallActivity;
import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.client.msg.MsgType;
import com.android.fisewatchlauncher.entity.dao.EaseFriend;
import com.android.fisewatchlauncher.event.EaseAuthEvent;
import com.android.fisewatchlauncher.event.EaseFriendUpdate;
import com.android.fisewatchlauncher.manager.PreferControler;
import com.android.fisewatchlauncher.parser.MsgParser;
import com.android.fisewatchlauncher.prenster.dao.EaseFriendDaoUtils;
import com.android.fisewatchlauncher.receiver.EaseCallReceiver;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.ToastUtils;
import com.android.fisewatchlauncher.utils.UnicodeUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mare
 * @Description:TODO 环信所有逻辑管理者
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/6
 * @time 11:22
 */
public class EaseMobManager {
    private EaseMobManager() {
    }

    private static class SingletonHolder {
        private static final EaseMobManager INSTANCE = new EaseMobManager();
    }

    public static EaseMobManager instance() {
        return SingletonHolder.INSTANCE;
    }

    public void parseEase(String usrPwd) {
        LogUtils.e("环信账号密码.. " + usrPwd);
        if (TextUtils.isEmpty(usrPwd)) {
            return;
        }
        String[] strs = TextUtils.split(usrPwd, GlobalSettings.MSG_CONTENT_SEPERATOR);
        PreferControler.instance().setEaseId(strs[0]);
        PreferControler.instance().setEasePwd(strs[1]);
        LogUtils.e("环信登陆usr pwd " + Arrays.toString(strs));
        login(strs[0], strs[1]); //登陆环信
    }

    public void parseEaseFriends(String content) {
        LogUtils.e("ease friends .. " + content);
        if (TextUtils.isEmpty(content)) {
            return;
        }
        content = UnicodeUtils.unicodeNo0xuToString(content);
        LogUtils.e("ease friends str " + content);
        String[] idNames = TextUtils.split(content, GlobalSettings.MSG_CONTENT_SEPERATOR);
        LogUtils.e(Arrays.toString(idNames));
        int len = idNames.length / 3;
        List<EaseFriend> easeFriedns = new ArrayList<>();
        EaseFriend friend;
        String friendId, nick, phone;
        for (int i = 0; i < len; i++) {
            friendId = idNames[i * 3];
            nick = idNames[i * 3 + 1];
            phone = idNames[i * 3 + 2];
            friend = new EaseFriend(friendId, nick, phone);
            easeFriedns.add(friend);
        }
        LogUtils.e("ease friends list " + easeFriedns.toString());
        EaseFriendDaoUtils.instance().updateFriendList(easeFriedns);//更新所有好友

        //TODO 更新好友列表界面
        EventBus.getDefault().post(new EaseFriendUpdate());
    }

    public void init(Application app) {
        EMOptions options = initChatOptions();
//        boolean isSdkInited = EaseUI.getInstance().init(app, options);
//        if (!isSdkInited) {
//            return;
//        }
        EMClient.getInstance().init(app, options);
        EMClient.getInstance().setDebugMode(true);
        setCallOptions();
        registerCallListener(app);
        EMClient.getInstance().addConnectionListener(connectionListener);
        registerMessageListener();
    }

    protected EMMessageListener messageListener = new EMMessageListener() {

        /**
         *收到的新消息集合
         * @param messages
         */
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // 循环遍历当前收到的消息
            for (EMMessage message : messages) {
                String curChatId = "1200";
                if (message.getFrom().equals(curChatId)) {
                    // 设置消息为已读
                } else {
                    // 如果消息不是当前会话的消息发送通知栏通知
                }

            }
        }

        /**
         * 收到新的 CMD 消息
         * @param messages
         */
        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            // 透传消息
            for (int i = 0; i < messages.size(); i++) {
                EMMessage cmdMessage = messages.get(i);
                EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
            }

        }

        /**
         * 收到新的已读回执
         *
         * @param messages 收到消息已读回执
         */
        @Override
        public void onMessageRead(List<EMMessage> messages) {

        }

        /**
         * 收到新的发送回执
         *
         * @param messages 收到发送回执的消息集合
         */
        @Override
        public void onMessageDelivered(List<EMMessage> messages) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }

        /**
         * 消息的状态改变
         * @param message 发生改变的消息
         * @param change 包含改变的消息
         */
        @Override
        public void onMessageChanged(EMMessage message, Object change) {

        }
    };

    private void registerMessageListener() {
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    private EMConnectionListener connectionListener = new EMConnectionListener() {
        @Override
        public void onDisconnected(int error) {
            String errorStr = "";
            if (error == EMError.USER_REMOVED) {
                errorStr = "账号被移除...";
            } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                errorStr = "账号被其他设备登陆...";
            } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {
                errorStr = "账号功能被禁用...";
            } else if (error == EMError.USER_KICKED_BY_CHANGE_PASSWORD) {
                errorStr = "账号被改密强制下线...";
            } else if (error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
                errorStr = "账号被其他设备踢下线...";
            }
            LogUtils.e("环信断开了服务器!!! onDisconnect " + error + " 错误详情 " + errorStr);
        }

        @Override
        public void onConnected() {
            LogUtils.d("环信连上了服务器!!!");
        }
    };
    private EaseCallReceiver callReceiver = new EaseCallReceiver();
    ;//环信语音视频通话广播

    private void registerCallListener(Application app) {
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        app.registerReceiver(callReceiver, callFilter);
    }

    private void setCallOptions() {
        // TODO: set Call options
        // min video kbps
        int minBitRate = PreferControler.instance().getCallMinVideoKbps();
        if (minBitRate != -1) {
            EMClient.getInstance().callManager().getCallOptions().setMinVideoKbps(minBitRate);
        }

        // max video kbps
        int maxBitRate = PreferControler.instance().getCallMaxVideoKbps();
        if (maxBitRate != -1) {
            EMClient.getInstance().callManager().getCallOptions().setMaxVideoKbps(maxBitRate);
        }

        // max frame rate
        int maxFrameRate = PreferControler.instance().getCallMaxFrameRate();
        if (maxFrameRate != -1) {
            EMClient.getInstance().callManager().getCallOptions().setMaxVideoFrameRate(maxFrameRate);
        }

        // audio sample rate
        int audioSampleRate = PreferControler.instance().getCallAudioSampleRate();
        if (audioSampleRate != -1) {
            EMClient.getInstance().callManager().getCallOptions().setAudioSampleRate(audioSampleRate);
        }

        //EMClient.getInstance().callManager().getVideoCallHelper().setPreferMovFormatEnable(true);

        // resolution
        String resolution = PreferControler.instance().getCallBackCameraResolution();
        if (resolution.equals("")) {
            resolution = PreferControler.instance().getCallFrontCameraResolution();
        }
        String[] wh = resolution.split("x");
        if (wh.length == 2) {
            try {
                EMClient.getInstance().callManager().getCallOptions().setVideoResolution(new Integer(wh[0]).intValue(), new Integer(wh[1]).intValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // enabled fixed sample rate
        boolean enableFixSampleRate = PreferControler.instance().isCallFixedVideoResolution();
        EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(enableFixSampleRate);

        // Offline call push
        EMClient.getInstance().callManager().getCallOptions().setIsSendPushIfOffline(true);
    }

    private EMOptions initChatOptions() {
        LogUtils.d("init HuanXin Options");

        EMOptions options = new EMOptions();
        //默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // set if you need read ack
        options.setRequireAck(true);
        // set if you need delivery ack
        options.setRequireDeliveryAck(false);

        /**
         * NOTE:你需要设置自己申请的Sender ID来使用Google推送功能，详见集成文档
         */
        // options.setFCMNumber("921300338324");
        //you need apply & set your own id if you want to use Mi push notification
        //options.setMipushConfig("2882303761517426801", "5381742660801");

//        options.setRestServer();
//        options.setIMServer();
//        options.setImPort();
//        options.setAppKey();
        options.allowChatroomOwnerLeave(false);
        options.setAutoLogin(true);//自动登陆true
        options.setDeleteMessagesAsExitGroup(false);
        options.setAutoAcceptGroupInvitation(true);
//        options.setAutoTransferMessageAttachments(false);
//        options.setAutoDownloadThumbnail(false);
        return options;
    }

    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    public boolean isVideoCalling;
    public boolean isVoiceCalling;

    public void autoLogin() {
        if (!isLoggedIn()) {
            String previousId = PreferControler.instance().getEaseId();
            String previousPwd = PreferControler.instance().getEasePwd();
            LogUtils.e("previousId " + previousId + " ,previousPwd " + previousPwd);
            if (TextUtils.isEmpty(previousId) || TextUtils.isEmpty(previousPwd)) {
                ToastUtils.showShortSafe("还没获取环信账号密码....");
                return;
            }
            login(previousId, previousPwd);
        }
    }

    public void login(final String myId, final String pwd) {
//        ToastUtils.showShortSafe("Id and Password :" + myId + "," + pwd);
        EMClient.getInstance().login(myId, pwd, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                LogUtils.e("mare", "登录环信聊天服务器成功！");
                String token = EMClient.getInstance().getAccessToken();
                LogUtils.e("usrid " + myId + " ,pwd " + pwd + " ,token " + token);
                //TODO 存储账号密码 和token
                PreferControler.instance().setEaseId(myId);
                PreferControler.instance().setEasePwd(pwd);
                PreferControler.instance().setEaseToken(token);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                LogUtils.e("mare", "登录聊天服务器失败！" + message);
            }
        });
    }

    /**
     * 注销环信相关
     */
    public void loginOut() {
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                LogUtils.d("mare", "注销环信成功！");
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
                LogUtils.d("mare", "注销环信失败！" + message);
            }
        });
    }

    /**
     * 拨打语音通话
     *
     * @param easeFriend
     */
    public void makeVoiceCall(EaseFriend easeFriend) {
        if (!isLoggedIn()) {
            LogUtils.e("还没登陆环信,开始登陆....");
//            MsgParser.instance().sendMsgByType();//请求环信账号密码
            autoLogin();
            return;
        }

        if (!EMClient.getInstance().isConnected()) {
            ToastUtils.showShortSafe(R.string.not_connect_to_server);
        } else {
            KApplication.sContext.startActivity(
                    new Intent(KApplication.sContext, VoiceEaseCallActivity.class)
                            .putExtra(EaseContactListActivity.EXTRA_KEY_EASE_FRIEND, easeFriend)
                            .putExtra("isComingCall", false)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    /**
     * 拨打视频通话
     *
     * @param easeFriend
     */
    public void makeVideoCall(EaseFriend easeFriend) {
        if (!isLoggedIn()) {
            LogUtils.e("还没登陆环信,开始登陆....");
//            MsgParser.instance().sendMsgByType();//请求环信账号密码
            autoLogin();
            return;
        }
//        if (!EMClient.getInstance().isConnected()) {
//            ToastUtils.showShortSafe(R.string.not_connect_to_server);
//        } else {
        KApplication.sContext.startActivity(
                new Intent(KApplication.sContext, VideoEaseCallActivity.class)
                        .putExtra(EaseContactListActivity.EXTRA_KEY_EASE_FRIEND, easeFriend)
                        .putExtra("isComingCall", false)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//        }
    }

    /**
     * 解析通话授权结果
     *
     * @param content
     */
    public void parseConversation(String content) {
//        结果： 0 表示可正常通话， -1 表示余额不足， 不能通话
        if (TextUtils.isEmpty(content)) {
            return;
        }
        int resultCode = Integer.parseInt(content);
        EventBus.getDefault().post(new EaseAuthEvent(resultCode == 0));
    }

    public static final int CONVERSATION_VOICE = 1;
    public static final int CONVERSATION_VIDEO = 2;

    public void requestConversation(int conversationType) {
//        请求： 1 表示语音通话， 2 表示视频通话
        MsgParser.instance().sendMsgByType(MsgType.TALK, String.valueOf(conversationType));
    }

}

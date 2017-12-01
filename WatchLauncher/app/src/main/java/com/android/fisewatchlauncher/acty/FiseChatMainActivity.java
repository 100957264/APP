package com.android.fisewatchlauncher.acty;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.adapter.ChatAdapter;
import com.android.fisewatchlauncher.entity.dao.WetMessage;
import com.android.fisewatchlauncher.entity.dao.WetMessage.MessageDirection;
import com.android.fisewatchlauncher.event.TalkingSendStatusEvent;
import com.android.fisewatchlauncher.function.talking.ImgPreviewInfo;
import com.android.fisewatchlauncher.function.talking.TalkingUtils;
import com.android.fisewatchlauncher.manager.StaticManager;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.MediaManager;
import com.android.fisewatchlauncher.widget.EmotionInputDetector;
import com.jude.easyrecyclerview.EasyRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/1
 * @time 18:47
 */
public class FiseChatMainActivity extends BaseActivity {

    @Bind(R.id.chat_list)
    EasyRecyclerView chatList;
    @Bind(R.id.voice_text)
    TextView voiceText;
    @Bind(R.id.chat_function_container)
    LinearLayout chatFunctionContainer;

    private EmotionInputDetector mDetector;

    private ChatAdapter chatAdapter;
    private LinearLayoutManager layoutManager;
    private List<WetMessage> messageChatInfos = new ArrayList<>();
    //录音相关
    int animationRes = 0;
    int res = 0;
    AnimationDrawable animationDrawable = null;
    private ImageView animView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fise_chat_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initWidget();
    }

    private void initWidget() {

        mDetector = EmotionInputDetector.with(this)
                .bindToVoiceText(voiceText, chatFunctionContainer)
                .build();

        chatAdapter = new ChatAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(chatAdapter);
        chatList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        chatAdapter.handler.removeCallbacksAndMessages(null);
                        chatAdapter.notifyDataSetChanged();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        chatAdapter.handler.removeCallbacksAndMessages(null);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        chatAdapter.addItemClickListener(itemClickListener);
        LoadData();
    }

    /**
     * item点击事件
     */
    private ChatAdapter.onItemClickListener itemClickListener = new ChatAdapter.onItemClickListener() {
        @Override
        public void onHeaderClick(int position) {
            Toast.makeText(FiseChatMainActivity.this, "onHeaderClick", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onImageClick(View view, int position) {
            int location[] = new int[2];
            view.getLocationOnScreen(location);
            ImgPreviewInfo fullImageInfo = new ImgPreviewInfo();
            fullImageInfo.setLocationX(location[0]);
            fullImageInfo.setLocationY(location[1]);
            fullImageInfo.setWidth(view.getWidth());
            fullImageInfo.setHeight(view.getHeight());
            fullImageInfo.setImageUrl(messageChatInfos.get(position).getImgUrl());
            EventBus.getDefault().postSticky(fullImageInfo);
            startActivity(new Intent(FiseChatMainActivity.this, ImagePreviewActivity.class));
            overridePendingTransition(0, 0);
            updateMsgReadStatus(position);
        }

        @Override
        public void onVoiceClick(final ImageView imageView, final int position) {
            if (animView != null) {
                animView.setImageResource(res);
                animView = null;
            }

            switch (messageChatInfos.get(position).getMessageDirection()) {
                case REC:
                    animationRes = R.drawable.voice_left;
                    res = R.mipmap.icon_voice_left3;
                    break;
                case SEND:
                    animationRes = R.drawable.voice_right;
                    res = R.mipmap.icon_voice_right3;
                    break;
            }
            animView = imageView;
            animView.setImageResource(animationRes);
            animationDrawable = (AnimationDrawable) imageView.getDrawable();
            animationDrawable.start();
            MediaManager.playSound(messageChatInfos.get(position).getAudioPath(), new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    animView.setImageResource(res);
                    updateMsgReadStatus(position);
                }
            });
        }
    };

    /**
     * 更新未读消息为已读消息
     *
     * @param position
     */
    private void updateMsgReadStatus(int position) {
        WetMessage wetMessage = messageChatInfos.get(position);
        MessageDirection direction = wetMessage.getMessageDirection();
        LogUtils.d("direction : " + direction.toString());
        if (direction != MessageDirection.REC) {
            return;
        }
        messageChatInfos.get(position).setRecvStatus(WetMessage.RecvStatus.READ);//已读
        chatAdapter.notifyItemChanged(position);
        //chatList.scrollToPosition(pos);//跳转到更新的指定消息位置
    }

    /**
     * 构造聊天数据
     */
    private void LoadData() {
        messageChatInfos = TalkingUtils.buildTalkingData();
        chatAdapter.addAll(messageChatInfos);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgSendStatusUpdate(TalkingSendStatusEvent event) {
        String audioPath = event.audiopath;
        LogUtils.d("audioPath" + audioPath);
        int size = messageChatInfos.size();
        WetMessage msg;
        int itemModify = -1;
        for (int i = 0; i < size; i++) {
            msg = messageChatInfos.get(i);
            MessageDirection direction = msg.getMessageDirection();
            if (direction == MessageDirection.SEND) {
                String srcPath = msg.getAudioPath();
                LogUtils.d("srcPath" + srcPath);
                if (TextUtils.equals(audioPath, srcPath)) {
                    msg.setSendStatus(event.sendStatus);
                    itemModify = i;
                    break;
                }
            }
        }
        chatAdapter.notifyItemChanged(itemModify);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(final WetMessage messageChatInfo) {//接收/发送讲消息回调
        LogUtils.e("MessageEventBus " + messageChatInfo.toString());
        if (!messageChatInfos.contains(messageChatInfo)) {//避免重新加入
            if (messageChatInfo.getMessageDirection() == MessageDirection.REC
                    && messageChatInfo.getMessageContentType() == WetMessage.MessageContentType.TXT){
                messageChatInfo.setRecvStatus(WetMessage.RecvStatus.READ);//设为已读
            }
            messageChatInfos.add(messageChatInfo);
            chatAdapter.add(messageChatInfo);
        }
        chatList.scrollToPosition(chatAdapter.getCount() - 1);
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                messageChatInfo.setSendStatus(WetMessage.SendStatus.SUCCESS);
//                chatAdapter.notifyDataSetChanged();
//            }
//        }, 2000);
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                WetMessage fakeReply = TalkingUtils.buildFakeReply();
//                messageChatInfos.add(fakeReply);
//                chatAdapter.add(fakeReply);
//                chatList.scrollToPosition(chatAdapter.getCount() - 1);
//            }
//        }, 3000);
    }

    @Override
    public void onBackPressed() {
        if (!mDetector.interceptBackPress()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //每次进来就清除小红点数据
        StaticManager.instance().removeHomeBage("WetChat");

    }
}

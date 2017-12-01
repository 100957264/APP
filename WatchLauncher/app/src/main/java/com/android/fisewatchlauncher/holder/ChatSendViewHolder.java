package com.android.fisewatchlauncher.holder;

import android.os.Handler;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.adapter.ChatAdapter;
import com.android.fisewatchlauncher.entity.dao.WetMessage;
import com.android.fisewatchlauncher.function.talking.TalkingUtils;
import com.android.fisewatchlauncher.utils.DpUtil;
import com.android.fisewatchlauncher.utils.TimeUtils;
import com.android.fisewatchlauncher.widget.BubbleImageView;
import com.android.fisewatchlauncher.widget.BubbleLinearLayout;
import com.android.fisewatchlauncher.widget.GifTextView;
import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：Rance on 2016/11/29 10:47
 * 邮箱：rance935@163.com
 */
public class ChatSendViewHolder extends BaseViewHolder<WetMessage> {

    @Bind(R.id.chat_item_date)
    TextView chatItemDate;
    @Bind(R.id.chat_item_header)
    ImageView chatItemHeader;
    @Bind(R.id.chat_item_content_text)
    GifTextView chatItemContentText;
    @Bind(R.id.chat_item_content_image)
    BubbleImageView chatItemContentImage;
    @Bind(R.id.chat_item_fail)
    ImageView chatItemFail;
    @Bind(R.id.chat_item_progress)
    ProgressBar chatItemProgress;
    @Bind(R.id.chat_item_voice)
    ImageView chatItemVoice;
    @Bind(R.id.chat_item_layout_content)
    BubbleLinearLayout chatItemLayoutContent;
    @Bind(R.id.chat_item_voice_time)
    TextView chatItemVoiceTime;
    private ChatAdapter.onItemClickListener onItemClickListener;
    private Handler handler;
    private RelativeLayout.LayoutParams layoutParams;

    public ChatSendViewHolder(ViewGroup parent, ChatAdapter.onItemClickListener onItemClickListener, Handler handler) {
        super(parent, R.layout.item_chat_send);
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
        this.handler = handler;
        layoutParams = (RelativeLayout.LayoutParams) chatItemLayoutContent.getLayoutParams();
    }

    private void setTimeLine(WetMessage data) {
        boolean isShowTimeLine = data.isShowTimeLine();
        chatItemDate.setVisibility(isShowTimeLine ? View.VISIBLE : View.GONE);
        chatItemDate.setText(isShowTimeLine ? TalkingUtils.getTimeLineString(data.getTimestamp()) : "");
    }

    private void setVoiceLayoutVis(boolean vis) {
        chatItemVoice.setVisibility(vis ? View.VISIBLE : View.GONE);
        chatItemVoiceTime.setVisibility(vis ? View.VISIBLE : View.GONE);
    }

    private void setTxtLayoutVis(boolean vis) {
        chatItemContentText.setVisibility(vis ? View.VISIBLE : View.GONE);
        chatItemLayoutContent.setVisibility(vis ? View.VISIBLE : View.GONE);
    }

    private void setImgLayoutVis(boolean vis) {
        chatItemContentImage.setVisibility(vis ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setData(WetMessage data) {
        setTimeLine(data);
        Glide.with(getContext()).load(data.getHeaderUrl()).placeholder(R.drawable.emotion_aini).error(R.drawable.msg_state_fail_resend).into(chatItemHeader);
        chatItemHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onHeaderClick(getDataPosition());
            }
        });
        WetMessage.MessageContentType contentType = data.getMessageContentType();
        switch (contentType) {
            case TXT:
                chatItemContentText.setSpanText(handler, data.getMsgContent(), true);
                setVoiceLayoutVis(false);
                setTxtLayoutVis(true);
                setImgLayoutVis(false);
                TextPaint paint = chatItemContentText.getPaint();
                // 计算textview在屏幕上占多宽
                int len = (int) paint.measureText(chatItemContentText.getText().toString().trim());
                if (len < DpUtil.dp2px(getContext(), 200)) {
                    layoutParams.width = len + DpUtil.dp2px(getContext(), 30);
                } else {
                    layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                }
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                chatItemLayoutContent.setLayoutParams(layoutParams);
                break;
            case IMG:
                setVoiceLayoutVis(false);
                setTxtLayoutVis(false);
                setImgLayoutVis(true);
                Glide.with(getContext()).load(data.getImgUrl()).placeholder(R.drawable.emotion_aini).error(R.drawable.msg_state_fail_resend).into(chatItemContentImage);
                chatItemContentImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onImageClick(chatItemContentImage, getDataPosition());
                    }
                });
                layoutParams.width = DpUtil.dp2px(getContext(), 120);
                layoutParams.height = DpUtil.dp2px(getContext(), 48);
                chatItemLayoutContent.setLayoutParams(layoutParams);
                break;
            case AUDIO:
                setVoiceLayoutVis(true);
                setTxtLayoutVis(false);
                setImgLayoutVis(false);
                chatItemLayoutContent.setVisibility(View.VISIBLE);
                chatItemVoiceTime.setText(TimeUtils.formatTime(data.getVoiceTime()));
                chatItemLayoutContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onVoiceClick(chatItemVoice, getDataPosition());
                    }
                });
                layoutParams.width = DpUtil.dp2px(getContext(), 120);
                layoutParams.height = DpUtil.dp2px(getContext(), 48);
                chatItemLayoutContent.setLayoutParams(layoutParams);
                break;
            case VIDEO:
                break;
        }

        switch (data.getSendStatus()) {
            case SENDING:
                chatItemProgress.setVisibility(View.VISIBLE);
                chatItemFail.setVisibility(View.GONE);
                break;
            case FAILURE:
                chatItemProgress.setVisibility(View.GONE);
                chatItemFail.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                chatItemProgress.setVisibility(View.GONE);
                chatItemFail.setVisibility(View.GONE);
                break;
        }
    }
}

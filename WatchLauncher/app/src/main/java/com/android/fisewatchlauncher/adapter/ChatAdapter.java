package com.android.fisewatchlauncher.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.fisewatchlauncher.constant.MsgConstants;
import com.android.fisewatchlauncher.entity.dao.WetMessage;
import com.android.fisewatchlauncher.holder.ChatAcceptViewHolder;
import com.android.fisewatchlauncher.holder.ChatSendViewHolder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/1
 * @time 19:24
 */
public class ChatAdapter extends RecyclerArrayAdapter<WetMessage> {

    private onItemClickListener onItemClickListener;
    public Handler handler;

    public ChatAdapter(Context context) {
        super(context);
        handler = new Handler();
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        switch (viewType) {
            case MsgConstants.CHAT_ITEM_TYPE_LEFT:
                viewHolder = new ChatAcceptViewHolder(parent, onItemClickListener, handler);
                break;
            case MsgConstants.CHAT_ITEM_TYPE_RIGHT:
                viewHolder = new ChatSendViewHolder(parent, onItemClickListener, handler);
                break;
//            case MsgConstants.CHAT_ITEM_TYPE_TIME_LINE:
//                viewHolder = new ChatTimeLineHolder(parent, onItemClickListener, handler);
//                break;
        }
        return viewHolder;
    }

    @Override
    public int getViewType(int position) {
        WetMessage wetMessage = getAllData().get(position);
        WetMessage.MessageContentType contentType = wetMessage.getMessageContentType();
        WetMessage.MessageDirection direction = wetMessage.getMessageDirection();
        int itemType;
        /*if (contentType == WetMessage.MessageContentType.TIMELINE) {
            itemType = MsgConstants.CHAT_ITEM_TYPE_TIME_LINE;
        } else */
        if (direction == WetMessage.MessageDirection.REC) {
            itemType = MsgConstants.CHAT_ITEM_TYPE_LEFT;
        } else {
            itemType = MsgConstants.CHAT_ITEM_TYPE_RIGHT;
        }
        return itemType;
    }

    public void addItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onHeaderClick(int position);

        void onImageClick(View view, int position);

        void onVoiceClick(ImageView imageView, int position);
    }
}

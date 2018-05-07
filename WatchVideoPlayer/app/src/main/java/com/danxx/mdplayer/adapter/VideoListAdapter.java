package com.danxx.mdplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danxx.mdplayer.R;
import com.danxx.mdplayer.model.VideoBean;
import com.danxx.mdplayer.ui.VideoActivity;

import java.util.List;

/**
 * Created by B415 on 2018/3/5.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private Context mContext;
    private List<VideoBean> videoBeanList;
    private LayoutInflater inflater;

    public VideoListAdapter(Context context, List<VideoBean> videoBeanList) {
        this.mContext = context;
        this.videoBeanList = videoBeanList;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_videos_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
         holder.videoName.setText(videoBeanList.get(position).name);
         holder.videoSize.setText(videoBeanList.get(position).size);
         holder.videoDuration.setText(videoBeanList.get(position).length);
         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 VideoActivity.intentTo(mContext,videoBeanList.get(position).path,videoBeanList.get(position).name);
             }
         });
    }


    @Override
    public int getItemCount() {
        return videoBeanList.size() > 0 ?videoBeanList.size() : 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView videoDuration;
        public TextView videoSize;
        public TextView videoName;
        public ViewHolder(View itemView) {
            super(itemView);
            videoName =(TextView) itemView.findViewById(R.id.tvName);
            videoSize =(TextView) itemView.findViewById(R.id.tvSize);
            videoDuration =(TextView) itemView.findViewById(R.id.tvlength);
        }
    }
}

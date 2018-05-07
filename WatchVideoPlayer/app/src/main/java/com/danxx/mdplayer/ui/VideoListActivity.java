package com.danxx.mdplayer.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.danxx.mdplayer.R;
import com.danxx.mdplayer.adapter.VideoListAdapter;
import com.danxx.mdplayer.base.BaseActivity;
import com.danxx.mdplayer.model.VideoBean;

import java.util.ArrayList;
import java.util.List;


public class VideoListActivity extends BaseActivity {
    private String path;
    private static final int MSG_READ_FINISH = 1;
    private VideoListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView videoListView;
    Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    String[] mediaColumns = { MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.VideoColumns.DURATION
           };
    private static final  int SCAN_SDCARD_VIDEO = 1001;
    VideoHandler videoHandler ;
    /**
     * 包含有视频文件夹集合
     **/
    private List<VideoBean> videoBeans = new ArrayList<VideoBean>();


    /**
     * Fill in layout id
     *
     * @return layout id
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_list;
    }

    /**
     * Initialize the view in the layout
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void initViews(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        videoListView = (RecyclerView) findViewById(R.id.videoListView);
        videoHandler = new VideoHandler();
        videoHandler.sendEmptyMessage(SCAN_SDCARD_VIDEO);
        mAdapter = new VideoListAdapter(this,videoBeans);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        videoListView.setLayoutManager(mLayoutManager);
        videoListView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {

    }


    /**
     * Initialize the toolbar in the layout
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    /**
     * Initialize the View of the listener
     */
    @Override
    protected void initListeners() {
    }

    class VideoHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SCAN_SDCARD_VIDEO:
                    scanSdcardAllVideoFile();
                    break;
            }
        }
    }
    private void scanSdcardAllVideoFile(){
        Log.e("VIDEO","scanSdcardAllVideoFile start");
        Cursor cursor = getContentResolver().query(uri,mediaColumns,null,null,null);
        if(cursor == null){
            Toast.makeText(this,"没有扫描到视频文件",Toast.LENGTH_SHORT).show();
            return;
        }
        if(cursor.moveToFirst()){
            do{
                String videopath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String videoName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                VideoBean videoBean = new VideoBean(videoName,videopath,size,duration);
                Log.e("VIDEO","scanSdcardAllVideoFile videopath=" + videopath + ",videoName =" + videoName + ",size=" + size + ",duration =" + duration);
                videoBeans.add(videoBean);
            }while (cursor.moveToNext());
            mAdapter.notifyDataSetChanged();
        }
    }


}

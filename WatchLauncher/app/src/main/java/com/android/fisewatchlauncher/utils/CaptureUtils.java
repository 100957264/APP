package com.android.fisewatchlauncher.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.client.msg.MsgType;
import com.android.fisewatchlauncher.constant.ReceiverConstant;
import com.android.fisewatchlauncher.entity.msg.TcpMsg;
import com.android.fisewatchlauncher.function.media.MediaTransformUtils;
import com.android.fisewatchlauncher.parser.MsgParser;
import com.android.fisewatchlauncher.service.CameraService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by qingfeng on 2017/11/3.
 */

public class CaptureUtils implements CameraService.CaptureListener {
    private boolean isBound = false;

    private static class SingletonHolder {
        private static final CaptureUtils INSTANCE = new CaptureUtils();
    }

    public static CaptureUtils instance() {
        return SingletonHolder.INSTANCE;
    }

    public void capture() {
        Intent intent = new Intent(KApplication.sContext, CameraService.class);
        KApplication.sContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CameraService.MyBinder binder = (CameraService.MyBinder) service;
            isBound = true;
            LogUtils.d("onServiceConnected: start capture...");
            binder.capture(CaptureUtils.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    public void onCaptureFinished(String path) {
        boolean isNull = TextUtils.isEmpty(path);
        if (!isNull) {
            uploadCapture(path);
        }
        if (isBound) {
            KApplication.sContext.unbindService(connection);//会自动销毁Service
        }
    }

    private void uploadCapture(final String path) {
        LogUtils.d("onCaptureFinished : upload picture ing ...path =" + path);
        byte[] srcBytes = FileIOUtils.readFile2BytesByMap(path);
        byte[] trans2UploadData = MediaTransformUtils.getDataUpload(srcBytes);
//        testImg(path);广播测试上传下载
//        [SG*8800000015*len*img,x,y,z]
        String msgPart = "5" + GlobalSettings.MSG_CONTENT_SEPERATOR +
                getPhotoTimeStr() + GlobalSettings.MSG_CONTENT_SEPERATOR;//"x,y,"
        MsgParser.instance().sendMsgStringBytesContent(MsgType.img, msgPart, trans2UploadData, new TcpMsg.SendCallBack() {
            @Override
            public void onSuccessSend(TcpMsg msg) {
                LogUtils.e("onCaptureFinished : upload picture successfully...path =" + path);
            }

            @Override
            public void onErrorSend(TcpMsg msg) {
                LogUtils.e("onCaptureFinished : upload picture failed...path =" + path);
            }
        });

    }

    private void testImg(String audioPath) {
        Intent testIntent = new Intent(ReceiverConstant.ACTION_CMD_TEST_IMG);
        testIntent.putExtra(ReceiverConstant.EXTRA_CMD_TEST_IMG, audioPath);
        KApplication.sContext.sendBroadcast(testIntent);
    }

    public String getPhotoTimeStr() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        return dateFormat.format(date);
    }
}

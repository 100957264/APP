package com.android.fisewatchlauncher.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.SurfaceView;

import com.android.fisewatchlauncher.acty.CameraPreviewWindow;
import com.android.fisewatchlauncher.constant.FileConstant;
import com.android.fisewatchlauncher.utils.LogUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by qingfeng on 2017/11/3.
 */

public class CameraService extends Service implements Camera.PictureCallback {


    private Camera mCamera;
    private boolean isRunning;
    PowerManager.WakeLock mLock;
    final String ROOT_PATH = /*"/sdcard/DCIM/"*/FileConstant.PHOTO_CAPTURE_OUTPUT_PATH;
    public static boolean isCaptureCompeleted = false;
    private CaptureListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        acquireWakeLock();//绑定的时候唤醒
        return (IBinder) binder;
    }

    public interface CaptureListener {
        public void onCaptureFinished(String path);
    }

    MyBinder binder = new MyBinder();

    public class MyBinder extends Binder {

        public void capture(CaptureListener listener) {
            CameraService.this.listener = listener;
            startTakePicture();
        }

    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.d("onUnbind: intent " + intent.toString());
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d("onCreate: show Camera Preview Window");
    }

    private void acquireWakeLock() {
        if (mLock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "camera_lock");
            mLock.acquire();
        }
    }

    private void releaseWakeLock() {
        if (mLock != null && mLock.isHeld()) {
            mLock.release();
            mLock = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        acquireWakeLock();//绑定或者启动服务时候唤醒
        LogUtils.d("onStartCommand---->startTakePicture()");
        return START_NOT_STICKY;
    }

    private void startTakePicture() {
        if (!isRunning) {
            SurfaceView preview = CameraPreviewWindow.getDummyCameraView();
            LogUtils.d("startTakePicture ,preview =" + preview);
            if (preview != null) {
                autoTakePicture(preview);
            } else {
                update(null);
            }
        }
    }

    private void update(String path) {
        if (null != listener) {
            listener.onCaptureFinished(path);
        }
    }

    private void autoTakePicture(SurfaceView preview) {
        isRunning = true;
        mCamera = Camera.open();
        if (mCamera == null) {
            update(null);
            return;
        }
        try {
            mCamera.setPreviewDisplay(preview.getHolder());
            mCamera.startPreview();// 开始预览
            Thread.sleep(500);
            LogUtils.d("autoTakePic---->takePicture()");
            takePicture();
        } catch (Exception e) {
            e.printStackTrace();
            releaseCamera();
            update(null);
        }
    }

    private void takePicture() throws Exception {
        try {
            mCamera.takePicture(null, null, this);
        } catch (Exception e) {
            e.printStackTrace();
            update(null);
            throw e;
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        releaseCamera();

        // 大于500K，压缩预防内存溢出
        BitmapFactory.Options opts = null;
        if (data.length > 50 * 1024) {
            opts = new BitmapFactory.Options();
            opts.inSampleSize = 2;
            LogUtils.d("options data =" + data.length);
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
                opts);
        // 旋转270度
        LogUtils.d("onPictureTaken---->savePicture()");
        // 保存
        savePictureToSdcard(bitmap, data);
        isCaptureCompeleted = true;

    }

    public void savePictureToSdcard(Bitmap bitmap, byte[] data) {

        try {
            String imageName = getPhotoFileName();
            File fileFolder = new File(ROOT_PATH);
            if (!fileFolder.exists()) {
                fileFolder.mkdir();
            }
            Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap,240,240,true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] newData = baos.toByteArray();
            File captureFile = new File(fileFolder, imageName);
            LogUtils.d("savePictureToSdcard: imageName =" + imageName + ",data =" + newData.length);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(captureFile));
            bos.write(newData);
            bos.flush();
            baos.close();
            bos.close();
            update(captureFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            update(null);
        }
    }

    public String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return "IMG_" + dateFormat.format(date) + ".jpg";
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        releaseWakeLock();
        releaseCamera();
    }
}

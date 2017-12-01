package com.android.fisewatchlauncher.fragment;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fisewatchlauncher.FlavorDiff;
import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.function.photo.QrCodeUtils;
import com.android.fisewatchlauncher.utils.DeviceInfoUtils;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.zxing.ZXingUtils;

import java.io.File;

import static android.content.Context.TELEPHONY_SERVICE;
import static com.android.fisewatchlauncher.R.string.app_qr_code_for_bind;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/1
 * @time 17:58
 */
public class QrCodeFragment extends BaseFragment {
    private static final int DEFAULT_QRCODE_IMG = R.drawable.qrcode_example;
    private static final String PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    ImageView qrView;
    TextView qrTextView;
    boolean isGenerQRSuccess = false;
    private boolean isViewInited = false;
    public File mQRLocateFile;
    public String mQRLocateFilePath;
    String imei = "";
    String imeiTxt = "";
    @Override
    protected int setLayoutResouceId() {
        return R.layout.fise_qrcode_activity;
    }

    public static QrCodeFragment newInstance() {
        Bundle args = new Bundle();
        QrCodeFragment fragment = new QrCodeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        qrView = (ImageView) mRootView.findViewById(R.id.btn_app_qrcode);
        qrTextView = (TextView) mRootView.findViewById(R.id.tv_app_qrcode);
/*        LinearLayout.LayoutParams parentParams = (LinearLayout.LayoutParams) qrView.getLayoutParams();
        String FLAVOR = BuildConfig.FLAVOR;
        if (FLAVOR.equals("BLUE_FOX")) {
            parentParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            parentParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            qrTextView.setVisibility(View.GONE);
            qrView.setImageResource(R.drawable.blue_fox_function_qr);
        }else{
            parentParams.width = DpUtil.dip2px(KApplication.sContext,180);
            parentParams.height = DpUtil.dip2px(KApplication.sContext,180);
            qrTextView.setVisibility(View.VISIBLE);
            setView(mQRLocateFilePath);
        }
        qrView.setLayoutParams(parentParams);*/
        setView(mQRLocateFilePath);

    }

    @Override
    protected void initData(Bundle arguments) {
        super.initData(arguments);
//        if ("BLUE_FOX".equals(BuildConfig.FLAVOR)) return;
         imei = GlobalSettings.instance().getImei();
        String newImei = DeviceInfoUtils.getIMEI(getActivity());
        if( newImei  != null && imei != newImei){
            imei = newImei;
            GlobalSettings.instance().setImei(imei);
        }
        if(isShowDownUrl(imei)) {
            imeiTxt = FlavorDiff.getDownloadUrl();
        }else {
            imeiTxt = FlavorDiff.getBondUrl(imei);
        }
        LogUtils.e("imeiTxt " + imeiTxt);
        mQRLocateFile = QrCodeUtils.getTempFile(imei);
        mQRLocateFilePath = mQRLocateFile.getAbsolutePath();
        LogUtils.e("filePath " + mQRLocateFilePath);
        generQr(imeiTxt, mQRLocateFilePath);

    }

    private void generQr(final String newImei, final String saveQRPath) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtils.e("exeTask tempLogoPath :" + saveQRPath);
                isGenerQRSuccess = ZXingUtils.createQRImage(newImei, 200, 200, null, saveQRPath);
                LogUtils.e("exeTask isSuccess :" + isGenerQRSuccess);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setView(saveQRPath);
                    }
                });
            }
        }).start();
    }

    void setView(String QRCodePath) {
        Bitmap QRCadeBt = BitmapFactory.decodeFile(QRCodePath);
        if (QRCadeBt == null) {
            qrTextView.setText("出现错误");
            return;
        }
        qrView.setImageBitmap(QRCadeBt);
        LogUtils.d("setView: isNetworkAvailible =" + isNetworkAvailible() + ",isNetworkConnected =");
        if(isShowDownUrl(imei)){
            showScanToDown();
        }else {
            showScanToBind();
        }
    }

    private boolean isSimReady() {
        TelephonyManager tm = (TelephonyManager) getContext().getSystemService(TELEPHONY_SERVICE);
        return tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    private void showScanToBind() {
        qrTextView.setText(getResources().getString(app_qr_code_for_bind));
    }
    private void showScanToDown() {
        qrTextView.setText(R.string.app_qr_code_for_download);
    }
    private void showNoSim() {
        qrTextView.setText(getResources().getString(R.string.app_qr_code_for_download));
        qrView.setImageResource(R.drawable.icon_qrcode_offline);
    }
    private boolean isNetworkAvailible(){
       boolean isNetworkAvailible = false;
        ConnectivityManager connectivityManager =(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null){
            isNetworkAvailible = networkInfo.isAvailable();
        }
       return isNetworkAvailible;
    }
    private boolean isShowDownUrl(String imeiStr){
        return imeiStr == null || TextUtils.isEmpty(imeiStr) || !isNetworkAvailible();
    }
}

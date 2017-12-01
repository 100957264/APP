package com.android.fisewatchlauncher.acty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.entity.dao.EaseFriend;
import com.android.fisewatchlauncher.event.EaseAuthEvent;
import com.android.fisewatchlauncher.function.wetchat.EaseMobManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class EaseFunctionActivity extends AppCompatActivity {

    public EaseFriend mEaseFriend;
    public ProgressDialog mWaitingDialog;
    private boolean mIsVoiceCall ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ease_function);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        } else {
            EventBus.getDefault().register(this);
            mEaseFriend = intent.getParcelableExtra(EaseContactListActivity.EXTRA_KEY_EASE_FRIEND);
            mWaitingDialog = new ProgressDialog(this);
        }
    }

    public void onVoiceCallClick(View v) {
        showWaitingDialog();
        mIsVoiceCall = true;
        EaseMobManager.instance().requestConversation(EaseMobManager.CONVERSATION_VOICE);
    }

    public void onVideoCallClick(View v) {
        showWaitingDialog();
        mIsVoiceCall = false;
        EaseMobManager.instance().requestConversation(EaseMobManager.CONVERSATION_VIDEO);
    }

    private void showWaitingDialog() {
        mWaitingDialog.setMessage("连接中...");
        mWaitingDialog.setIndeterminate(true);
        mWaitingDialog.setCancelable(false);
        mWaitingDialog.show();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mWaitingDialog.dismiss();
        super.onDestroy();
    }

    @Subscribe
    public void onVideoCallClick(EaseAuthEvent event) {
        mWaitingDialog.dismiss();
        if (!event.isGranted) {
            Toast.makeText(this, "余额不足", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mIsVoiceCall) {
            EaseMobManager.instance().makeVoiceCall(mEaseFriend);
        }else {
            EaseMobManager.instance().makeVideoCall(mEaseFriend);
        }
    }

}

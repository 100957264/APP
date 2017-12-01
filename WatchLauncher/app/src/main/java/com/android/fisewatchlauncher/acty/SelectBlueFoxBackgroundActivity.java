package com.android.fisewatchlauncher.acty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.fisewatchlauncher.FlavorDiff;
import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.utils.SPUtils;

public class SelectBlueFoxBackgroundActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int UPDATE_BACKGROUND = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bluefox_background);
        findViewById(R.id.blue_fox_background0).setOnClickListener(this);
        findViewById(R.id.blue_fox_background1).setOnClickListener(this);
        findViewById(R.id.blue_fox_background2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.blue_fox_background0:
                SPUtils.getInstance().put(FlavorDiff.SP_KEY_BLUE_FOX_BACKGROUND, 0);
                finish();
                break;
            case R.id.blue_fox_background1:
                SPUtils.getInstance().put(FlavorDiff.SP_KEY_BLUE_FOX_BACKGROUND, 1);
                setResult(UPDATE_BACKGROUND);
                finish();
                break;
            case R.id.blue_fox_background2:
                SPUtils.getInstance().put(FlavorDiff.SP_KEY_BLUE_FOX_BACKGROUND, 2);
                setResult(UPDATE_BACKGROUND);
                finish();
                break;
        }
    }
}
package com.android.fisewatchlauncher.listener;

import android.view.View;

/**
 * Created by mare on 2017/8/27 0027.
 */

public interface BaseViewListener extends View.OnClickListener {

    public void onStart();

    public void onStop();

    public void onResume();

    public void onPause();

}

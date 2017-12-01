package com.android.fisewatchlauncher.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.widget.FiseRectAnalogClockView;


/**
 * Created by qingfeng on 2017/11/16.
 */

public class KeyguardFragment extends Fragment {
     FiseRectAnalogClockView fiseRectAnalogClockView;
     public static KeyguardFragment newInstance(){
         KeyguardFragment fragment = new KeyguardFragment();
         return fragment;
     }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.keyguard_fragment,container,false);
        fiseRectAnalogClockView = view.findViewById(R.id.rect_analog_clock);
        fiseRectAnalogClockView.setFocusable(true);
        fiseRectAnalogClockView.setFocusableInTouchMode(true);
        fiseRectAnalogClockView.requestFocus();
        return view;
    }
}

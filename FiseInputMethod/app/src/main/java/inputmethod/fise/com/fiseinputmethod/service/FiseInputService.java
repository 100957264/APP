package inputmethod.fise.com.fiseinputmethod.service;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.view.LayoutInflater;
import android.view.View;

import inputmethod.fise.com.fiseinputmethod.R;
import inputmethod.fise.com.fiseinputmethod.utils.KeyboardUtils;

/**
 * Created by qingfeng on 2017/11/28.
 */

public class FiseInputService extends InputMethodService{
    KeyboardView keyboardView;
    @Override
    public View onCreateInputView() {
        View view = LayoutInflater.from(this).inflate(R.layout.keyboard_layout,null);
        keyboardView =(KeyboardView) view.findViewById(R.id.keyboard);
        KeyboardUtils.instance().initKeyboardView(this,keyboardView);
        return view;
    }

    @Override
    public View onCreateCandidatesView() {
        return super.onCreateCandidatesView();
    }
    public void commitText(String data){
        getCurrentInputConnection().commitText(data,0);
        setCandidatesViewShown(false);
    }
    public void deleteText(){
        getCurrentInputConnection().deleteSurroundingText(1,0);
    }
    public void hideInputMethod(){
        hideWindow();
    }
    public void showInputMethod(){
        //showInputMethod();
        //showWindow(true);
    }
}

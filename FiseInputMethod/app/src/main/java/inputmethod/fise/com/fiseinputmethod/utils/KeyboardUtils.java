package inputmethod.fise.com.fiseinputmethod.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;

import inputmethod.fise.com.fiseinputmethod.R;
import inputmethod.fise.com.fiseinputmethod.service.FiseInputService;

/**
 * Created by qingfeng on 2017/11/28.
 */

public class KeyboardUtils {
    private KeyboardView keyboardView;
    private FiseInputService fiseInputService;
    private Keyboard keyboard;

    private KeyboardUtils() {
    }
    private static class SingletonHolder {
        private static final KeyboardUtils INSTANCE = new KeyboardUtils();
    }

    public static KeyboardUtils instance() {
        return SingletonHolder.INSTANCE;
    }
    public void initKeyboardView(FiseInputService fiseInputService, KeyboardView keyboardView){
            this.keyboardView = keyboardView;
            this.keyboardView.setOnKeyboardActionListener(keyListener);
            this.fiseInputService = fiseInputService;
            keyboard = new Keyboard(this.fiseInputService.getApplicationContext(), R.xml.keyboard);
            this.keyboardView.setKeyboard(keyboard);
            this.keyboardView.setEnabled(true);
            keyboardView.setPreviewEnabled(true);
    }
    private KeyboardView.OnKeyboardActionListener keyListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {

        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };
}

package inputmethod.fise.com.fiseinputmethod.aty;

import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import inputmethod.fise.com.fiseinputmethod.R;

public class FiseInputMethodActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.keyboard_layout);
    }
}

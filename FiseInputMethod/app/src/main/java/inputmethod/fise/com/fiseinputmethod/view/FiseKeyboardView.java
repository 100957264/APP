package inputmethod.fise.com.fiseinputmethod;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;
import android.widget.PopupWindow;

/**
 * Created by qingfeng on 2017/11/27.
 */

public class FiseKeyboardView extends Keyboard{

    public FiseKeyboardView(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    public FiseKeyboardView(Context context, int xmlLayoutResId, int modeId, int width, int height) {
        super(context, xmlLayoutResId, modeId, width, height);
    }

    public FiseKeyboardView(Context context, int xmlLayoutResId, int modeId) {
        super(context, xmlLayoutResId, modeId);
    }

    public FiseKeyboardView(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }

    @Override
    protected Key createKeyFromXml(Resources res, Row parent, int x, int y, XmlResourceParser parser) {
        Key key = new Key(res,parent,x,y,parser);
        return key;
    }
}

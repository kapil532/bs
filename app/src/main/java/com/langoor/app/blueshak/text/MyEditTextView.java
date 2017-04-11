package com.langoor.app.blueshak.text;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class MyEditTextView extends EditText {
    public MyEditTextView(Context context) {
        super(context);
        init();
    }

    public MyEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "Raleway-Medium.ttf");
        setTypeface(tf);
    }


}

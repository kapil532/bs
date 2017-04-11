package com.langoor.app.blueshak.text;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

public class MyButtonNormal extends Button {
    public MyButtonNormal(Context context) {
        super(context);
        init();
    }

    public MyButtonNormal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyButtonNormal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "Raleway-Medium.ttf");
        setTypeface(tf);
    }
}

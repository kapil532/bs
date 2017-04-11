package com.langoor.app.blueshak.text;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextViewMediumBold extends TextView {
    public MyTextViewMediumBold(Context context) {
        super(context);
        init();
    }

    public MyTextViewMediumBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewMediumBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "Raleway-Bold.ttf");
        setTypeface(tf);
    }
}

package com.blueshak.app.blueshak.text;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by lsingh013 on 08/06/18.
 */

public class ExpandedSpannable extends ClickableSpan {

    private boolean isUnderline = true;

    /**
     * Constructor
     */
    public ExpandedSpannable(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(isUnderline);
        ds.setColor(Color.parseColor("#0246B5"));
    }

    @Override
    public void onClick(View widget) {


    }

}

package com.langoor.app.blueshak.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.daimajia.slider.library.R.layout;
import com.daimajia.slider.library.R.id;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

/**
 * Created by Manu on 13-10-2015.
 */
public class CustomSliderTextView extends BaseSliderView {
    private static Typeface font = null;
    private Context context ;
    public CustomSliderTextView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(layout.render_type_text,null);
        ImageView target = (ImageView)v.findViewById(id.daimajia_slider_image);
        LinearLayout linearLayout = (LinearLayout) v.findViewById(id.description_layout);
        linearLayout.setBackgroundColor(Color.TRANSPARENT);
        this.bindEventAndShow(v, target);
        return v;
    }
}

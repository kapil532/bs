/*
 *
 * Copyright (c) 2015.
 * For the development of personal software in the android platform,
 * Subjected too the idea of SUM Enterprices
 * This information and the oding style and the functionalities are not reusealblee withhout permissios.
 * The Information and the coding from these module if found to be used any where it is an offence.
 *
 * Created by Manu
 * Created on 26/6/15 2:01 AM
 */

package com.langoor.app.blueshak.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.langoor.blueshak.R;


public class AlertDialog extends Dialog {


    TextView title = null, description = null;
    ImageView titleImage = null;
    Button okButton = null, cancelButton = null, neutralButton = null;

    String titleString = null, message= null, okButtonString = null, cancelButtonString = null, neutralButtonString = null;
    View.OnClickListener okButtonListener = null, cancelButtonListener = null, neutralButtonListener = null;
    int ImageRes = 0;

    static String TAG = "AlertDialog";

    public AlertDialog(Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.alert_custom_dialog);

        title = (TextView) findViewById(R.id.alert_custom_dialog_title_textview);
        description = (TextView) findViewById(R.id.alert_custom_dialog_description_textview);
        titleImage = (ImageView) findViewById(R.id.alert_custom_dialog_title_image);
        okButton = (Button) findViewById(R.id.alert_custom_dialog_okButton);
        cancelButton = (Button) findViewById(R.id.alert_custom_dialog_cancelButton);
        neutralButton = (Button) findViewById(R.id.alert_custom_dialog_neutralButton);

        if(titleString == null){title.setVisibility(View.GONE);}
        else{title.setVisibility(View.VISIBLE);title.setText(titleString);}

        if(ImageRes == 0){titleImage.setVisibility(View.GONE);}
        else{titleImage.setVisibility(View.VISIBLE);titleImage.setImageResource(ImageRes);}

        if(message == null){description.setVisibility(View.GONE);}
        else{description.setVisibility(View.VISIBLE);description.setText(message);}

        okButton.setText(okButtonString);
        okButton.setTextColor(getContext().getResources().getColor(R.color.brandColor));
        okButton.setOnClickListener(okButtonListener);

        if(cancelButtonString!=null && cancelButtonListener!=null){
            cancelButton.setVisibility(View.VISIBLE);
            cancelButton.setText(cancelButtonString);
            cancelButton.setTextColor(getContext().getResources().getColor(R.color.brandColor));
            cancelButton.setOnClickListener(cancelButtonListener);
        }else{
            cancelButton.setVisibility(View.INVISIBLE);
        }

        if(neutralButtonString!=null && neutralButtonListener!=null){
            neutralButton.setVisibility(View.VISIBLE);
            neutralButton.setText(neutralButtonString);
            neutralButton.setTextColor(getContext().getResources().getColor(R.color.brandColor));
            neutralButton.setOnClickListener(neutralButtonListener);
        }else{
            neutralButton.setVisibility(View.INVISIBLE);
        }

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);

    }

    public void setTitle(String titleString){
        this.titleString = titleString;
    }

    public void setIcon(int resID){
        this.ImageRes = resID;
    }

    public void setMessage(String descriptionString){
        this.message = descriptionString;
    }

    public void setPositiveButton(String name, View.OnClickListener listener){
        okButtonString = name;
        okButtonListener = listener;
    }

    public void setNegativeButton(String name, View.OnClickListener listener){
        cancelButtonString = name;
        cancelButtonListener = listener;
    }

    public void setNeutralButton(String name, View.OnClickListener listener){
        neutralButtonString = name;
        neutralButtonListener = listener;
    }

    public void setIsCancelable(boolean cancelable){
        this.setCancelable(cancelable);
    }

}


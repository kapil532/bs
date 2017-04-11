package com.langoor.app.blueshak.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.langoor.blueshak.R;
import com.langoor.app.blueshak.services.model.PostcodeModel;
import com.tokenautocomplete.TokenCompleteTextView;

public class MultiAutoCompletionView extends TokenCompleteTextView<PostcodeModel> {
    public MultiAutoCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(PostcodeModel person) {

        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout)l.inflate(R.layout.autocomplete_row_item, (ViewGroup)MultiAutoCompletionView.this.getParent(), false);
        ((TextView)view.findViewById(R.id.autoCompleteLabel)).setText(person.getPostcode()+"-"+person.getSuburb());

        return view;
    }

    @Override
    protected PostcodeModel defaultObject(String s) {
        return null;
    }

}

package com.langoor.app.blueshak.currency;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.crashlytics.android.Crashlytics;
import com.langoor.app.blueshak.category.OnSelected;
import com.langoor.app.blueshak.services.model.CategoryModel;
import com.langoor.app.blueshak.services.model.CurrencyModel;
import com.langoor.app.blueshak.services.model.SalesModel;
import com.langoor.blueshak.R;

import java.util.List;

public class CurrencyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<CurrencyModel> albumList;
    private OnSelected onSelected;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView item_name;
        public CheckBox chkSelected;
        public ImageView ic_check;
        View item_view;
        public MyViewHolder(View view) {
            super(view);
            item_name = (TextView) view.findViewById(R.id.name_tv);
            item_view=view;
            chkSelected = (CheckBox) view.findViewById(R.id.chkSelected);
            ic_check = (ImageView) view.findViewById(R.id.ic_check);
        }
    }

    public CurrencyListAdapter(Context mContext, List<CurrencyModel> albumList, OnSelected onSelected) {
        this.onSelected=onSelected;
        this.context = mContext;
        this.albumList = albumList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item_row, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder,final int position) {
        try {
            final int pos = position;
            if (view_holder instanceof MyViewHolder) {
                final MyViewHolder holder = (MyViewHolder) view_holder;
                CurrencyModel obj = albumList.get(position);
                holder.item_name.setText(obj.getCurrency());
                holder.item_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.ic_check.setVisibility(View.VISIBLE);
                        onSelected.onSelected(position);
                        holder.item_name.setTextColor(context.getResources().getColor(R.color.brandColor));
                    }
                });
                if(obj.is_selected()){
                    holder.ic_check.setVisibility(View.VISIBLE);
                    holder.item_name.setTextColor(context.getResources().getColor(R.color.brandColor));
                }else{
                    holder.item_name.setTextColor(context.getResources().getColor(R.color.brand_text_color));
                    holder.ic_check.setVisibility(View.GONE);
                }
                holder.chkSelected.setTag(obj);
                holder.chkSelected.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        onSelected.onSelected(position);
                        CheckBox cb = (CheckBox) v;
                        CategoryModel obj = (CategoryModel) cb.getTag();
                        obj.setIs_selected(cb.isChecked());
                        albumList.get(pos).setIs_selected(cb.isChecked());
                        if(cb.isChecked()){
                            holder.item_name.setTextColor(context.getResources().getColor(R.color.brandColor));
                        }else{
                            holder.item_name.setTextColor(context.getResources().getColor(R.color.brand_text_color));
                        }
                    }
                });
            }
        }  catch (NullPointerException e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }catch (NumberFormatException e) {
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }

    }
    @Override
    public int getItemCount() {
        return albumList.size();
    }
    // method to access in activity after updating selection
    public List<CurrencyModel> getProductList() {
        return albumList;
    }


}

package com.langoor.app.blueshak.garage;

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
import com.langoor.app.blueshak.services.model.SalesModel;
import com.langoor.blueshak.R;
import java.util.List;

public class SalesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<SalesModel> albumList;
    private OnSelected onSelected;
    private boolean is_multiple_selection=false;
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

    public SalesListAdapter(Context mContext, List<SalesModel> albumList, OnSelected onSelected) {
        this.is_multiple_selection=is_multiple_selection;
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
                SalesModel obj = albumList.get(position);
                holder.item_name.setText(obj.getName());
                holder.item_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!is_multiple_selection){
                            holder.ic_check.setVisibility(View.VISIBLE);
                            onSelected.onSelected(position);
                            holder.item_name.setTextColor(context.getResources().getColor(R.color.brandColor));
                        }
                    }
                });

                if(is_multiple_selection)
                    holder.chkSelected.setVisibility(View.VISIBLE);

                holder.chkSelected.setChecked(obj.is_selected());
                if(obj.is_selected()){
                    holder.chkSelected.setChecked(obj.is_selected());
                    if(!is_multiple_selection)
                        holder.ic_check.setVisibility(View.VISIBLE);
                    holder.item_name.setTextColor(context.getResources().getColor(R.color.brandColor));
                }else{
                    holder.item_name.setTextColor(context.getResources().getColor(R.color.brand_text_color));
                    if(!is_multiple_selection)
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
    public List<SalesModel> getProductList() {
        return albumList;
    }


}

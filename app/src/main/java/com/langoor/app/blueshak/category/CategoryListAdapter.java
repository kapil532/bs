package com.langoor.app.blueshak.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.langoor.blueshak.R;
import com.langoor.app.blueshak.bookmarks.OnBookMarksDeleted;
import com.langoor.app.blueshak.services.model.CategoryModel;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<CategoryModel> albumList;
    private OnSelected onSelected;
    private boolean is_multiple_selection=false;
    private static int lastCheckedPos = 0;
    private static CheckBox lastChecked = null;
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

    public CategoryListAdapter(Context mContext, List<CategoryModel> albumList, boolean is_multiple_selection,OnSelected onSelected) {
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
                //for default check in first item
                if(is_multiple_selection){
                    if(position == 0 && albumList.get(0).is_selected() && ((MyViewHolder) view_holder).chkSelected.isChecked())
                    {
                        lastChecked = ((MyViewHolder) view_holder).chkSelected;
                        lastCheckedPos = 0;
                    }else if( albumList.get(position).is_selected() && ((MyViewHolder) view_holder).chkSelected.isChecked()){
                        lastChecked = ((MyViewHolder) view_holder).chkSelected;
                        lastCheckedPos = position;
                    }
                }


                final MyViewHolder holder = (MyViewHolder) view_holder;
                CategoryModel obj = albumList.get(position);
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
                    if(!is_multiple_selection){
                        holder.ic_check.setVisibility(View.VISIBLE);
                        holder.item_name.setTextColor(context.getResources().getColor(R.color.brandColor));
                    }
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
                        int clickedPos = albumList.indexOf(obj);
                        obj.setIs_selected(cb.isChecked());
                        albumList.get(pos).setIs_selected(cb.isChecked());
                        if(cb.isChecked()){
                            if(lastChecked != null)
                            {
                                lastChecked.setChecked(false);
                                albumList.get(lastCheckedPos).setIs_selected(false);
                            }


                            lastChecked = cb;
                            lastCheckedPos = clickedPos;
                            /*holder.item_name.setTextColor(context.getResources().getColor(R.color.brandColor));*/
                        }else{
                            lastChecked = null;
                          /*  holder.item_name.setTextColor(context.getResources().getColor(R.color.brand_text_color));*/
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
    public List<CategoryModel> getProductList() {
        return albumList;
    }


}

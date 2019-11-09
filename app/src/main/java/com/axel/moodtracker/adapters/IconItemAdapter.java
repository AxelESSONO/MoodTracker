package com.axel.moodtracker.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.axel.moodtracker.R;
import com.axel.moodtracker.model.IconeItem;

import java.util.List;

public class IconItemAdapter extends BaseAdapter {

    //field
    Context context;
    List<IconeItem> iconeItemList;
    LayoutInflater inflater;

    //conctructor
    public IconItemAdapter(Context context, List<IconeItem> iconeItemList){
        this.context = context;
        this.iconeItemList = iconeItemList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return iconeItemList.size();
    }

    @Override
    public IconeItem getItem(int position) {
        return iconeItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.adapter_image, null);

        // get information aboput smiley
        IconeItem currentItem = getItem(position);
        String mnemonic = currentItem.getMnemonic();

        //get item icon view
        String resourceName = "smiley_" + mnemonic;
        ImageView itemIconView = convertView.findViewById(R.id.item_happy_smiley);
        int resId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
        itemIconView.setImageResource(resId);

        return convertView;
    }
}
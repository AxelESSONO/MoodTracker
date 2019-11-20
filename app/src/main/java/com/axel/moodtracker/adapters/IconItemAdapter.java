package com.axel.moodtracker.adapters;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.axel.moodtracker.R;
import com.axel.moodtracker.model.IconeItem;
import java.util.List;

public class IconItemAdapter extends BaseAdapter {

    //field
    Context context;
    List<IconeItem> iconeItemList;
    LayoutInflater inflater;
    LinearLayout mLinearLayout;

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

        // get information about smiley
        IconeItem currentItem = getItem(position);
        String mnemonic = currentItem.getMnemonic();
        final IconeItem pcolor = new IconeItem(currentItem.getName(),currentItem.getMnemonic(), currentItem.getColorSmiley());

        //Change the width of imageView
        WindowManager wm = (WindowManager) this.context.getSystemService(context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;

        //get item icon view
        String resourceName = "smiley_" + mnemonic;
        String resourceColor = pcolor.getColorSmiley();
        final ImageView itemIconView = convertView.findViewById(R.id.item_happy_smiley);
        itemIconView.getLayoutParams().height = screenHeight;

        int resId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
        itemIconView.setImageResource(resId);
        itemIconView.setBackgroundColor(Color.parseColor(resourceColor));

        final String name = pcolor.getName();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Vous venez d'appuyer sur le Smiley: "+ name,Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
package com.axel.moodtracker.adapter;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axel.moodtracker.R;
import com.axel.moodtracker.model.Mood;

import java.util.List;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MoodViewHolder> {

    private List<Mood> moodList;
    private Context context;

    public MoodAdapter(List<Mood> moodList, Context context) {
        this.moodList = moodList;
        this.context = context;
    }

    @NonNull
    @Override
    public MoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.mood_row, parent, false);
        return new MoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoodViewHolder holder, final int position) {

        holder.durationTxt.setText(moodList.get(position).getDate());
        holder.relativeLayout.setBackgroundColor(moodList.get(position).getColor());
        //setRelativeLayoutParam(holder.relativeLayout, position);
        holder.commentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, String.valueOf(moodList.get(position).getComment()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Set Layout Width and Height according to mood
    private void setRelativeLayoutParam(RelativeLayout relativeLayout, int position) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(resizeWidthAccordingToMood(moodList.get(position).getColor()), measureHeight() / 7);
        layoutParams.topMargin = 2;
        relativeLayout.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return moodList.size();
    }

    public class MoodViewHolder extends RecyclerView.ViewHolder {

        TextView durationTxt;
        ImageView commentImg;
        RelativeLayout relativeLayout;
        public MoodViewHolder(@NonNull View itemView) {
            super(itemView);
            durationTxt = (TextView) itemView.findViewById(R.id.duration);
            commentImg =(ImageView) itemView.findViewById(R.id.comment_row);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_mood);
        }
    }

    // Get Screen Width
    private int measureWidth() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        } else {
            screenWidth = display.getWidth();
        }
        return screenWidth;
    }

    // Get Screen Height
    private int measureHeight() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenHeight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        } else {
            screenHeight = display.getWidth();
        }
        return screenHeight;
    }

    private int resizeWidthAccordingToMood(int paramColor) {
        int colorIndex[] = {0, 1, 2, 3, 4};
        int widthSet[] = {5, 4, 3, 2, 1};
        int newWidth = 0;
        for (int i = 0; i < colorIndex.length; i++) {
            if (paramColor == colorIndex[i]) {
                newWidth = measureWidth() / widthSet[i];
            }
        }
        return newWidth;
    }
}

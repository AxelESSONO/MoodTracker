package com.axel.moodtracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axel.moodtracker.R;
import com.axel.moodtracker.controller.ContactsActivity;
import com.axel.moodtracker.model.Mood;
import com.axel.moodtracker.utils.Constants;
import com.axel.moodtracker.utils.FrenchNumberToWords;
import com.muddzdev.styleabletoast.StyleableToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public void onBindViewHolder(@NonNull final MoodViewHolder holder, final int position) {

        holder.relativeLayout.setBackgroundColor(moodList.get(position).getColor());
        final String comment = String.valueOf(moodList.get(position).getComment());
        setRelativeLayout(holder, position);
        holder.commentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new StyleableToast
                        .Builder(context)
                        .text(comment)
                        .textColor(Color.WHITE)
                        .backgroundColor(moodList.get(position).getColor())
                        .show();
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToContactsActivity(context, position);
            }
        });
        setDuration(holder, position, moodList);
    }

    private void setDuration(MoodViewHolder holder, int position, List<Mood> moodList) {

        String dateMood = null;
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        Date date1 = new java.util.Date();
        Date date2 = null;
        long diff = 0;
        try {
            dateMood = moodList.get(position).getDate();
            date2 = df.parse(dateMood);
            diff = (date1.getTime() - date2.getTime()) / 86400000;

        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (diff < 1) {
                holder.durationTxt.setText("Aujourd'hui");
            } else if (diff >= 1 && diff < 2) {
                holder.durationTxt.setText("Hier");
            } else if (diff >= 2 && diff < 3) {
                holder.durationTxt.setText("Avant hier");
            } else if (diff >= 3 && diff < 6) {
                holder.durationTxt.setText("Il y'a " + FrenchNumberToWords.convert(diff) + " jours");
            } else {
                holder.durationTxt.setText("Il y'a une semaine");
            }
        }
    }

    private void setRelativeLayout(MoodViewHolder holder, int position) {
        holder.relativeLayout.getLayoutParams().height = 285;
        holder.relativeLayout.getLayoutParams().width = setWidth(RelativeLayout.LayoutParams.MATCH_PARENT, position);
    }

    private int setWidth(int matchParent, int position) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = (size.x) / 5;

        //width = fitWidth(matchParent, position, width);

        width = width * (1 + moodList.get(position).getmMoodPosition());

        return width;
    }

    private void goToContactsActivity(Context context, int position) {

        //String comment = mood.getComment();
        //int color = mood.getColor();
        Intent contactIntent = new Intent(context, ContactsActivity.class);
        contactIntent.putExtra(Constants.MY_COMMENT, moodList.get(position).getComment());
        contactIntent.putExtra(Constants.COLOR_MOOD, moodList.get(position).getColor());
        contactIntent.putExtra(Constants.CURRENT_ITEM, moodList.get(position).getmMoodPosition());
        context.startActivity(contactIntent);
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
            commentImg = (ImageView) itemView.findViewById(R.id.comment_row);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_mood);
        }
    }
}

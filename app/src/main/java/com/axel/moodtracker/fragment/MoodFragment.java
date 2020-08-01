package com.axel.moodtracker.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.axel.moodtracker.R;
import com.bumptech.glide.Glide;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class MoodFragment extends Fragment {


    // 1 - Create keys for our Bundle
    private static final String KEY_POSITION = "position";
    private static final String KEY_COLOR = "color";
    private static final String PREF_DATA = "PREF_DATA";
    private static final String IMAGE_COLOR = "IMAGE COLOR";
    private static final String POSITION = "POSITION";
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    private int mImageRessource[] = {R.drawable.a_smiley_disappointed,
            R.drawable.b_smiley_sad,
            R.drawable.c_smiley_normal,
            R.drawable.d_smiley_happy,
            R.drawable.e_smiley_super_happy};


    public MoodFragment() {
        // Required empty public constructor
    }

    // 2 - Method that will create a new instance of PageFragment, and add data to its bundle.
    public static MoodFragment newInstance(int position, int color) {

        // 2.1 Create new fragment
        MoodFragment frag = new MoodFragment();

        // 2.2 Create bundle and add it some data
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        args.putInt(KEY_COLOR, color);
        frag.setArguments(args);

        return (frag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // 3 - Get layout of PageFragment
        View result = inflater.inflate(R.layout.fragment_mood, container, false);

        // 4 - Get widgets from layout and serialise it
        LinearLayout rootView = (LinearLayout) result.findViewById(R.id.fragment_page_rootview);
        //TextView textView= (TextView) result.findViewById(R.id.fragment_page_title);
        ImageView moodImage = (ImageView) result.findViewById(R.id.mood_image);

        // 5 - Get data from Bundle (created in method newInstance)
        int position = getArguments().getInt(KEY_POSITION, 3);
        int color = getArguments().getInt(KEY_COLOR, -11160041);

        // 6 - Update widgets with it
        rootView.setBackgroundColor(color);



        Glide.with(getContext()).load(mImageRessource[position]).into(moodImage);
        passFragmentData(position, color);

        return result;
    }

    private void passFragmentData(int position, int color) {
        Context context = getContext();
        preferences = context.getSharedPreferences(PREF_DATA, MODE_PRIVATE);
        editor = preferences.edit();
        editor.putInt(IMAGE_COLOR, color);
        editor.putInt(POSITION, position);
        editor.commit();
    }
}
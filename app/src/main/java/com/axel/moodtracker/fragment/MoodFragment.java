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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.axel.moodtracker.R;
import com.bumptech.glide.Glide;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class MoodFragment extends Fragment{

    // 1 - Create keys for our Bundle
    private static final String KEY_POSITION = "position";
    private static final String KEY_COLOR = "color";
    private static final String PREF_DATA = "PREF_DATA";
    private static final String IMAGE_RESSOURCE = "IMAGE RESSOURCE";
    private static final String IMAGE_COLOR = "IMAGE COLOR";
    private static final String POSITION = "POSITION";

    private int mColor;

    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    PassDataInterface mCallback;



    private int mImageRessource[] = {R.drawable.a_smiley_disappointed,
            R.drawable.b_smiley_sad,
            R.drawable.c_smiley_normal,
            R.drawable.d_smiley_happy,
            R.drawable.e_smiley_super_happy};

    public interface PassDataInterface {
        void onDataReceived(int color, int position);
    }

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

        return(frag);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // 3 - Get layout of PageFragment
        View result = inflater.inflate(R.layout.fragment_mood, container, false);

        // 4 - Get widgets from layout and serialise it
        LinearLayout rootView= (LinearLayout) result.findViewById(R.id.fragment_page_rootview);
        //TextView textView= (TextView) result.findViewById(R.id.fragment_page_title);
        ImageView moodImage = (ImageView) result.findViewById(R.id.mood_image);

        // 5 - Get data from Bundle (created in method newInstance)

        int position = getArguments().getInt(KEY_POSITION, 3);
        int color = getArguments().getInt(KEY_COLOR, -11160041);

        mCallback.onDataReceived(color, position);

        // 6 - Update widgets with it
        rootView.setBackgroundColor(color);

        Glide.with(getContext()).load(mImageRessource[position]).into(moodImage);


        return result;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void passFragmentData(int position, int color) {
        Context context = getContext();
        preferences = context.getSharedPreferences(PREF_DATA, MODE_PRIVATE);
        editor = preferences.edit();
        editor.putInt(IMAGE_COLOR, color);
        editor.putInt(POSITION, position);
        editor.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (PassDataInterface) context;
        } catch (ClassCastException e) { }
    }


    // 3 - Create callback to parent activity
/*    private void createCallbackToParentActivity(){
        try {
            //Parent activity will automatically subscribe to callback
            mCallback = (PassDataInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()+ " must implement OnButtonClickedListener");
        }
    }*/

/*    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int position = getArguments().getInt(KEY_POSITION, 3);
        int color = getArguments().getInt(KEY_COLOR, -11160041);

        int action = event.getAction();
        mCallback = (PassDataInterface) getActivity();
        if (action==MotionEvent.ACTION_UP)
        {
            //myView.setBackgroundColor(Color.RED);
            mCallback.onDataReceived(color, position);
            Log.d("Valeur", "ACTION_UP");
        }
        if (action==MotionEvent.ACTION_DOWN)
        {
            mCallback.onDataReceived(color, position);
            //myView.setBackgroundColor(Color.YELLOW);
            Log.d("Valeur", "ACTION_DOWN");
        }
        // TODO Auto-generated method stub
        return true;
    }*/

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        int position = getArguments().getInt(KEY_POSITION, 3);
        int color = getArguments().getInt(KEY_COLOR, -11160041);
        mCallback.onDataReceived(color, position);
    }
}
package com.axel.moodtracker.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.axel.moodtracker.R;
import com.axel.moodtracker.adapters.IconItemAdapter;
import com.axel.moodtracker.model.DatabaseHelper;
import com.axel.moodtracker.model.IconeItem;
import java.util.ArrayList;
import java.util.List;

public class MoodActivity extends AppCompatActivity {

    private MoodActivity moodActivity;
    public static final String TAG = "MoodActivity";
    DatabaseHelper mDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        this.moodActivity = this;
        List<IconeItem> iconeItemList = new ArrayList<>();
        iconeItemList.add(new IconeItem("smiley_sad", "sad", "#AB1A49"));
        iconeItemList.add(new IconeItem("smiley_disappointed", "disappointed", "#808A89"));
        iconeItemList.add(new IconeItem("smiley_normal", "normal", "#3135D0"));
        iconeItemList.add(new IconeItem("smiley_happy", "happy", "#55B617"));
        iconeItemList.add(new IconeItem("smiley_super_happy", "super_happy", "#D0E807"));

        //get listView
        ListView pictureListView = findViewById(R.id.list_picture);
        pictureListView.setAdapter(new IconItemAdapter(this, iconeItemList));

        // the popup to write the coment
        final ImageView myPopup = findViewById(R.id.add_comment_button);
        mDatabaseHelper = new DatabaseHelper(this);
        myPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MoodActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.my_popup,null);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                final EditText mComent = (EditText) mView.findViewById(R.id.subTitle);
                TextView mTitle = (TextView) mView.findViewById(R.id.title);
                Button mValidateBtn = (Button) mView.findViewById(R.id.validateButton);
                Button mCancelBtn = (Button) mView.findViewById(R.id.cancelButton);

                // Clicking on Button Validate
                mValidateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newComment = mComent.getText().toString();
                        // Check if field is not empty
                        if(!mComent.getText().toString().isEmpty()){
                            addData(newComment);
                            mComent.setText("");
                            //Toast.makeText(MoodActivity.this,"Votre commentaire a été enrégistré avec succès",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(MoodActivity.this, "Veuillez saisir un commentaire s'il vous plaît !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //To dismiss writing comment
                mCancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        final ImageView goToHistoryViewBtn = findViewById(R.id.add_history_button);
        goToHistoryViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hystoryActivity = new Intent(MoodActivity.this, HistoryActivity.class);
                startActivity(hystoryActivity);
            }
        });
    }

    public void addData(String newEntry){
        boolean inserData = mDatabaseHelper.addData(newEntry);

        if(inserData){
            toastMessage("Commentaire enregistré avec succès");
        } else {
            toastMessage("Votre commentaire n'a pu être enregistré, veuiller ressayer");
        }
    }

    /**
     * customizable message
     * @param message
     */

    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}

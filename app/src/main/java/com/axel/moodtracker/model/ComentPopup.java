package com.axel.moodtracker.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.axel.moodtracker.R;

public class ComentPopup extends Dialog {

    //field
    private String title;
    private String subTitle;
    private Button validateButton, cancelButton;
    private TextView titleView;
    private EditText subTitleView;

    //constructor
    public ComentPopup(Activity activity){

        super(activity, R.style.Theme_AppCompat_DayNight);
        setContentView(R.layout.my_popup);

        //set field
        this.title= "Mon titre";
        this.subTitle = "Mon sous titre";
        this.titleView = findViewById(R.id.title);
        this.subTitleView = findViewById(R.id.subTitle);
        this.validateButton = findViewById(R.id.validateButton);
        this.cancelButton = findViewById(R.id.cancelButton);
    }
    public void setTitle(String title){ this.title = title;}
    public void setSubTitle(String subTitle) {this.subTitle = subTitle;}
    public Button getValidateButton() { return validateButton; }
    public Button getCancelButton() { return cancelButton;}

    public void build(){

        show();
        //To obtain title and coment
        titleView.setText(title);
        subTitleView.setText(subTitle);
    }
}

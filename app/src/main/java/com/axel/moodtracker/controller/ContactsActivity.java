package com.axel.moodtracker.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.axel.moodtracker.R;
import com.axel.moodtracker.utils.Constants;
import com.bumptech.glide.Glide;
import com.muddzdev.styleabletoast.StyleableToast;

public class ContactsActivity extends AppCompatActivity {

    private TextView phone, txtMessage;
    private Button select, sendMessageBtn;
    private ImageView imageView;
    private String phoneNo;
    private String retreiveComment;
    private int retreiveColor;
    private int image;
    private ImageView homePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        initView();
        retreiveData();

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectContact();
            }
        });

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void initView() {
        phone = (TextView) findViewById(R.id.phone);
        txtMessage = (TextView) findViewById(R.id.message_contact);
        imageView = (ImageView) findViewById(R.id.image_smiley_contact);
        select = (Button) findViewById(R.id.select);
        sendMessageBtn = (Button) findViewById(R.id.send_message_btn);
        homePage = (ImageView) findViewById(R.id.ret);

        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactsActivity.this, HistoryActivity.class));
            }
        });
    }

    // retreive the data
    private void retreiveData() {
        retreiveComment = getIntent().getStringExtra(Constants.MY_COMMENT);
        retreiveColor = getIntent().getIntExtra(Constants.COLOR_MOOD, 3);
        image = getIntent().getIntExtra(Constants.CURRENT_ITEM, R.drawable.d_smiley_happy);
        Glide.with(this).load(Constants.mImageRessource[image]).into(imageView);
        txtMessage.setText(retreiveComment);
    }

    private void selectContact() {
        Intent in = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(in, Constants.RESULT_PICK_CONTACT);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void sendMessage() {

        String phoneNumber = phone.getText().toString();

        //If the comment field length is greater than 0 characters and the message is not empty, we start the sending procedure
        if (retreiveComment.length() > 0) {

            if (!phoneNumber.equals("")){
                //Thanks to the SMS management object (SmsManager) which is retrieved via the static getDefault () method
                //We send the SMS using the sendTextMessage method
                SmsManager.getDefault().sendTextMessage(phoneNumber, null, retreiveComment, null, null);
                //On efface les deux EditText
                txtMessage.setText("");
                String message = getResources().getString(R.string.successMessage);
                displayToast(getApplicationContext(), message);

            }else {
                String noPhoneNumber = getResources().getString(R.string.choose_number_phone);
                displayToast(getApplicationContext(), noPhoneNumber);
            }
        }
        else  {
            String chooseNumberPhone = getResources().getString(R.string.choose_number_phone);
            displayToast(getApplicationContext(), chooseNumberPhone);
        }
    }

    private void displayToast(Context applicationContext, String retreiveComment) {
        new StyleableToast
                .Builder(applicationContext)
                .text(retreiveComment)
                .textColor(Color.WHITE)
                .backgroundColor(Color.GREEN)
                .show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            String noContactSelected = getResources().getString(R.string.no_contact_selected);
            displayToast(getApplicationContext(), noContactSelected);
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;

        try {
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNo = cursor.getString(phoneIndex);
            savePhone(phoneNo);
            phone.setText(phoneNo);
            sendMessageBtn.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void savePhone(String phoneNo) {
   /*     SharedPreferences.Editor editor = getSharedPreferences(Constants.PREF_PHONE, MODE_PRIVATE).edit();
        editor.putString(Constants.PHONE_NUMBER, phoneNo);
        editor.apply()*/;
        phone.setText(phoneNo);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ContactsActivity.this, HistoryActivity.class));
    }
}

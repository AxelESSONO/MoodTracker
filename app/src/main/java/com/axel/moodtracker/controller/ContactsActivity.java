package com.axel.moodtracker.controller;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.gsm.SmsManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.axel.moodtracker.R;

import huxy.huxy.huxylab2.HuxyApp;

public class ContactsActivity extends AppCompatActivity {
    private static final int RESULT_PICK_CONTACT = 1;
    private TextView phone, txtMessage;
    private Button select, sendMessageBtn;
    private ImageView imageView;
    private String phoneNo;
    private String retreiveComment;
    private String retreiveColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        phone = (TextView) findViewById(R.id.phone);
        txtMessage = (TextView) findViewById(R.id.message_contact);
        imageView = (ImageView) findViewById(R.id.image_smiley_contact);
        select = (Button) findViewById(R.id.select);
        sendMessageBtn = (Button) findViewById(R.id.send_message_btn);

        // retreive the comment
        retreiveComment = getIntent().getStringExtra("myComment");
        retreiveColor = getIntent().getStringExtra("colorMood");
        imageView.setImageResource(loadSmileyImage(retreiveColor));
        txtMessage.setText(retreiveComment);
        //Toast.makeText(this, retreiveComment, Toast.LENGTH_SHORT).show();

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(in, RESULT_PICK_CONTACT);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                //Si le numéro est supérieur à 4 caractères et que le message n'est pas vide on lance la procédure d'envoi
                if (phoneNo.length() >= 4) {

                    if (retreiveComment.length() > 0){
                        //Grâce à l'objet de gestion de SMS (SmsManager) que l'on récupère via la méthode static getDefault()
                        //On envoie le SMS à l'aide de la méthode sendTextMessage
                        SmsManager.getDefault().sendTextMessage(phoneNo, null, retreiveComment, null, null);
                        //On efface les deux EditText
                        phone.setText("");
                        txtMessage.setText("");

                        HuxyApp.successToast(ContactsActivity.this, "Votre message a été envoyé avec succès!")
                                .setPadding(3)
                                .setPositionAndOffSet(Gravity.CENTER,0,30);

                    }else {
                        String emptyComment = "Vous ne pouvez pas envoyer un message vide!";
                        HuxyApp.dangerToast(ContactsActivity.this, emptyComment)
                                .setPadding(3)
                                .setPositionAndOffSet(Gravity.CENTER,0,0);
                    }

                } else {
                    //On affiche un petit message d'erreur dans un Toast
                    HuxyApp.successToast(ContactsActivity.this, "Veuillez choisir un numéro")
                            .setPadding(3)
                            .setPositionAndOffSet(Gravity.CENTER,0,30);
                }
            }
        });


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            HuxyApp.warningToast(ContactsActivity.this, "Aucun contact sélectionné!")
                    .setPadding(3)
                    .setPositionAndOffSet(Gravity.CENTER,0,0);
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;

        try {
            //String phoneNo = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            phoneNo = cursor.getString(phoneIndex);

            phone.setText(phoneNo);

            //Toast.makeText(ContactsActivity.this, "Le numéro est: " + phoneNo, Toast.LENGTH_SHORT).show();
            sendMessageBtn.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int loadSmileyImage(String color) {
        int idImage = 0;
        if (color.equals("#AB1A49")) {
            idImage = R.drawable.a_smiley_disappointed;
        }
        if (color.equals("#808A89")) {
            idImage = R.drawable.b_smiley_sad;
        }
        if (color.equals("#3135D0")) {
            idImage = R.drawable.c_smiley_normal;
        }
        if (color.equals("#55B617")) {
            idImage = R.drawable.d_smiley_happy;
        }
        if (color.equals("#D0E807")) {
            idImage = R.drawable.e_smiley_super_happy;
        }
        return idImage;
    }
}

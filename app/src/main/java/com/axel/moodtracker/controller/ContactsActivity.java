package com.axel.moodtracker.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.telephony.gsm.SmsManager;
import com.axel.moodtracker.R;
import com.axel.moodtracker.fragment.MainFragment;

public class ContactsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Intent intent = getIntent();
        String retrieveComment = "";
        if (intent != null)
        {
            //String retrieveComment = "";
            if (intent.hasExtra("myComment"))
            {

                retrieveComment = intent.getStringExtra("myComment");
                Toast.makeText(this, retrieveComment, Toast.LENGTH_SHORT).show();
            }
        }

        //============================= SHARE MOOD BY SENDING SMS TO FRIEND =====================================
        /**
        LinearLayout linearLayoutSms;
        linearLayoutSms = (LinearLayout) findViewById(R.id.linear_layout_sms);
        linearLayoutSms.setOnClickListener(new View.OnClickListener()
        {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v)
            {
                //Si le numéro est supérieur à 4 caractères et que le message n'est pas vide on lance la procédure d'envoi
                if(num.length()>= 4 && msg.length() > 0){
                    //Grâce à l'objet de gestion de SMS (SmsManager) que l'on récupère via la méthode static getDefault()
                    //On envoie le SMS à l'aide de la méthode sendTextMessage
                    SmsManager.getDefault().sendTextMessage(num, null, retrieveComment, null, null);
                    //On efface les deux EditText
                    numero.setText("");
                    message.setText("");
                }else{
                    //On affiche un petit message d'erreur dans un Toast
                    Toast.makeText(ContactsActivity.this, "Choisissez un numéro", Toast.LENGTH_SHORT).show();
                }
            }
        });
         **/
    }
}

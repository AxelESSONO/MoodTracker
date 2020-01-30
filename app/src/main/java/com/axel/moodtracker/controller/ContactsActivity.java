package com.axel.moodtracker.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.gsm.SmsManager;
import com.axel.moodtracker.R;

public class ContactsActivity extends AppCompatActivity
{
    private static final int RESULT_PICK_CONTACT =1;
    private TextView phone, txtMessage;
    private Button select, sendMessageBtn;
    private ImageView imageView;
    private String phoneNo = null;
    private String retreiveComment = null, retreiveColor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_contacts);

        phone = (TextView) findViewById (R.id.phone);
        txtMessage = (TextView) findViewById(R.id.message_contact);
        imageView = (ImageView) findViewById(R.id.image_smiley_contact);
        select = (Button) findViewById (R.id.select);
        sendMessageBtn = (Button) findViewById(R.id.send_message_btn);

        // retreive the comment
        retreiveComment = getIntent().getStringExtra("myComment");
        retreiveColor = getIntent().getStringExtra("colorMood");
        imageView.setImageResource(loadSmileyImage(retreiveColor));
        txtMessage.setText(retreiveComment);
        //Toast.makeText(this, retreiveComment, Toast.LENGTH_SHORT).show();


        select.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                Intent in = new Intent (Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult (in, RESULT_PICK_CONTACT);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        sendMessageBtn.setOnClickListener(new View.OnClickListener()
        {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v)
            {
                //Si le numéro est supérieur à 4 caractères et que le message n'est pas vide on lance la procédure d'envoi
                if(phoneNo.length()>= 4 && retreiveComment.length() > 0)
                {
                    //Grâce à l'objet de gestion de SMS (SmsManager) que l'on récupère via la méthode static getDefault()
                    //On envoie le SMS à l'aide de la méthode sendTextMessage
                    SmsManager.getDefault().sendTextMessage(phoneNo, null, retreiveComment, null, null);
                    //On efface les deux EditText
                    phone.setText("");
                    txtMessage.setText("");

                    Toast.makeText(ContactsActivity.this, "Votre message a été envoyé avec succès!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //On affiche un petit message d'erreur dans un Toast
                    Toast.makeText(ContactsActivity.this, "Choisissez un numéro", Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode,  Intent data)
    {
        if(resultCode==RESULT_OK)
        {
            switch (requestCode)
            {
                case RESULT_PICK_CONTACT:
                    contactPicked (data);
                    break;
            }
        }
        else
        {
            //Toast.makeText (this, "Failed To pick contact", Toast.LENGTH_SHORT).show ();
        }
    }

    private void contactPicked(Intent data)
    {
        Cursor cursor = null;

        try
        {
            //String phoneNo = null;
            Uri uri = data.getData ();
            cursor = getContentResolver ().query (uri, null, null,null,null);
            cursor.moveToFirst ();
            int phoneIndex = cursor.getColumnIndex (ContactsContract.CommonDataKinds.Phone.NUMBER);

            phoneNo = cursor.getString (phoneIndex);

            phone.setText (phoneNo);

            //Toast.makeText(ContactsActivity.this, "Le numéro est: " + phoneNo, Toast.LENGTH_SHORT).show();
            sendMessageBtn.setVisibility(View.VISIBLE);

        }
        catch (Exception e)
        {
            e.printStackTrace ();
        }
    }

// ==============================================================================================================
    /*// a static variable to get a reference of our application context
    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contextOfApplication = getApplicationContext();

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
        }*/

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
    // }

    private int loadSmileyImage(String color) {
        int idImage = 0;
        if(color.equals("#AB1A49")) {
            idImage = R.drawable.a_smiley_disappointed;
        }
        if(color.equals("#808A89")) {
            idImage = R.drawable.b_smiley_sad;
        }
        if(color.equals("#3135D0")) {
            idImage = R.drawable.c_smiley_normal;
        }
        if(color.equals("#55B617")) {
            idImage = R.drawable.d_smiley_happy;
        }
        if(color.equals("#D0E807")) {
            idImage =  R.drawable.e_smiley_super_happy;
        }
        return idImage;
    }
}

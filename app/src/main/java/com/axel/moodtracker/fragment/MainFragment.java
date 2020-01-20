package com.axel.moodtracker.fragment;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.axel.moodtracker.R;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.axel.moodtracker.R.id.photo;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment
{

    public MainFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final ListView list = (ListView) rootView.findViewById(android.R.id.list);
        final List<Map<String, Object>> contacts = retrieveContacts(getActivity().getContentResolver());

        if (contacts != null)
        {
            final SimpleAdapter adapter = new SimpleAdapter(getActivity(), contacts, R.layout.contact, new String[] { "name", "photo" }, new int[] { R.id.name,
                    photo });
            adapter.setViewBinder(new SimpleAdapter.ViewBinder()
            {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation)
                {
                    if ((view instanceof ImageView) & (data instanceof Bitmap))
                    {
                        final ImageView image = (ImageView) view;
                        final Bitmap photo = (Bitmap) data;
                        image.setImageBitmap(photo);
                        return true;
                    }
                    return false;
                }
            });

            //un ViewBinder permettant à notre SimpleAdapter d’afficher correctement les photos
            adapter.setViewBinder(new SimpleAdapter.ViewBinder()
            {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation)
                {
                    if ((view instanceof ImageView) & (data instanceof Bitmap))
                    {
                        final ImageView image = (ImageView) view;
                        Bitmap photo = (Bitmap) data;
                        image.setImageBitmap(photo);

                        return true;
                    }
                    return false;
                }
            });

            list.setAdapter(adapter);

            //=========================================================================================================
            list.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                { }
            });
            //========================================================================================================
        }

        return rootView;
        //return inflater.inflate(R.layout.fragment_main, container, false);
    }

    //==============================================================================================================================
    private List<Map<String, Object>> retrieveContacts(ContentResolver contentResolver)
    {
        final List<Map<String, Object>> contacts = new ArrayList<Map<String, Object>>();

        //final Set<String> contacts = new HashSet<String>();


        final Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[] { ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Data._ID, ContactsContract.Contacts.HAS_PHONE_NUMBER }, null, null, null);

        if (cursor == null)
        {
            Log.e("retrieveContacts", "Cannot retrieve the contacts");
            return null;
        }

        if (cursor.moveToFirst() == true)
        {
            do
            {
                final long id = Long.parseLong(cursor.getString(cursor.getColumnIndex(ContactsContract.Data._ID)));
                final String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                final int hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.HAS_PHONE_NUMBER));

                if (hasPhoneNumber > 0)
                {
                    Bitmap photo = getPhoto(contentResolver, id);
                    final Map<String, Object> contact = new HashMap<String, Object>();
                    contact.put("name", name);
                    contact.put("photo", photo);
                    contacts.add(contact);
                }
            }
            while (cursor.moveToNext() == true);
        }

        if (cursor.isClosed() == false)
        {
            cursor.close();
        }
        return contacts;
    }

    public interface OnItemSelectedListener
    {
        public void onMainFragmentItemSelected(String link);
    }

    private Bitmap getPhoto(ContentResolver contentResolver, long contactId)
    {
        Bitmap photo = null;
        final Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        final Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        final Cursor cursor = contentResolver.query(photoUri, new String[] { ContactsContract.Contacts.Photo.DATA15 }, null, null, null);

        if (cursor == null)
        {
            Log.e("getPhoto", "Cannot retrieve the photo of the contact with id '" + contactId + "'");
            return null;
        }

        if (cursor.moveToFirst() == true)
        {
            final byte[] data = cursor.getBlob(0);

            if (data != null)
            {
                photo = BitmapFactory.decodeStream(new ByteArrayInputStream(data));
            }
        }

        if (cursor.isClosed() == false)
        {
            cursor.close();
        }
        return photo;
    }
}
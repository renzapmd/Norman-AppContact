package com.example.norman_appcontact.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.norman_appcontact.Contact;
import com.example.norman_appcontact.R;

public class ContactAdapter extends ArrayAdapter<Contact> {
    Activity context;
    int resource;
    public ContactAdapter(Activity context, int resource){
        super(context, resource);
        this.context = context;
        this.resource = resource;

    }
    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        TextView txtId=convertView.findViewById(R.id.id);

        TextView txtName=convertView.findViewById(R.id.Name);
        TextView txtPhone=convertView.findViewById(R.id.Phone);
        TextView txtEmail=convertView.findViewById(R.id.Email);
        ImageView imageView=convertView.findViewById(R.id.imageView3);
        Contact contact = getItem(position);
        txtId.setText("ID:" + contact.getId());
        txtName.setText(contact.getName());
        txtPhone.setText(contact.getPhone());
        txtEmail.setText(contact.getEmail());
        if(contact.getPicture()!=null){
            byte[] decodedString = Base64.decode(contact.getPicture(), Base64.DEFAULT);
            Bitmap decodedBye = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedBye);
        }
        return convertView;
    }


}

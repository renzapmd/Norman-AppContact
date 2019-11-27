package com.example.norman_appcontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.norman_appcontact.Contact;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateContact extends AppCompatActivity {
    EditText edtId,edtName,edtPhone,edtEmail;
    ImageView imgPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);
        addControl();
        getContactDetail();

    }

    public void getContactDetail(){
        Intent intent = getIntent();
        final String key = intent.getStringExtra("Key");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("contacts");
        myRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Contact contact = dataSnapshot.getValue(Contact.class);
                contact.setId(dataSnapshot.getKey());
                edtId.setText(contact.getId());
                edtEmail.setText(contact.getEmail());
                edtName.setText(contact.getName());
                edtPhone.setText(contact.getPhone());
                if(contact.getPicture()!=null){
                    byte[] decodedString = Base64.decode(contact.getPicture(), Base64.DEFAULT);
                    Bitmap decodedBye = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imgPicture.setImageBitmap(decodedBye);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addControl(){
        edtId=findViewById(R.id.edtContactId);
        edtEmail=findViewById(R.id.edtEmail);
        edtName=findViewById(R.id.edtName);
        edtPhone=findViewById(R.id.edtPhone);
        imgPicture = (ImageView)findViewById(R.id.imageView);
    }

    public void updateContact(View view){
        String key= edtId.getText().toString();
        String phone= edtPhone.getText().toString();
        String name=edtName.getText().toString();
        String email=edtEmail.getText().toString();

        if (!email.matches("") && !phone.matches("") && !name.matches("") && !key.matches("")) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myref = database.getReference("contacts");
            myref.child(key).child("phone").setValue(phone);
            myref.child(key).child("email").setValue(email);
            myref.child(key).child("name").setValue(name);
            Intent intent = new Intent(UpdateContact.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
        else
            Toast.makeText(this,"Error:",Toast.LENGTH_LONG).show();

    }

    public void removeContact(View view){
        String key = edtId.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("contacts");
        myRef.child(key).removeValue();
        Intent intent = new Intent(UpdateContact.this, MainActivity.class);
        finish();
        startActivity(intent);

    }
}

package com.example.norman_appcontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
        DatabaseReference myref = database.getReference("contacts");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot childrenSnapshot = dataSnapshot.child(key);
                Contact c = childrenSnapshot.getValue(Contact.class);
                edtId.setText(key);
                edtEmail.setText(c.getEmail());
                edtName.setText(c.getName());
                edtPhone.setText(c.getPhone());

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
    }

    public void updateContact(View view){
        String key= edtId.getText().toString();
        String phone= edtPhone.getText().toString();
        String name=edtName.getText().toString();
        String email=edtEmail.getText().toString();
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("contacts");
        myref.child(key).child("phone").setValue(phone);
        myref.child(key).child("email").setValue(email);
        myref.child(key).child("name").setValue(name);
        finish();

    }

    public void removeContact(View view){
        String key = edtId.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("contacts");
        myRef.child(key).removeValue();
        finish();

    }
}

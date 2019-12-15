package com.example.norman_appcontact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactInfo extends AppCompatActivity {
    TextView Id, Name, Phone, Email;
    ImageView imgPicture;
    Bitmap selectedBitmap;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    FloatingActionButton fab, fab2;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        setToolbar();
        addcontrol();
        addEvents();
        getContactDetail();

    }

    public void addcontrol(){
        Id = findViewById(R.id.infoContactId);
        Name = findViewById(R.id.infoName);
        Phone = findViewById(R.id.infoPhone);
        Email = findViewById(R.id.infoEmail);
        imgPicture = findViewById(R.id.imageInfo);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

    }
    public void getContactDetail(){
        Intent intent = getIntent();
        final String key = intent.getStringExtra("Key");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child(user.getUid()).child("contacts").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Contact contact = dataSnapshot.getValue(Contact.class);
                contact.setId(dataSnapshot.getKey());
                Id.setText(contact.getId());
                Name.setText(contact.getName());
                Phone.setText(contact.getPhone());
                Email.setText(contact.getEmail());
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

    public void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbarInfo);
        setSupportActionBar(toolbar);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact Info");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }
    private void call(){
        Intent intent = getIntent();
        final String key = intent.getStringExtra("Key");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child(user.getUid()).child("contacts").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Contact contact = dataSnapshot.getValue(Contact.class);
                contact.setId(dataSnapshot.getKey());
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contact.getPhone(), null)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void addEvents(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                final String key = intent.getStringExtra("Key");
                Intent new_intent = new Intent(ContactInfo.this, UpdateContact.class);
                new_intent.putExtra("Key", key);
                startActivity(new_intent);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
            }
        });
    }
}

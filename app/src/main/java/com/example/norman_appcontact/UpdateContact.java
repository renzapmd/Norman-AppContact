package com.example.norman_appcontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateContact extends AppCompatActivity {

    EditText edtId,edtName,edtPhone,edtEmail;
    ImageView imgPicture;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    Button btnChoose;
    Button btnCapture;
    Bitmap selectedBitmap;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        addControl();
        setToolbar();
        getContactDetail();
        addEvents();
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
    public void addEvents() {
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePicture();
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoosePicture();
            }
        });
    }
    private void ChoosePicture() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 200);
    }

    private void capturePicture(){
        Intent  cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cInt, 100);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            selectedBitmap = (Bitmap) data.getExtras().get("data");
            imgPicture.setImageBitmap(selectedBitmap);
        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            try {
                Uri imageUri = data.getData();
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgPicture.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addControl(){
        edtId=findViewById(R.id.edtContactId);
        edtEmail=findViewById(R.id.edtEmail);
        edtName=findViewById(R.id.edtName);
        edtPhone=findViewById(R.id.edtPhone);
        imgPicture = (ImageView)findViewById(R.id.imageViewUpdate);
        btnChoose = (Button) findViewById(R.id.btnChooseUP);
        btnCapture = (Button) findViewById(R.id.btnCaptureUP);
    }

    public void updateContact(View view){
        String key = edtId.getText().toString();
        String phone = edtPhone.getText().toString();
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();

        if (!email.matches("") && !phone.matches("") && !name.matches("") && !key.matches("")&& !(selectedBitmap == null)) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myref = database.getReference();
            myref.child(user.getUid()).child("contacts").child(key).child("phone").setValue(phone);
            myref.child(user.getUid()).child("contacts").child(key).child("email").setValue(email);
            myref.child(user.getUid()).child("contacts").child(key).child("name").setValue(name);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String imgEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            myref.child(user.getUid()).child("contacts").child(key).child("picture").setValue(imgEncoded);
            Intent intent = new Intent(UpdateContact.this, ContactList.class);
            finish();
            startActivity(intent);
        }
        else
            Toast.makeText(this,"Error:",Toast.LENGTH_LONG).show();

    }

    public void removeContact(View view){
        String key = edtId.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child(user.getUid()).child("contacts").child(key).removeValue();
        Intent intent = new Intent(UpdateContact.this, ContactList.class);
        finish();
        startActivity(intent);

    }

    public void setToolbar(){
        toolbar =  findViewById(R.id.toolbarUP);
        setSupportActionBar(toolbar);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Contact");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });

    }
}

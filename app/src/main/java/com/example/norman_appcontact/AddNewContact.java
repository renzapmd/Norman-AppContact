package com.example.norman_appcontact;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddNewContact extends AppCompatActivity {
    EditText edtId, edtName, edtPhone, edtEmail;
    CircleImageView imgPicture;
    Button btnCapture;
    Button btnChoose;
    Bitmap selectedBitmap;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);
        addControls();
        addEvents();
        setToolbar();

    }

    private void addControls() {
        edtId = findViewById(R.id.edtContactId);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        btnCapture = (Button) findViewById(R.id.btnCapture);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        imgPicture = (CircleImageView) findViewById(R.id.imageView2);

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

    public void AddProcess(View view) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        String id = edtId.getText().toString();
        String email = edtEmail.getText().toString();
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();

        if (!email.matches("") && !phone.matches("") && !name.matches("") && !id.matches("") && !(selectedBitmap == null)) {
            myRef.child(user.getUid()).child("contacts").child(id).child("email").setValue(email);
            myRef.child(user.getUid()).child("contacts").child(id).child("name").setValue(name);
            myRef.child(user.getUid()).child("contacts").child(id).child("phone").setValue(phone);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String imgEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            myRef.child(user.getUid()).child("contacts").child(id).child("picture").setValue(imgEncoded);
            Intent intent = new Intent(AddNewContact.this, ContactList.class);
            finish();
            startActivity(intent);
        }
    else
            Toast.makeText(this,"Error:",Toast.LENGTH_LONG).show();
}
    public void setToolbar(){

        toolbar = (Toolbar) findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add New Contact");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });

    }
}


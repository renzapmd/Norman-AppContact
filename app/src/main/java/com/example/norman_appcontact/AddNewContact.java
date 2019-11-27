package com.example.norman_appcontact;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddNewContact extends AppCompatActivity {

    EditText edtId, edtName, edtPhone, edtEmail;
    ImageView imgPicture;
    ImageButton btnCapture, btnChoose;
    Bitmap selectedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);
        addControls();
        addEvents();

    }

    private void addControls() {
        edtId = findViewById(R.id.edtContactId);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        // btnCapture = (ImageButton) findViewById(R.id.btnCapture);
        btnChoose = (ImageButton) findViewById(R.id.btnChoose);
        imgPicture = (ImageView) findViewById(R.id.imageView);

    }

    public void addEvents() {
       /* btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePicture();
            }
        });
            */
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

    /*private void capturePicture(){
        Intent  cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cInt, 100);
    }*/

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
        DatabaseReference myRef = database.getReference("contacts");

        String id = edtId.getText().toString();
        String email = edtEmail.getText().toString();
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();

        if (!email.matches("") && !phone.matches("") && !name.matches("") && !id.matches("")) {
            myRef.child(id).child("email").setValue(email);
            myRef.child(id).child("name").setValue(name);
            myRef.child(id).child("phone").setValue(phone);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String imgEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            myRef.child(id).child("picture").setValue(imgEncoded);
            Intent intent = new Intent(AddNewContact.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    else
            Toast.makeText(this,"Error:",Toast.LENGTH_LONG).show();
}
}


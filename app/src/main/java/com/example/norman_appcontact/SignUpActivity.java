package com.example.norman_appcontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText emailField;
    EditText passField;
    EditText fname;
    EditText lname;
    EditText otherPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailField = findViewById(R.id.signupEmail);
        passField = findViewById(R.id.signupPassword);
        otherPass =  findViewById(R.id.signupConfirmPass);
        fname = findViewById(R.id.signupName);
        lname = findViewById(R.id.signupLName);
        mAuth = FirebaseAuth.getInstance();


        findViewById(R.id.signupCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.signupConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if fields are empty
                if(TextUtils.isEmpty(fname.getText().toString()) || TextUtils.isEmpty(lname.getText().toString()) || TextUtils.isEmpty(emailField.getText().toString()) || TextUtils.isEmpty(passField.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "Please fill in all fields.",
                            Toast.LENGTH_SHORT).show();
                }else if (!passField.getText().toString().equals(otherPass.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "Passwords aren't identical.",
                            Toast.LENGTH_SHORT).show();

                }else {
                    mAuth.createUserWithEmailAndPassword(emailField.getText().toString(), passField.getText().toString())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("test", "createUserWithEmail:success");

                                        FirebaseUser user = mAuth.getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(fname.getText().toString() + " " + lname.getText().toString())
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("test", "User profile updated.");
                                                            Intent contactListIntent = new Intent(SignUpActivity.this, ContactList.class);
                                                            startActivity(contactListIntent);
                                                            finish();
                                                        }
                                                    }
                                                });

                                        //updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("test", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }


                                }
                            });
                }





            }
        });

    }
}

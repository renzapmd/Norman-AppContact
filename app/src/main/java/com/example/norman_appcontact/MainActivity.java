package com.example.norman_appcontact;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.norman_appcontact.adapter.ContactAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    ListView lvContact;
    ContactAdapter adapter;
    String TAG="FIREBASE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControl();
        getContactsFromFirebase();

        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Contact contact = adapter.getItem(position);
                String key = contact.getId();
                Intent intent = new Intent(MainActivity.this, UpdateContact.class);
                intent.putExtra("Key", key);
                startActivity(intent);

            }
        });

    }
    public void addControl(){
        lvContact = findViewById(R.id.lvContact);
        adapter=new ContactAdapter(this, R.layout.item);
        lvContact.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void getContactsFromFirebase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("contacts");
        adapter.clear();
        adapter.notifyDataSetChanged();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren())
                {
                 Contact contact = data.getValue(Contact.class);
                 String key = data.getKey();
                 contact.setId(key);
                 adapter.add(contact);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.mnuAdd)
        {
            Intent intent=new Intent(MainActivity.this,AddNewContact.class);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.refresh)
        {
            finish();
            startActivity(getIntent());
        }
        return super.onOptionsItemSelected(item);

    }




}
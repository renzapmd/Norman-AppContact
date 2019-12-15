package com.example.norman_appcontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import androidx.appcompat.widget.SearchView;

import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.example.norman_appcontact.adapter.ContactAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class ContactList extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    FirebaseUser user;
    private FirebaseAuth mAuth;
    ContactAdapter adapter;
    RecyclerView recyclerView;
    List<Contact> lvContact = new ArrayList<>();
    SearchView searchView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    View navFooter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        addControl();
        setToolbar();
        setRView();
        setNavigation();

        getContactsFromFirebase();


    }
    public void addControl(){
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawLayout);
        recyclerView = (RecyclerView) findViewById(R.id.lvContact);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

    }

    public void setToolbar(){
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_title);

        toolbar.setNavigationIcon(R.drawable.ic_action_account_circle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    public void setRView(){
        adapter = new ContactAdapter(this, lvContact);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Contact contact = adapter.getItem(position);
                String key = contact.getId();
                Intent intent = new Intent(ContactList.this, ContactInfo.class);
                intent.putExtra("Key", key);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }
    public void setNavigation(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View navFooter1 = findViewById(R.id.footer_item_1);
        navFooter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(ContactList.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                menuItem.setChecked(true);
                switch (id) {
                    case R.id.NewContact:
                        Intent intent=new Intent(ContactList.this,AddNewContact.class);
                        startActivity(intent);
                        break;
                    case R.id.Home:
                        finish();
                        startActivity(getIntent());
                        break;
                }

                drawerLayout.closeDrawers();

                return true;
            }
        });
    }
    public void getContactsFromFirebase(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child(user.getUid()).child("contacts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data: dataSnapshot.getChildren())
                    {

                        Contact contact = data.getValue(Contact.class);
                        String key = data.getKey();
                        contact.setId(key);
                        lvContact.add(contact);
                    }
                    int a = lvContact.size();
                    adapter.notifyDataSetChanged();
                } else {
                }

         }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

        myRef.child(user.getUid()).child("userInfo").child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                         TextView username = findViewById(R.id.nav_user_name);
                         String name = dataSnapshot.getValue(String.class);
                         username.setText(name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
       myRef.child(user.getUid()).child("userInfo").child("email").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists()) {
                   TextView useremail = findViewById(R.id.nav_email);
                   String email = dataSnapshot.getValue(String.class);
                   useremail.setText(email);
               }
           }
           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
           }
       });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }


    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ContactAdapter.MyViewHolder) {
            dialContactPhone(lvContact.get(viewHolder.getAdapterPosition()).getPhone());
            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
        }
    }


    private void dialContactPhone(final String phoneNumber) {
        finish();
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }



}

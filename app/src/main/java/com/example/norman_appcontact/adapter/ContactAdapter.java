package com.example.norman_appcontact.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.norman_appcontact.Contact;
import com.example.norman_appcontact.R;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder>
implements  Filterable {



    private Fragment mFragment;
    private List<Contact> contactList;
    private List<Contact> contactListFilter;
    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtId, txtName, txtPhone, txtEmail;
        public ImageView imageView;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            txtId = (TextView) view.findViewById(R.id.id);
            txtName = (TextView) view.findViewById(R.id.Name);
            txtPhone = (TextView) view.findViewById(R.id.Phone);
            txtEmail = (TextView) view.findViewById(R.id.Email);
            imageView = (ImageView) view.findViewById(R.id.imageView3);
            viewBackground = (RelativeLayout) view.findViewById(R.id.view_background);
            viewForeground = (RelativeLayout) view.findViewById(R.id.view_foreground);

        }



    }

    public Contact getItem(int position) {
        return contactList.get(position);
    }

    public void removeContact(int position){
        contactList.remove(position);
        notifyItemRemoved(position);
    }


    public void restoreContact(Contact contact, int position){
        contactList.add(position, contact);
        notifyItemInserted(position);

    }
    public ContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.contactListFilter = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       final Contact contact = contactListFilter.get(position);
        holder.txtId.setText(contact.getId());
        holder.txtName.setText(contact.getName());
        holder.txtPhone.setText(contact.getPhone());
        holder.txtEmail.setText(contact.getEmail());
        if(contact.getPicture()!=null){
            byte[] decodedString = Base64.decode(contact.getPicture(), Base64.DEFAULT);
            Bitmap decodedBye = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imageView.setImageBitmap(decodedBye);
        }

    }

    @Override
    public int getItemCount() {
        return contactListFilter.size();
    }
    @Override

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFilter = contactList;
                } else {
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact row : contactList) {

                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence) || row.getEmail().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFilter = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }




}

package com.example.bloodgenix;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class myAdapter extends FirebaseRecyclerAdapter<DonationDetails,myAdapter.myviewholder> {

    public myAdapter(@NonNull FirebaseRecyclerOptions<DonationDetails> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull DonationDetails model) {
        holder.personNameSearch.setText(model.personFullName);
        holder.personLocationSearch.setText(model.donorLocation);
        Glide.with(holder.profileImageSearch.getContext()).load(model.personProfileImg).into(holder.profileImageSearch);

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_search_page,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder {

        CircleImageView profileImageSearch;
        TextView personNameSearch, personLocationSearch;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            profileImageSearch = itemView.findViewById(R.id.profileImageSearch);
            personNameSearch = itemView.findViewById(R.id.personNameSearch);
            personLocationSearch = itemView.findViewById(R.id.personLocationSearch);
        }

    }
}

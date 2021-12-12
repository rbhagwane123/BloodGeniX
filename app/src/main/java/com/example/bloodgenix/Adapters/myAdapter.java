package com.example.bloodgenix.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloodgenix.Models.DonationDetails;
import com.example.bloodgenix.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class myAdapter extends FirebaseRecyclerAdapter<DonationDetails, myAdapter.myviewholder> {

    private OnItemClickListener mListener;
    String SearchMobileNumber;

    public interface OnItemClickListener {
        String OnItemClick(int position, String number);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public myAdapter(@NonNull FirebaseRecyclerOptions<DonationDetails> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull DonationDetails model) {
        holder.personNameSearch.setText(model.personFullName);
        holder.personLocationSearch.setText(model.donorLocation);
        holder.bloodGroupVal.setText(model.blGroup);
        holder.personNumberSearch.setText(model.phoneNumber);
        Glide.with(holder.profileImageSearch.getContext()).load(model.personProfileImg).into(holder.profileImageSearch);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_search_page, parent, false);
        return new myviewholder(view, mListener);
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        CircleImageView profileImageSearch;
        TextView personNameSearch, personLocationSearch, bloodGroupVal;
        TextView personNumberSearch;

        public myviewholder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            profileImageSearch = itemView.findViewById(R.id.profileImageSearch);
            personNameSearch = itemView.findViewById(R.id.personNameSearch);
            personLocationSearch = itemView.findViewById(R.id.personLocationSearch);
            bloodGroupVal = itemView.findViewById(R.id.bloodGroupVal);
            personNumberSearch = itemView.findViewById(R.id.personNumberSearch);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.OnItemClick(position, personNumberSearch.getText().toString());
                        }
                    }
                }
            });
        }
    }
}

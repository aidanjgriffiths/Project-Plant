package com.example.plantproject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantproject.Model.BaseProfile;
import com.example.plantproject.R;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    private List<BaseProfile> prof;

    private OnNoteListener mOnNoteListener;

    // Provide a suitable constructor (depends on the kind of dataSet)
    public ProfileAdapter(List<BaseProfile> prof, OnNoteListener onNoteListener) {
        this.prof = prof;
        this.mOnNoteListener = onNoteListener;
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView profileView;
        public ImageView profilePicView, profileOpenView;
        OnNoteListener onNoteListener;

        public ProfileViewHolder(View view, OnNoteListener onNoteListener) {
            super(view);
            profileView = view.findViewById(R.id.profileView);
            profileOpenView = view.findViewById(R.id.profileOpenView);
            profilePicView = view.findViewById(R.id.profilePicView);
            this.onNoteListener = onNoteListener;
            view.setOnClickListener(this);  //set this if profile clickable
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_view, parent, false);
        return new ProfileViewHolder(itemView, mOnNoteListener);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        holder.profileView.setText(prof.get(position).getProfile());
        holder.profilePicView.setImageBitmap(prof.get(position).getImageOpenProfile1());
        holder.profileOpenView.setImageResource(prof.get(position).getImageOpenProfile2());
    }

    // Return the size of your dataSet (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return prof.size();
    }

}

package com.example.helloworld.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.helloworld.R;
import com.example.helloworld.utils.User;

import java.util.List;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {

    private List<User> userList;
    private OnRatingChangedListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;

        TextView userName;

        RatingBar ratingBar;

        TextView userInfo;

        View cardView;

        public ViewHolder(View view) {
            super(view);
            cardView = view;
            userImage = view.findViewById(R.id.info_icon);
            userName = view.findViewById(R.id.info_name);
            ratingBar = view.findViewById(R.id.info_rating);
            userInfo = view.findViewById(R.id.info_info);
        }
    }

    @NonNull
    @Override
    public CardViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_view, parent, false);
        final CardViewAdapter.ViewHolder holder = new CardViewAdapter.ViewHolder(view);
        holder.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> listener.onRatingChanged((int)rating, holder.getAdapterPosition()+1));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewAdapter.ViewHolder holder, int position) {
        User user = userList.get(position);
        Glide.with(holder.cardView).load(user.getImageId()).into(holder.userImage);
        holder.userInfo.setText(user.getInfo());
        holder.userName.setText(user.getName());
        holder.ratingBar.setRating(user.getRating());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public CardViewAdapter(List<User> list) {
        userList = list;
    }

    public List<User> getUserList() {
        return userList;
    }

    public interface OnRatingChangedListener{
        void onRatingChanged(int rating, int id);
    }


    public void setListener(OnRatingChangedListener listener) {
        this.listener = listener;
    }

}

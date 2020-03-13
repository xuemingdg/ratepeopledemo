package com.example.helloworld.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.helloworld.R;
import com.example.helloworld.utils.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> userList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onClick(View v, User user);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;

        TextView userName;

        View userView;

        public ViewHolder(View view) {
            super(view);
            userView = view;
            userImage = view.findViewById(R.id.user_icon);
            userName = view.findViewById(R.id.user_name);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_user, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.userView.setOnClickListener(view1 -> {
            int pos = holder.getAdapterPosition();
            User user = userList.get(pos);
            listener.onClick(view1, user);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        Glide.with(holder.userView).load(user.getImageId()).into(holder.userImage);
        holder.userName.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public UserAdapter(List<User> list) {
        userList = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}


package com.example.helloworld.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.helloworld.R;
import com.example.helloworld.utils.MySQLiteOpenHelper;
import com.example.helloworld.utils.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class SingleInfoFragment extends Fragment {

    MySQLiteOpenHelper mySQLiteOpenHelper;
    CardItemView cardItemView;
    private int userId = 1;
    private User user;
    private SQLiteDatabase db;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this.getContext(), "UserPool.db", null, 1);
        db = mySQLiteOpenHelper.getReadableDatabase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_singleinfo, container,false);
        cardItemView = view.findViewById(R.id.card_item_view);
        initLandscapeMode();
        return view;
    }


    private void initLandscapeMode() {
        EventBus.getDefault().register(this);
        Cursor cursor = mySQLiteOpenHelper.getUser(1,db );
        cursor.moveToFirst();
        String userName = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.USER_COLUMN_NAME));
        String image = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.USER_COLUMN_IMAGE));
        int rating = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.USER_COLUMN_RATING));
        String info = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.USER_COLUMN_INFO));
        User user = new User(1,userName, image, info, rating);
        cardItemView.fillData(user, getContext());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleUserEvent(User user) {
        cardItemView.getRatingBar().setOnRatingBarChangeListener(null);
        cardItemView.fillData(user, getContext());
        userId = user.getId();
        this.user = user;
        cardItemView.getRatingBar().setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            mySQLiteOpenHelper.updateUser(userId, (int)rating);
            user.setRating((int)rating);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}

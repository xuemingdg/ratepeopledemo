package com.example.helloworld.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.helloworld.R;
import com.example.helloworld.utils.User;

public class CardItemView extends LinearLayout {

    RatingBar ratingBar;

    TextView infoView;

    ImageView iconView;

    TextView nameView;

    public CardItemView(Context context) {
        this(context, null);
    }

    public CardItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.card_item_view, this);
        ratingBar = (RatingBar) findViewById(R.id.info_rating);
        infoView = (TextView) findViewById(R.id.info_info);
        iconView = (ImageView) findViewById(R.id.info_icon);
        nameView = (TextView) findViewById(R.id.info_name);
    }

    public void fillData(User itemData, Context context) {
        Glide.with(context).load(itemData.getImageId()).into(iconView);
        infoView.setText(itemData.getInfo());
        nameView.setText(itemData.getName());
        ratingBar.setRating(itemData.getRating());
    }

    public RatingBar getRatingBar() {
        return ratingBar;
    }
}

package com.example.helloworld.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class User implements Parcelable {
    private int id;
    private String name;
    private String imageId;
    private String info;
    private int rating;

    public User(){
        super();
    }
    public User(int id, String name, String imageId, String info, int rating) {
        super();
        this.id = id;
        this.name = name;
        this.imageId = imageId;
        this.info = info;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getImageId() {
        return imageId;
    }

    public String getInfo() {
        return info;
    }

    public int getRating() {
        return rating;
    }

    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(imageId);
        dest.writeString(info);
        dest.writeInt(rating);
    }

    public static final Parcelable.Creator<User> CREATOR = new Creator(){

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            // TODO Auto-generated method stub
            return new User[size];
        }
    };


    public User(Parcel parcel) {
        super();
        this.id = parcel.readInt();
        this.name = parcel.readString();
        this.imageId = parcel.readString();
        this.info = parcel.readString();
        this.rating = parcel.readInt();
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

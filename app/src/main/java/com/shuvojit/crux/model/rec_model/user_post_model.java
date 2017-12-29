package com.shuvojit.crux.model.rec_model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SHOBOJIT on 12/29/2017.
 */

public class user_post_model implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("comments")
    @Expose
    private List<String> comments = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.image);
        dest.writeString(this.user);
        dest.writeValue(this.v);
        dest.writeString(this.date);
        dest.writeStringList(this.comments);
    }

    public user_post_model() {
    }

    protected user_post_model(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.image = in.readString();
        this.user = in.readString();
        this.v = (Integer) in.readValue(Integer.class.getClassLoader());
        this.date = in.readString();
        this.comments = in.createStringArrayList();
    }

    public static final Parcelable.Creator<user_post_model> CREATOR = new Parcelable.Creator<user_post_model>() {
        @Override
        public user_post_model createFromParcel(Parcel source) {
            return new user_post_model(source);
        }

        @Override
        public user_post_model[] newArray(int size) {
            return new user_post_model[size];
        }
    };
}


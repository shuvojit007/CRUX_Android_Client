package com.shuvojit.crux.model.rec_model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SHOBOJIT on 12/30/2017.
 */

public class All_User_Post_Model implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("comments")
    @Expose
    private List<Object> comments = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    public List<Object> getComments() {
        return comments;
    }

    public void setComments(List<Object> comments) {
        this.comments = comments;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.description);
        dest.writeString(this.image);
        dest.writeString(this.title);
        dest.writeParcelable(this.user, flags);
        dest.writeValue(this.v);
        dest.writeString(this.date);
        dest.writeList(this.comments);
    }

    public All_User_Post_Model() {
    }

    protected All_User_Post_Model(Parcel in) {
        this.id = in.readString();
        this.description = in.readString();
        this.image = in.readString();
        this.title = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.v = (Integer) in.readValue(Integer.class.getClassLoader());
        this.date = in.readString();
        this.comments = new ArrayList<Object>();
        in.readList(this.comments, Object.class.getClassLoader());
    }

    public static final Parcelable.Creator<All_User_Post_Model> CREATOR = new Parcelable.Creator<All_User_Post_Model>() {
        @Override
        public All_User_Post_Model createFromParcel(Parcel source) {
            return new All_User_Post_Model(source);
        }

        @Override
        public All_User_Post_Model[] newArray(int size) {
            return new All_User_Post_Model[size];
        }
    };
}
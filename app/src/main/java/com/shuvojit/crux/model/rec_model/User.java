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
public class User implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("phnNumber")
    @Expose
    private String phnNumber;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("comments")
    @Expose
    private List<Object> comments = null;
    @SerializedName("post")
    @Expose
    private List<String> post = null;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("local")
    @Expose
    private Local local;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhnNumber() {
        return phnNumber;
    }

    public void setPhnNumber(String phnNumber) {
        this.phnNumber = phnNumber;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public List<Object> getComments() {
        return comments;
    }

    public void setComments(List<Object> comments) {
        this.comments = comments;
    }

    public List<String> getPost() {
        return post;
    }

    public void setPost(List<String> post) {
        this.post = post;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.method);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.phnNumber);
        dest.writeValue(this.v);
        dest.writeList(this.comments);
        dest.writeStringList(this.post);
        dest.writeString(this.image);
        dest.writeParcelable( this.local, flags);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.method = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.phnNumber = in.readString();
        this.v = (Integer) in.readValue(Integer.class.getClassLoader());
        this.comments = new ArrayList<Object>();
        in.readList(this.comments, Object.class.getClassLoader());
        this.post = in.createStringArrayList();
        this.image = in.readString();
        this.local = in.readParcelable(Local.class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
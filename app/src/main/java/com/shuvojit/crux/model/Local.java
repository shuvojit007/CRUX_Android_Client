package com.shuvojit.crux.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SHOBOJIT on 12/27/2017.
 */

public class Local implements Parcelable {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.password);
    }

    public Local() {
    }

    protected Local(Parcel in) {
        this.email = in.readString();
        this.password = in.readString();
    }

    public static final Parcelable.Creator<Local> CREATOR = new Parcelable.Creator<Local>() {
        @Override
        public Local createFromParcel(Parcel source) {
            return new Local(source);
        }

        @Override
        public Local[] newArray(int size) {
            return new Local[size];
        }
    };
}

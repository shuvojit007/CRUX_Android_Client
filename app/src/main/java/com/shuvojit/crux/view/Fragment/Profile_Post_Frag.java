package com.shuvojit.crux.view.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuvojit.crux.R;

/**
 * Created by SHOBOJIT on 12/28/2017.
 */

public class Profile_Post_Frag extends Fragment {
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.profile_post_frag,container,false);
        return v;
    }
}
package com.example.FacebookFriendList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created with IntelliJ IDEA.
 * User: nehalimaye
 * Date: 10/28/13
 * Time: 11:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelectionFragment extends Fragment {

    private static final String TAG = "SelectionFragment";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.selection,
                container, false);
        return view;
    }
}

package com.corebaseit.bevia.marvelcomicsreleaseapp.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.corebaseit.bevia.marvelcomicsreleaseapp.R;


/**
 * Created by vbevia on 23/03/16.
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class ComicsGridViewFragment extends Fragment {

    public ComicsGridViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    public static ComicsGridViewFragment newInstance() {
        ComicsGridViewFragment fragment = new ComicsGridViewFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grid_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

}

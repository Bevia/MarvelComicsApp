package com.corebaseit.bevia.marvelcomicsreleaseapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.corebaseit.bevia.marvelcomicsreleaseapp.R;
import com.corebaseit.bevia.marvelcomicsreleaseapp.adapters.ComicsGridAdapter;
import com.corebaseit.bevia.marvelcomicsreleaseapp.interfaces.OnComicsRecycleViewScrollListener;

public class ComicsGridViewFragment extends Fragment {

    private RecyclerView recyclerViewComics;
    private ComicsGridAdapter comicsListAdapter;
    private Context CONTEXT;
    private GridLayoutManager gm;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        CONTEXT = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerViewComics = (RecyclerView) inflater.inflate(R.layout.fragment_comics, container, false);
        return recyclerViewComics;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListComics();
    }

    private void initListComics() {

     /*   if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            gm  = new GridLayoutManager(CONTEXT, 2);
        } else {
            gm = new GridLayoutManager(CONTEXT, 3);
        }*/

        gm = new GridLayoutManager(CONTEXT, 2);
        comicsListAdapter = new ComicsGridAdapter(CONTEXT);
        recyclerViewComics.setLayoutManager(gm);
        recyclerViewComics.setAdapter(comicsListAdapter);
        recyclerViewComics.addOnScrollListener(new OnComicsRecycleViewScrollListener(gm) {
            @Override
            public void onLoadMore() {
                comicsListAdapter.requestForMoreComics();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comicsListAdapter.requestForMoreComics();
    }
}

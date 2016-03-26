package com.corebaseit.bevia.marvelcomicsreleaseapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.corebaseit.bevia.marvelcomicsreleaseapp.R;
import com.corebaseit.bevia.marvelcomicsreleaseapp.fragments.ComicDetailFragment;

public class ComicDetailActivity extends AppCompatActivity {

    public final static int COMIC_DETAIL_FRAGMENT = 0;
    public final static String COMIC_DETAIL_FRAGMENT_TAG = "detailFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comic_detail_activity);


        if (savedInstanceState == null) {
            savedInstanceState = getIntent().getExtras();
            fragmentSwitcher(savedInstanceState.getInt(COMIC_DETAIL_FRAGMENT_TAG), savedInstanceState);
        }
    }

    private void fragmentSwitcher(int fragmentId, Bundle args) {
        switch (fragmentId) {
            case COMIC_DETAIL_FRAGMENT:
                setFragment(ComicDetailFragment.getInstance(args), COMIC_DETAIL_FRAGMENT_TAG);
                break;
        }
    }

    private void setFragment(Fragment which, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detailContainer, which, tag)
                .commit();
    }
}

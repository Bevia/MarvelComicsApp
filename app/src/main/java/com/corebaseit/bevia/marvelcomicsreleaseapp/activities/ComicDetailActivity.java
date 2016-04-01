package com.corebaseit.bevia.marvelcomicsreleaseapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.corebaseit.bevia.marvelcomicsreleaseapp.R;
import com.corebaseit.bevia.marvelcomicsreleaseapp.fragments.ComicDetailFragment;

public class ComicDetailActivity extends AppCompatActivity {

    public final static String COMIC_DETAIL_FRAGMENT_TAG = "detailFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comic_detail_activity);

        if (savedInstanceState == null) {
            savedInstanceState = getIntent().getExtras();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailContainer,
                            ComicDetailFragment.getInstance(savedInstanceState), COMIC_DETAIL_FRAGMENT_TAG)
                    .commit();
        }
    }
}

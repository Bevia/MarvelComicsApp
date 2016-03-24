package com.corebaseit.bevia.marvelcomicsreleaseapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.corebaseit.bevia.marvelcomicsreleaseapp.fragments.ComicsGridViewFragment;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by vbevia on 23/03/16.
 */

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("  Marvel Comics");
        toolbar.setSubtitle("   list of comics");

        toolbar.setLogo(R.mipmap.ic_launcher);

        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, ComicsGridViewFragment.newInstance())
                    .commit();
        }
    }
}

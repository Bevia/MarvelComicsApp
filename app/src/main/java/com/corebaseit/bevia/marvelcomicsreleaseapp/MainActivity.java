package com.corebaseit.bevia.marvelcomicsreleaseapp;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.corebaseit.bevia.marvelcomicsreleaseapp.fragments.ComicsGridViewFragment;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by vbevia on 23/03/16.
 */

public class MainActivity extends AppCompatActivity {

    private SharedPreferences userInfoSettings;
    public static final String MY_PARAM_0 =
            "com.corebaseit.bevia.marvelcomicsreleaseapp.fragments.PARAM_0";
    public static final String MY_PARAM_1 =
            "com.corebaseit.bevia.marvelcomicsreleaseapp.fragments.PARAM_1";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        ButterKnife.bind(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("  Marvel Comics");
        toolbar.setSubtitle("   list of comics");

        toolbar.setLogo(R.mipmap.ic_launcher);
        Fragment fragment = null;
        fragment = new ComicsGridViewFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        //Cleaning files...
        File dir = new File(Environment.getExternalStorageDirectory()+"/Bevia/");
        deleteDirectory(dir);
        userInfoSettings = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor userInfoEdit = userInfoSettings.edit();
            userInfoEdit.putString(MY_PARAM_0, "missing");
            //userInfoEdit.putString(MY_PARAM_1, "missing");
            // Commit the edits!
            userInfoEdit.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    static public boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());

    }
}

package com.corebaseit.bevia.marvelcomicsreleaseapp.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.corebaseit.bevia.marvelcomicsreleaseapp.R;
import com.corebaseit.bevia.marvelcomicsreleaseapp.constants.Constants;
import com.corebaseit.bevia.marvelcomicsreleaseapp.network.UploadFileToDropbox;
import com.corebaseit.bevia.marvelcomicsreleaseapp.utils.HeroUserPictureStorage;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.TokenPair;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComicDetailFragment extends Fragment {

    @Bind(R.id.img_detail_character)
    ImageView heroImage;
    @Bind(R.id.heroName)
    TextView heroName;
    @Bind(R.id.heroDesc)
    TextView heroDesc;
    @Bind(R.id.butonimagen)
    Button butonimagen;
    @Bind(R.id.upload_file)
    Button uploadFile;
    @Bind(R.id.dropbox_login)
    Button login;
    @Bind(R.id.takePicture)
    TextView takePicture;

    private String heroID;
    private File newPhotofile;

    private SharedPreferences userInfoSettings;
    public static final String MY_PARAM_0 =
            "com.corebaseit.bevia.marvelcomicsreleaseapp.fragments.PARAM_0";//path of picture
    public static final String MY_PARAM_1 =
            "com.corebaseit.bevia.marvelcomicsreleaseapp.fragments.PARAM_1";//path of picture
    private DropboxAPI<AndroidAuthSession> dropbox;
    private final static String FILE_DIR = "/MarvelComicsReleaseApp/";
    private final static String DROPBOX_NAME = "Corebaseit";
    private final static String ACCESS_KEY = "0130l86m25ohiao";
    private final static String ACCESS_SECRET = "uffp4sjqfzfzgk2";
    private boolean isLoggedIn;
    final private int CAPTURE_IMAGE = 2;
    private boolean takenPicture = false;
    private String profilePicture = "profileUserPicture";
    private String selectedImagePath;
    private String newName;

    private Context context;

    public ComicDetailFragment() {
    }

    public static ComicDetailFragment getInstance(Bundle bundle) {
        ComicDetailFragment mCharacterDetailFragment = new ComicDetailFragment();
        mCharacterDetailFragment.setRetainInstance(true);
        mCharacterDetailFragment.setArguments(bundle);
        return mCharacterDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comic_detail, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Restore preferences
        userInfoSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String nameOfPerfilPictureInPreference = userInfoSettings.getString(MY_PARAM_0, "missing");
        String nameOfHeroIdInPreference = userInfoSettings.getString(MY_PARAM_1, "missing");

        heroName.setText(getArguments().getString(Constants.HERO_NAME));
        heroDesc.setText(getArguments().getString(Constants.HERO_DESC));
        heroID = getArguments().getString(Constants.ID_KEY);

        if (nameOfHeroIdInPreference.equals(heroID)) {

            Context contexts = getActivity().getApplicationContext();
            CharSequence texts = "ready to send image";
            int durationz = Toast.LENGTH_SHORT;

            Toast toasts = Toast.makeText(contexts, texts, durationz);
            toasts.show();

            getPicture(nameOfPerfilPictureInPreference, nameOfHeroIdInPreference);

        } else {
            Picasso.with(context)
                    .load(Uri.parse(getArguments().getString(Constants.THUMBNAIL_KEY)))
                    .into(heroImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                        }
                    });
        }

        butonimagen.setOnClickListener((View v) -> {

            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
            startActivityForResult(i, CAPTURE_IMAGE);

        });

        login.setOnClickListener((View view) -> {

            if (isLoggedIn) {
                dropbox.getSession().unlink();
                loggedIn(false);
            } else {
                dropbox.getSession().startAuthentication(getActivity());
            }

            Context context = getActivity().getApplicationContext();
            CharSequence text = "Login in...";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        });
        login.setVisibility(View.GONE);


        uploadFile.setOnClickListener((View view) -> {

            if (newPhotofile != null) {

                UploadFileToDropbox upload = new UploadFileToDropbox(getActivity(), dropbox, FILE_DIR, newPhotofile);
                upload.execute();

                Context context = getActivity().getApplicationContext();
                CharSequence text = "Loading image...";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } else {
                Context context = getActivity().getApplicationContext();
                CharSequence text = "You must take a picture first...";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        });
        uploadFile.setVisibility(View.GONE);


        loggedIn(false);

        AndroidAuthSession session;
        AppKeyPair pair = new AppKeyPair(ACCESS_KEY, ACCESS_SECRET);

        SharedPreferences prefs = getActivity().getSharedPreferences(DROPBOX_NAME, 0);
        String key = prefs.getString(ACCESS_KEY, null);
        String secret = prefs.getString(ACCESS_SECRET, null);

        if (key != null && secret != null) {
            AccessTokenPair token = new AccessTokenPair(key, secret);
            session = new AndroidAuthSession(pair, token);
        } else {
            session = new AndroidAuthSession(pair);
        }

        dropbox = new DropboxAPI<>(session);
        /**
         *                    Image setting:
         */

        /*deleteImage = (Button) getActivity().findViewById(R.id.delete);
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File dir = new File(Environment.getExternalStorageDirectory()+"/Bevia/");
                deleteDirectory(dir);
            }
        });*/
    }

    private void getPicture(String nameOfPerfilPictureInPreference, String nameOfHeroIdInPreference) {

        if (nameOfPerfilPictureInPreference.equals("missing")) {

            if (HeroUserPictureStorage.checkifImageExists(profilePicture)) {
                File photosEnPrederencia = HeroUserPictureStorage.getImage("/" + profilePicture + ".jpg");
                String path = (photosEnPrederencia.getAbsolutePath());

                Log.d("PerfilPicture", "picture from SD CARD!!!");

                if (path != null) {

                    Bitmap bitmapEnPreferenceias = decodeFile(photosEnPrederencia);

                    try {
                        ExifInterface exif = new ExifInterface(path);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        } else if (orientation == 3) {
                            matrix.postRotate(180);
                        } else if (orientation == 8) {
                            matrix.postRotate(270);
                        }
                        bitmapEnPreferenceias = Bitmap.createBitmap(bitmapEnPreferenceias, 0, 0,
                                bitmapEnPreferenceias.getWidth(), bitmapEnPreferenceias.getHeight(), matrix, true); // rotating bitmap
                    } catch (Exception e) {

                    }

                    heroImage.setImageBitmap(bitmapEnPreferenceias);
                }
            }

        } else {

            File photosEnPrederencia = new File(nameOfPerfilPictureInPreference);

            Log.d("PerfilPicture", "picture from preferencias!!!");

            Bitmap bitmapEnPreferenceias = decodeFile(photosEnPrederencia);

            try {
                ExifInterface exif = new ExifInterface(photosEnPrederencia.getAbsolutePath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                Matrix matrix = new Matrix();

                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                bitmapEnPreferenceias = Bitmap.createBitmap(bitmapEnPreferenceias, 0,
                        0, bitmapEnPreferenceias.getWidth(), bitmapEnPreferenceias.getHeight(), matrix, true); // rotating bitmap
            } catch (Exception e) {
            }

            //image button:
            heroImage.setImageBitmap(bitmapEnPreferenceias);
        }
    }

    /**
     * START: Methods for picture:
     */
    public Uri setImageUri() {

        File mediaImage = null;

        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root);
            if (!myDir.exists())
                return null;
            mediaImage = new File(myDir.getPath() + "/Bevia/" + profilePicture + ".jpg");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Uri imgUri = Uri.fromFile(mediaImage);
        this.selectedImagePath = mediaImage.getAbsolutePath();
        return imgUri;
    }

    public String getImagePath() {
        return selectedImagePath;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == CAPTURE_IMAGE && resultCode != Activity.RESULT_CANCELED) {

                selectedImagePath = getImagePath();

                File photos = new File(selectedImagePath);
                Bitmap bitmap = decodeFile(photos);

                //resave file with new name
                newName = profilePicture;
                File newFile = new File(newName);
                photos.renameTo(newFile);

                // save image internal path.....July 20th 2015
                SharedPreferences.Editor userInfoEdit = userInfoSettings.edit();
                userInfoEdit.putString(MY_PARAM_0, selectedImagePath);// password
                userInfoEdit.putString(MY_PARAM_1, heroID);// password

                // Commit the edits!
                userInfoEdit.commit();

                // making sure that image is never rotated!!!
                try {
                    ExifInterface exif = new ExifInterface(photos.getAbsolutePath());
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    // Log.d("EXIF", "Exif: " + orientation);
                    Matrix matrix = new Matrix();

                    if (orientation == 6) {
                        matrix.postRotate(90);
                    } else if (orientation == 3) {
                        matrix.postRotate(180);
                    } else if (orientation == 8) {
                        matrix.postRotate(270);
                    }
                    // rotating bitmap
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                } catch (Exception e) {

                }
                //image button:
                heroImage.setImageBitmap(bitmap);
                /**
                 *create a file to write the scaled bitmap data
                 */
                newPhotofile = new File(selectedImagePath);
                newPhotofile.createNewFile();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                byte[] bitmapdata = bos.toByteArray();
                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(newPhotofile);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap decodeFile(File f) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            //o.inSampleSize = 8;
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            final int REQUIRED_SIZE = 250; //it's safer for large images VB??

            //Find the correct scale value. It should be the power of 2, it's more efficient!
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            Log.e("ERRORFILE", "File not found");
        }
        return null;
    }

    public void loggedIn(boolean isLogged) {
        isLoggedIn = isLogged;
        uploadFile.setEnabled(isLogged);
        login.setText(isLogged ? "Logout" : "Login");
    }

    @Override
    public void onResume() {
        super.onResume();

        String nameOfHeroIdInPreference = userInfoSettings.getString(MY_PARAM_1, "missing");
        if (nameOfHeroIdInPreference.equals(heroID)) {
            takePicture.setText(" Login to upload image");
        }

        AndroidAuthSession session = dropbox.getSession();
        if (session.authenticationSuccessful()) {
            try {
                session.finishAuthentication();

                TokenPair tokens = session.getAccessTokenPair();
                SharedPreferences prefs = getActivity().getSharedPreferences(DROPBOX_NAME, 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(ACCESS_KEY, tokens.key);
                editor.putString(ACCESS_SECRET, tokens.secret);
                editor.commit();

                loggedIn(true);
                uploadFile.setVisibility(View.VISIBLE);
                login.setVisibility(View.VISIBLE);

            } catch (IllegalStateException e) {
                Toast.makeText(getActivity(), "Error during Dropbox authentication",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            uploadFile.setVisibility(View.GONE);
            //download.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }
    }
}

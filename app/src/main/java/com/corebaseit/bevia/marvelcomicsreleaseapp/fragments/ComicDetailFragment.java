package com.corebaseit.bevia.marvelcomicsreleaseapp.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.corebaseit.bevia.marvelcomicsreleaseapp.R;
import com.corebaseit.bevia.marvelcomicsreleaseapp.constants.Constants;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComicDetailFragment  extends Fragment {

    @Bind(R.id.img_detail_character)
    ImageView heroImage;
    @Bind(R.id.heroName)
    TextView heroName;
    @Bind(R.id.heroDesc)
    TextView heroDesc;
    @Bind(R.id.butonimagen)
    Button butonimagen;

    private Context context;

    public ComicDetailFragment() {
    }

    public static ComicDetailFragment getInstance(Bundle bundle){
        ComicDetailFragment mCharacterDetailFragment = new ComicDetailFragment();

        mCharacterDetailFragment.setArguments(bundle);
        return mCharacterDetailFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comic_detail, container, false);

        ButterKnife.bind(this, rootView);
        initView();

        return rootView;
    }

    private void initView(){

        butonimagen.setOnClickListener((View v) -> {

            Context context = getActivity().getApplicationContext();
            CharSequence text = "almost there...ready for picture?";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        });

        Picasso.with(context)
                .load(Uri.parse(getArguments().getString(Constants.THUMBNAIL_KEY)))
                        .into(heroImage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {}

                            @Override
                            public void onError() {}
                        });
        heroName.setText(getArguments().getString(Constants.HERO_NAME));
        heroDesc.setText(getArguments().getString(Constants.HERO_DESC));
    }
}

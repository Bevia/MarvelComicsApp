package com.corebaseit.bevia.marvelcomicsreleaseapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.corebaseit.bevia.marvelcomicsreleaseapp.R;
import com.corebaseit.bevia.marvelcomicsreleaseapp.activities.ComicDetailActivity;
import com.corebaseit.bevia.marvelcomicsreleaseapp.constants.Constants;
import com.corebaseit.bevia.marvelcomicsreleaseapp.marvelapis.MarvelApiClient;
import com.corebaseit.bevia.marvelcomicsreleaseapp.models.Comic;
import com.corebaseit.bevia.marvelcomicsreleaseapp.network.ComicsDeserializer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ComicsGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_PROGRESS = 0;
    private static final int VIEW_COMIC = 1;

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Comic> comics;

    public ComicsGridAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        comics = new ArrayList<>();
        comics.add(null);
        comics.add(null);
    }

    @Override
    public int getItemViewType(int position) {
        return comics.get(position) != null ? VIEW_COMIC : VIEW_PROGRESS;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = layoutInflater.inflate(R.layout.item_comic, parent, false);
        return new ComicViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (position < getItemCount() - 2)

            if (viewHolder instanceof ComicViewHolder) {

                Comic currentCumic = comics.get(position);

                ((ComicViewHolder) viewHolder).setImg(currentCumic.getUrlImage());
                ((ComicViewHolder) viewHolder).setComicData(currentCumic);

                Bundle bundle = new Bundle();

                bundle.putString(Constants.THUMBNAIL_KEY, String.valueOf(currentCumic.getUrlImage()));
                bundle.putString(Constants.HERO_NAME, String.valueOf(currentCumic.getTitle()));
                bundle.putString(Constants.ID_KEY, String.valueOf(currentCumic.getId()));

                viewHolder.itemView.setOnClickListener((View v) -> {
                    Intent detailCharacter = new Intent(context, ComicDetailActivity.class);
                    detailCharacter.putExtras(bundle);
                    context.startActivity(detailCharacter);
                });
            }
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

    public void requestForMoreComics() {
        MarvelApiClient.getInstance(context)
                .requestComicsList(Constants.CHARACTERS_LIMIT, getComicsItemsCount(), new retrofit.Callback<ComicsDeserializer>() {
                    @Override
                    public void success(ComicsDeserializer comicsListResponse, Response response) {
                        updateList(comicsListResponse.getComics());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
    }

    public void updateList(List<Comic> comics) {
        this.comics.addAll(getComicsItemsCount(), comics);
        notifyDataSetChanged();
    }

    private int getComicsItemsCount() {
        return comics.size() - 2;
    }

    public class ComicViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.img_comic)
        ImageView imgComic;

        @Bind(R.id.textViewTitleComic)
        TextView txtTItle;

        @Bind(R.id.textViewIssueComic)
        TextView txtIssue;


        public ComicViewHolder(View itemView, int type) {
            super(itemView);

            if (type == VIEW_COMIC) {
                ButterKnife.bind(this, itemView);
            }
        }

        public void setImg(Uri urlImage) {
            if (!urlImage.equals(Uri.EMPTY))

                Picasso.with(context)
                        .load(urlImage)
                        .into(imgComic, new Callback() {
                            @Override
                            public void onSuccess() {
                            }
                            @Override
                            public void onError() {

                            }
                        });
        }

        public void setComicData(Comic comic) {

            txtTItle.setText(comic.getTitle());
            txtIssue.setText("Issue # " + comic.getIssues());

        }

    }
}

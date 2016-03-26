package com.corebaseit.bevia.marvelcomicsreleaseapp.marvelapis;

import com.corebaseit.bevia.marvelcomicsreleaseapp.constants.Constants;
import com.corebaseit.bevia.marvelcomicsreleaseapp.network.ComicsDeserializer;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface MarvelApiService {

    @GET(Constants.COMICS_URL)
    public void requestComicsList(
            @Query(Constants.LIMIT_PARAM) int limit,
            @Query(Constants.OFFSET_PARAM) int offset,
            @Query(Constants.API_KEY_PARAM) String apiKey,
            @Query(Constants.TS_PARAM) long ts,
            @Query(Constants.HASH_PARAM) String hash,
            Callback<ComicsDeserializer> callback);
}

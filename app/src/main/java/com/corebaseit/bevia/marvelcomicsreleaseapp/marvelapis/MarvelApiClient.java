package com.corebaseit.bevia.marvelcomicsreleaseapp.marvelapis;

import android.content.Context;

import com.corebaseit.bevia.marvelcomicsreleaseapp.constants.Constants;
import com.corebaseit.bevia.marvelcomicsreleaseapp.network.ComicsDeserializer;
import com.corebaseit.bevia.marvelcomicsreleaseapp.network.ComicsGridDeserializer;
import com.corebaseit.bevia.marvelcomicsreleaseapp.utils.UtilMethods;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class MarvelApiClient {

    private MarvelApiService apiService;
    private Context context;

    private static MarvelApiClient API_CLIENT;

    public static MarvelApiClient getInstance(Context context) {
        //Singleton Pattern
        if(API_CLIENT == null)
            API_CLIENT = new MarvelApiClient(context);

        return API_CLIENT;
    }

    private MarvelApiClient(Context context) {
        this.context = context;
        //Build the response parser
        Gson gsonConf = new GsonBuilder()
                .registerTypeAdapter(ComicsDeserializer.class, new ComicsGridDeserializer())
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        //Retrofit adapter to make requests
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(Constants.MAIN_URL)
                .setConverter(new GsonConverter(gsonConf))
                .build();

        apiService = restAdapter.create(MarvelApiService.class);
    }

    /**
     * This method will return the retrofit interface to call any method available
     * */
    private MarvelApiService getApiService() {
        return apiService;
    }

    /**
     * @param limit How many heroes you want to request
     * @param offset Skip the specified number of heroes in the result set.
     * @param callback the interface to communicate the response
     * */

    public void requestComicsList (int limit, int offset, Callback<ComicsDeserializer> callback){
        Long ts = UtilMethods.generateTimeStamp();
        String hash = UtilMethods.generateHash(ts);

        getApiService().requestComicsList(limit,
                offset,
                Constants.API_PUBLIC_KEY,
                ts,
                hash,
                callback);
    }
}

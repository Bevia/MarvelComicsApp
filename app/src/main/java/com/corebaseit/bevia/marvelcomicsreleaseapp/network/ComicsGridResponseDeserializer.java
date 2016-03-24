package com.corebaseit.bevia.marvelcomicsreleaseapp.network;

import com.corebaseit.bevia.marvelcomicsreleaseapp.constants.Constants;
import com.corebaseit.bevia.marvelcomicsreleaseapp.models.Comic;
import com.corebaseit.bevia.marvelcomicsreleaseapp.models.ComicsDeserializer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vbevia on 23/03/16.
 */
public class ComicsGridResponseDeserializer implements JsonDeserializer<ComicsDeserializer> {


    @Override
    public ComicsDeserializer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        //First deserialize the attributes that are in the first level in the JsonObject.
        Gson gson = new Gson();
        ComicsDeserializer response = gson.fromJson(json, ComicsDeserializer.class);

        //Then deserialize all the attributes that are needed but are nested
        JsonObject data = json.getAsJsonObject()
                .getAsJsonObject(Constants.DATA_KEY);
        response.setOffset(data.get(Constants.OFFSET_KEY).getAsInt());

        JsonArray charactersArray = data.getAsJsonArray(Constants.RESULTS_KEY);
        response.setComics(extractComicsFromJson(charactersArray));

        return response;    }

    private List<Comic> extractComicsFromJson(JsonArray comicsArray) {
        List<Comic> comics = new ArrayList<>();

        for (int i = 0; i < comicsArray.size(); i++) {
            JsonObject characterData = comicsArray.get(i).getAsJsonObject();
            comics.add(Comic.buildFromJson(characterData));
        }

        return comics;
    }

}


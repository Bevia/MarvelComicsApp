package com.corebaseit.bevia.marvelcomicsreleaseapp.models;

import com.corebaseit.bevia.marvelcomicsreleaseapp.constants.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by vbevia on 24/03/16.
 */

public class ComicsDeserializer {
    @SerializedName(Constants.CODE_KEY)
    private int code;

    @SerializedName(Constants.STATUS_KEY)
    private String status;

    @Expose
    private int offset;

    @Expose
    List<Comic> comics;

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<Comic> getComics() {
        return comics;
    }

    public void setComics(List<Comic> comics) {
        this.comics = comics;
    }
}



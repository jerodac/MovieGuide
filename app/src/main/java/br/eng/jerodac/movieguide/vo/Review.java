package br.eng.jerodac.movieguide.vo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jean Rodrigo Dalbon Cunha on 06/10/2017.
 */
public class Review extends RestResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    @SerializedName("url")
    private String url;

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}

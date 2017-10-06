package br.eng.jerodac.movieguide.vo;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jean Rodrigo Dalbon Cunha on 06/10/2017.
 */
public class Video extends RestResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("iso_639_1")
    private String languageCode;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("site")
    private String site;

    @SerializedName("size")
    int size;

    @SerializedName("type")
    private String type;

    public String getId() {
        return id;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public boolean isYoutubeTrailer() {
        return (site.toLowerCase().equals("youtube") && type.toLowerCase().equals("trailer"));
    }

    public Uri getYoutubUrl() {
        return Uri.parse("http://www.youtube.com/watch?v=" + getKey());
    }
}

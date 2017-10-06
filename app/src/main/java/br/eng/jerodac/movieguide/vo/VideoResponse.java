package br.eng.jerodac.movieguide.vo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jean Rodrigo Dalbon Cunha on 06/10/2017.
 */
public class VideoResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private List<Video> videos;

    public int getId() {
        return id;
    }

    public List<Video> getVideos() {
        if (videos == null) {
            return Collections.EMPTY_LIST;
        }
        return videos;
    }

    public List<Video> getYoutubeTrailers() {
        List<Video> youtubeTrailers = new ArrayList<>();
        for (Video video : videos) {
            if (video.isYoutubeTrailer()) {
                youtubeTrailers.add(video);
            }
        }
        return youtubeTrailers;
    }
}

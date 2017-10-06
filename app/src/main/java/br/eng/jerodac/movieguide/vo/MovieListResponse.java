package br.eng.jerodac.movieguide.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jean Rodrigo Dalbon Cunha on 06/10/2017.
 */
public class MovieListResponse extends RestResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Movie> movies;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_results")
    private int totalResults;

    public int getPage() {
        return page;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }
}

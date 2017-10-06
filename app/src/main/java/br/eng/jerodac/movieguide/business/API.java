package br.eng.jerodac.movieguide.business;


import br.eng.jerodac.movieguide.vo.MovieListResponse;
import br.eng.jerodac.movieguide.vo.MovieResponse;
import br.eng.jerodac.movieguide.vo.ReviewResponse;
import br.eng.jerodac.movieguide.vo.VideoResponse;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Jean Rodrigo Dalbon Cunha on 06/10/2017.
 * TMDb API
 */
public interface API {

    /**
     * Top 20 movies sorted by specified criteria
     * @param sortBy
     * @param apiKey
     * @param cb
     */
    @GET("/discover/movie")
    void discoverMovies(@Query("sort_by") String sortBy, @Query("api_key") String apiKey, Callback<MovieListResponse> cb);

    /**
     * Top 20 sorted by popularity
     * @param apiKey
     * @param cb
     */
    @GET("/movie/popular")
    void fetchPopularMovies(@Query("api_key") String apiKey, Callback<MovieListResponse> cb);

    /**
     * Next page of 20 sorted by popularity
     * @param apiKey
     * @param page
     * @param cb
     */
    @GET("/movie/popular")
    void fetchPopularMovies(@Query("api_key") String apiKey, @Query("page") int page, Callback<MovieListResponse> cb);

    /**
     * Top 20 sorted by rating
     * @param apiKey
     * @param cb
     */
    @GET("/movie/top_rated")
    void fetchHighestRatedMovies(@Query("api_key") String apiKey, Callback<MovieListResponse> cb);

    /**
     * Next page of 20 sorted by rating
     * @param apiKey
     * @param page
     * @param cb
     */
    @GET("/movie/top_rated")
    void fetchHighestRatedMovies(@Query("api_key") String apiKey, @Query("page") int page, Callback<MovieListResponse> cb);

    /**
     * Get movie details by id
     * @param movieId
     * @param apiKey
     * @param cb
     */
    @GET("/movie/{id}")
    void fetchMovie(@Path("id") int movieId, @Query("api_key") String apiKey, Callback<MovieResponse> cb);


    /**
     *  Traillers
     * @param movieId
     * @param apiKey
     * @param cb
     */
    @GET("/movie/{id}/videos")
    void fetchVideos(@Path("id") int movieId, @Query("api_key") String apiKey, Callback<VideoResponse> cb);

    /**
     * Reviews
     * @param movieId
     * @param apiKey
     * @param cb
     */
    @GET("/movie/{id}/reviews")
    void fetchReviews(@Path("id") int movieId, @Query("api_key") String apiKey, Callback<ReviewResponse> cb);
}

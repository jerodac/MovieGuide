package br.eng.jerodac.movieguide.business;


import br.eng.jerodac.movieguide.vo.MovieListResponse;
import br.eng.jerodac.movieguide.vo.MovieResponse;
import br.eng.jerodac.movieguide.vo.ReviewResponse;
import br.eng.jerodac.movieguide.vo.VideoResponse;
import io.reactivex.Observable;
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
     */
    @GET("/discover/movie")
    Observable<MovieListResponse> discoverMovies(@Query("api_key") String apiKey, @Query("sort_by") String sortBy);

    /**
     * Top 20 sorted by popularity
     * @param apiKey
     */
    @GET("/3/movie/popular")
    Observable<MovieListResponse> fetchPopularMovies(@Query("api_key") String apiKey);
    
    /**
     * Next page of 20 sorted by popularity
     * @param apiKey
     * @param page
     */
    @GET("/3/movie/popular")
    Observable<MovieListResponse> fetchPopularMovies(@Query("api_key") String apiKey, @Query("page") int page);

    /**
     * Top 20 sorted by rating
     * @param apiKey
     */
    @GET("/movie/top_rated")
    Observable<MovieListResponse> fetchHighestRatedMovies(@Query("api_key") String apiKey);

    /**
     * Next page of 20 sorted by rating
     * @param apiKey
     * @param page
     */
    @GET("/movie/top_rated")
    Observable<MovieListResponse> fetchHighestRatedMovies(@Query("api_key") String apiKey, @Query("page") int page);

    /**
     * Get movie details by id
     * @param movieId
     * @param apiKey
     */
    @GET("/movie/{id}")
    Observable<MovieResponse> fetchMovie(@Path("id") int movieId, @Query("api_key") String apiKey);


    /**
     *  Traillers
     * @param movieId
     * @param apiKey
     */
    @GET("/movie/{id}/videos")
    Observable<VideoResponse> fetchVideos(@Path("id") int movieId, @Query("api_key") String apiKey);

    /**
     * Reviews
     * @param movieId
     * @param apiKey
     */
    @GET("/movie/{id}/reviews")
    Observable<ReviewResponse> fetchReviews(@Path("id") int movieId, @Query("api_key") String apiKey);
}

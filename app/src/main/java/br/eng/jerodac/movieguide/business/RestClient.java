package br.eng.jerodac.movieguide.business;

import br.eng.jerodac.movieguide.vo.MovieListResponse;
import br.eng.jerodac.movieguide.vo.MovieResponse;
import br.eng.jerodac.movieguide.vo.ReviewResponse;
import br.eng.jerodac.movieguide.vo.VideoResponse;
import io.reactivex.Observable;

/**
 * Created by Jean Rodrigo Dalbon Cunha on 06/10/2017.
 */
public class RestClient {

    private static final String KEY = "29c31311d307b97ab2a04b4dcdab150d";

    /**
     * Get Top 20 sorted by popularity
     */
    public static Observable<MovieListResponse> fetchPopularMovies() {
        return Configuration.getApi().fetchPopularMovies(KEY);
    }

    /**
     * Get Top 20 sorted by popularity
     * Pagination
     */
    public static Observable<MovieListResponse> fetchPopularMovies(int page) {
        return Configuration.getApi().fetchPopularMovies(KEY, page);
    }

    /**
     * Top 20 sorted by rating
     */
    public static Observable<MovieListResponse> fetchHighestRatedMovies(){
        return Configuration.getApi().fetchHighestRatedMovies(KEY);
    }

    /**
     * Next page of 20 sorted by rating
     * Pagination
     */
    public static Observable<MovieListResponse> fetchHighestRatedMovies(int page){
        return Configuration.getApi().fetchHighestRatedMovies(KEY, page);
    }

    /**
     *  Get movie details by id
     */
    public static Observable<MovieResponse> fetchMovie(int movieId){
        return Configuration.getApi().fetchMovie(movieId, KEY);
    }

    /**
     *  Traillers
     */
    public static Observable<VideoResponse> fetchVideos(int movieId){
        return Configuration.getApi().fetchVideos(movieId, KEY);
    }

    /**
     *  Reviews
     */
    public static Observable<ReviewResponse> fetchReviews(int movieId){
        return Configuration.getApi().fetchReviews(movieId, KEY);
    }

}

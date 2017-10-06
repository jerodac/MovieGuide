package br.eng.jerodac.movieguide.business;

import br.eng.jerodac.movieguide.vo.MovieListResponse;
import io.reactivex.Observable;

/**
 * Created by cin_jcunha on 06/10/2017.
 */

public class RestClient {

    private static final String KEY = "29c31311d307b97ab2a04b4dcdab150d";

    /**
     * Get Top 20 sorted by popularity
     */
    public static Observable<MovieListResponse> fetchPopularMovies() {
        return Configuration.getApi().fetchPopularMovies(KEY);
    }
}

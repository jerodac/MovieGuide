package br.eng.jerodac.movieguide.interfaces;

import br.eng.jerodac.movieguide.business.RestError;
import br.eng.jerodac.movieguide.vo.MovieListResponse;

/**
 * Created by Jean Rodrigo Dalbon Cunha on 06/10/2017.
 */
public interface MovieListListener {
    public void success(MovieListResponse response);
    public void error(RestError error);
}

package br.eng.jerodac.movieguide.interfaces;

import br.eng.jerodac.movieguide.business.ApiError;
import br.eng.jerodac.movieguide.vo.MovieResponse;

/**
 * Created by Jean Rodrigo Dalbon Cunha on 06/10/2017.
 */
public interface MovieListener {
    public void success(MovieResponse response);
    public void error(ApiError error);
}

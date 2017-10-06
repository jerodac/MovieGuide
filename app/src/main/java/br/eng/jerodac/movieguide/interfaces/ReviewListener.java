package br.eng.jerodac.movieguide.interfaces;

import br.eng.jerodac.movieguide.business.RestError;
import br.eng.jerodac.movieguide.vo.ReviewResponse;

/**
 * Created by Jean Rodrigo Dalbon Cunha on 06/10/2017.
 */
public interface ReviewListener {
    public void success(ReviewResponse response);
    public void error(RestError restError);
}

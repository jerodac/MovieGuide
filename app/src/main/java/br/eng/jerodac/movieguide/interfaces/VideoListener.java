package br.eng.jerodac.movieguide.interfaces;

import br.eng.jerodac.movieguide.business.RestError;
import br.eng.jerodac.movieguide.vo.VideoResponse;

/**
 * Created by Jean Rodrigo Dalbon Cunha on 06/10/2017.
 */
public interface VideoListener {
    public void success(VideoResponse response);
    public void error(RestError restError);
}

package br.eng.jerodac.movieguide.controllers;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import br.eng.jerodac.movieguide.business.FactoryRequester;
import br.eng.jerodac.movieguide.business.RestClient;
import br.eng.jerodac.movieguide.business.RestError;
import br.eng.jerodac.movieguide.interfaces.MovieListListener;
import br.eng.jerodac.movieguide.vo.MovieListResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Jean Rodrigo Dalbon Cunha on 06/10/2017.
 */
public class MainController {
    private static MainController mController;

    public static MainController getInstance(){
        if(mController == null){
            mController = new MainController();
        }
        return mController;
    }

    public void requestMostPopularMovies(@Nullable final MovieListListener listener){
        RestClient.fetchPopularMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FactoryRequester<MovieListResponse>() {
                    @Override
                    public void onSuccess(MovieListResponse movieListResponse) {
                            listener.success(movieListResponse);
                    }

                    @Override
                    public void onError(RestError restError) {
                            listener.error(restError);
                    }
                });
    }

}

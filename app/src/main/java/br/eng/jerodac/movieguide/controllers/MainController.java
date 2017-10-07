package br.eng.jerodac.movieguide.controllers;

import android.support.annotation.Nullable;

import br.eng.jerodac.movieguide.business.FactoryRequester;
import br.eng.jerodac.movieguide.business.RestClient;
import br.eng.jerodac.movieguide.business.RestError;
import br.eng.jerodac.movieguide.interfaces.MovieListListener;
import br.eng.jerodac.movieguide.interfaces.MovieListener;
import br.eng.jerodac.movieguide.interfaces.ReviewListener;
import br.eng.jerodac.movieguide.interfaces.VideoListener;
import br.eng.jerodac.movieguide.vo.MovieListResponse;
import br.eng.jerodac.movieguide.vo.MovieResponse;
import br.eng.jerodac.movieguide.vo.ReviewResponse;
import br.eng.jerodac.movieguide.vo.VideoResponse;
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

    public void requestMostPopularMovies(int page, @Nullable final MovieListListener listener){
        RestClient.fetchPopularMovies(page)
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

    public void requestHighestRatedMovies(@Nullable final MovieListListener listener){
        RestClient.fetchHighestRatedMovies()
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

    public void requestHighestRatedMovies(int page, @Nullable final MovieListListener listener){
        RestClient.fetchHighestRatedMovies(page)
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


    public void requestMovieReviews(int movieId, final ReviewListener listener){
        RestClient.fetchReviews(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FactoryRequester<ReviewResponse>() {
                    @Override
                    public void onSuccess(ReviewResponse reviewResponse) {
                        listener.success(reviewResponse);
                    }

                    @Override
                    public void onError(RestError restError) {
                        listener.error(restError);
                    }
                });
    }

    public void requestMovieVideos(int movieId, @Nullable final VideoListener listener){
        RestClient.fetchVideos(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FactoryRequester<VideoResponse>() {
                    @Override
                    public void onSuccess(VideoResponse videoResponse) {
                        listener.success(videoResponse);
                    }

                    @Override
                    public void onError(RestError restError) {
                        listener.error(restError);
                    }
                });
    }


    public void requestMovie(int movieId, @Nullable final MovieListener listener){
        RestClient.fetchMovie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FactoryRequester<MovieResponse>() {
                    @Override
                    public void onSuccess(MovieResponse movieResponse) {
                        listener.success(movieResponse);
                    }

                    @Override
                    public void onError(RestError restError) {
                        listener.error(restError);
                    }
                });
    }

}

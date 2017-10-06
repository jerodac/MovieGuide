package br.eng.jerodac.movieguide.business;

import java.net.UnknownHostException;

import retrofit2.HttpException;

public class ErrorHandler {

    public void error(Throwable mError, ErrorListener listener) {
        try {
            RestError restError = new RestError(mError);
            if (mError instanceof HttpException) {
                String error;
                if (mError instanceof HttpException) {
                    error = ((HttpException) mError).response().errorBody().string();
                }
            } else if (mError instanceof UnknownHostException) {
                AppLog.e(AppLog.TAG, "Conexão com internet não detectada.");
                restError.setConnectionError(true);
                listener.onError(restError);
            } else {
                listener.onError(restError);
            }
        } catch (Exception e) {
            listener.onError(new RestError(e));
        }
    }

    public interface ErrorListener {
        void onError(RestError error);
    }
}

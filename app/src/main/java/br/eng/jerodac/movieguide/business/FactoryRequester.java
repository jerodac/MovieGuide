package br.eng.jerodac.movieguide.business;

import android.util.Log;

import br.eng.jerodac.movieguide.vo.RestResponse;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public abstract class FactoryRequester<T extends RestResponse> extends DisposableObserver<T>
        implements ErrorHandler.ErrorListener {

    private ErrorHandler mErrorHandler;

    public FactoryRequester() {
        mErrorHandler = new ErrorHandler();
    }

    public abstract void onSuccess(T t);

    public abstract void onError(RestError restError);

    @Override
    public void onNext(@NonNull T t) {
        onSuccess(t);
    }

    @Override
    public void onError(@NonNull Throwable throwable) {
        mErrorHandler.error(throwable, this);
    }

    @Override
    public void onComplete() {
        Log.i("FactoryAsyncTask2", "onComplete");
    }
}

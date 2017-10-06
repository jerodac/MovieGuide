package br.eng.jerodac.movieguide.business;

/**
 * @author Jean Rodrigo Dalbon Cunha on 28/02/17.
 */
public class RestError {

    private Throwable exception;
    private boolean connectionError;

    public RestError(Throwable e) {
        exception = e;
        connectionError = false;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public boolean isConnectionError() {
        return connectionError;
    }

    public void setConnectionError(boolean connectionError) {
        this.connectionError = connectionError;
    }
}

package br.eng.jerodac.movieguide.business;

import android.util.Log;

/**
 * @author Jean Rodrigo D. Cunha on 30/08/16.
 */
public class AppLog {

    //Ativa logs
    static final boolean isDebug = true;
    public final static String TAG = "MovieGuide";

    public static void e(String tag, String message) {
        if (isDebug) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Throwable e) {
        if (isDebug) {
            Log.e(tag, message, e);
        }
    }

    public static void i(String tag, String message) {
        if (isDebug) {
            Log.i(tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (isDebug) {
            Log.v(tag, message);
        }
    }

    public static boolean isDebug() {
        return isDebug;
    }
}

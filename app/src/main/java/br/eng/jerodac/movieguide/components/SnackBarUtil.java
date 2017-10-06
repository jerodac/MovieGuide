package br.eng.jerodac.movieguide.components;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;


/**
 * Created by Jean Rodrigo Dalbon Cunha on 06/10/2017.
 */
public class SnackBarUtil {

    private Snackbar mSnackbar;
    private View mView;

    public SnackBarUtil(View view) {
        mView = view;
    }

    public void showMessageAction(String textButton, String message, final OnClickListener listener) {
        mSnackbar = Snackbar.make(mView, message, Snackbar.LENGTH_INDEFINITE);
        View view = mSnackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        mSnackbar.setAction(textButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnackbar.dismiss();
                if (listener != null) {
                    listener.onClick();
                }
            }
        });
        mSnackbar.show();
    }

    public void showMessageSuccess(String message) {
        mSnackbar = Snackbar.make(mView, message, Snackbar.LENGTH_LONG);
        View view = mSnackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        mSnackbar.show();
    }

    public void showMessageActionOK(String message, final OnClickListener listener) {
        showMessageAction("OK", message, listener);
    }

    public interface OnClickListener {
        public void onClick();
    }

    public void onDestroy() {
        if (mSnackbar != null) {
            mSnackbar.dismiss();
            mSnackbar = null;
        }
    }

}

package kyasa.com.popularmovies1.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import kyasa.com.popularmovies1.R;

/**
 * Created by kiran on 25/5/16.
 */
public class Utils {

    private static ProgressDialog mProgressDialog;

    private static void initProgressDialog(Context mContext) {

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    public static void showProgressDialog(Context mContext) {

        try {
            if (mProgressDialog == null)
                initProgressDialog(mContext);
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();
        } catch (Exception e) {
        }
    }

    public static void disMissProgressDialog() {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connection = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = null;
        if (connection != null) {
            nInfo = connection.getActiveNetworkInfo();
        }
        if (nInfo == null || !nInfo.isConnectedOrConnecting()) {
            return false;
        }

        if (nInfo == null || !nInfo.isConnected()) {
            return false;
        }
        if (nInfo != null && ((nInfo.getType() == ConnectivityManager.TYPE_MOBILE) || (nInfo.getType() == ConnectivityManager.TYPE_WIFI))) {
            if (nInfo.getState() != NetworkInfo.State.CONNECTED || nInfo.getState() == NetworkInfo.State.CONNECTING) {
                return false;
            }
        }
        return true;
    }


}

package com.decode.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by lucian.cioroga on 3/14/2018.
 */

public class Permissions {
    private final static String PERMISSION_SHARED_PREFERENCES = "preferences-permissions";

    /**
     * Checks the permission against an activity. It will call the apropriate callback depending on the state of the permission.
     *
     * @param activity
     * @param permission
     * @param code
     * @param callback
     */
    public static void check(Activity activity, String permission, int code, Callback callback) {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            SharedPreferences prefs = activity.getSharedPreferences(PERMISSION_SHARED_PREFERENCES, Context.MODE_PRIVATE);
            boolean wasRequested = prefs.getBoolean("requested_" + permission, false);

            // ActivityCompat.shouldShowRequestPermissionRationale can be false in 3 situations
            // * never asked for permission before
            // * The user has checked the 'never again' checkbox
            // * The permission has been disabled by policy (say, in a work situation)
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                if (callback != null)
                    callback.onNeedMoreInfo(permission);
            } else if (!wasRequested) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("requested_" + permission, true);
                editor.commit();
                ActivityCompat.requestPermissions(activity, new String[]{permission}, code);
            } else {
                // arriving here means that the user has requested the permission, and he was denied with "never ask again"
                if (callback != null)
                    callback.onNeverAskAgain(permission);
            }
        } else if (callback != null)
            callback.onAllowed(permission);
    }

    public interface Callback {
        void onNeedMoreInfo(String permission);

        void onNeverAskAgain(String permission);

        void onAllowed(String permission);
    }
}

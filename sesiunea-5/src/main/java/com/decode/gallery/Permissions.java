package com.decode.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by lucian.cioroga on 3/14/2018.
 */

public class Permissions {
    private final static String PERMISSION_SHARED_PREFERENCES = "preferences-permissions";
    public final static int REQUEST_PERMISSION_SETTING = 11;

    private static boolean mChecking = false;

    /**
     * Checks the permission against an activity. It will call the apropriate callback depending on the state of the permission.
     *
     * @param activity
     * @param permission
     * @param code
     * @param callback
     */
    public static void check(Activity activity, String permission, int code, Callback callback) {
        if (mChecking)
            return;

        mChecking = true;
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            SharedPreferences prefs = activity.getSharedPreferences(PERMISSION_SHARED_PREFERENCES, Context.MODE_PRIVATE);
            boolean wasRequested = prefs.getBoolean("requested_" + permission, false);

            // ActivityCompat.shouldShowRequestPermissionRationale can be false in 3 situations
            // * never asked for permission before
            // * The user has checked the 'never again' checkbox
            // * The permission has been disabled by policy (say, in a work situation)
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                if (callback != null)
                    callback.onPermissionNeedMoreInfo(permission);
            } else if (!wasRequested) {
                request(activity, permission, code);
            } else {
                // arriving here means that the user has requested the permission, and he was denied with "never ask again"
                if (callback != null)
                    callback.onPermissionNeverAskAgain(permission);
            }
        } else if (callback != null)
            callback.onPermissionAllowed(permission);
    }

    public static void request(Activity activity, String permission, int code) {
        SharedPreferences prefs = activity.getSharedPreferences(PERMISSION_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("requested_" + permission, true);
        editor.commit();
        ActivityCompat.requestPermissions(activity, new String[]{permission}, code);
    }

    public static void settings(Activity activity) {
        mChecking = false;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
    }

    public static void onPermissionsRequestResult(Activity activity, String permission, String[] permissions, int[] grantResults, Callback callback) {
        boolean allowed = false;
        for (String p : permissions)
            if (p.equals(permission)) {
                allowed = true;
                break;
            }

        if (allowed && grantResults.length > 0)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                callback.onPermissionAllowed(permission);
            else if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                callback.onPermissionDenied(permission);
    }

    public static void reset() {
        mChecking = false;
    }

    public interface Callback {
        void onPermissionNeedMoreInfo(String permission);

        void onPermissionNeverAskAgain(String permission);

        void onPermissionAllowed(String permission);

        void onPermissionDenied(String permission);
    }
}

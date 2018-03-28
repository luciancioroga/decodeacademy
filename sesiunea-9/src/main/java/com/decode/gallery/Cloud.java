package com.decode.gallery;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by lucian.cioroga on 3/28/2018.
 */
public class Cloud extends Service {
    public static final String ACTION_CLOUD = "com.decode.gallery.action-cloud";

    private final IBinder mBinder = new CloudBinder();
    private Executor mExecutor;
    private DB.Helper mDB;

    @Override
    public IBinder onBind(Intent intent) {
        mExecutor = Executors.newSingleThreadExecutor();
        mDB = new DB.Helper(getApplicationContext());
        return mBinder;
    }

    public class CloudBinder extends Binder {
        Cloud getService() {
            // Return this instance of Cloud so clients can call public methods
            return Cloud.this;
        }
    }

    /**
     * client methods
     */
    public void fetch() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // 1. access api
                // 2. interpret json
                Photo[] photos = U.api("https://goo.gl/xATgGr", Photo[].class);
                SQLiteDatabase db = mDB.getWritableDatabase();

                // 3. store to DB
                db.execSQL(DB.CloudPhoto.SQL_DROP);
                db.execSQL(DB.CloudPhoto.SQL_CREATE);
                for (Photo p : photos) {
                    ContentValues values = new ContentValues();
                    values.put(DB.CloudPhoto.Entry.COLUMN_URL, p.url);
                    values.put(DB.CloudPhoto.Entry.COLUMN_TITLE, p.title);
                    db.insert(DB.CloudPhoto.Entry.TABLE_NAME, null, values);
                }

                // 4. notify
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(ACTION_CLOUD));
            }
        });
    }

    private static class Photo {
        public String url;
        public String title;
    }
}

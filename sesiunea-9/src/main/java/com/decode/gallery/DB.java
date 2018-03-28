package com.decode.gallery;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by lucian.cioroga on 3/21/2018.
 */

public class DB {
    public final static class Visit {
        public final static class Entry implements BaseColumns {
            public final static String TABLE_NAME = "visits";
            public final static String COLUMN_URL = "url";
            public final static String COLUMN_VISITS = "visits";
        }

        private static final String SQL_CREATE =
                "CREATE TABLE " + Entry.TABLE_NAME + " (" +
                        Entry._ID + " INTEGER PRIMARY KEY," +
                        Entry.COLUMN_URL + " TEXT," +
                        Entry.COLUMN_VISITS + " INTEGER)";

        private static final String SQL_DROP =
                "DROP TABLE IF EXISTS " + Entry.TABLE_NAME;
    }

    public final static class CloudPhoto {
        public final static class Entry implements BaseColumns {
            public final static String TABLE_NAME = "cloud_photos";
            public final static String COLUMN_URL = "url";
            public final static String COLUMN_TITLE = "title";
        }

        public static final String SQL_CREATE =
                "CREATE TABLE " + Entry.TABLE_NAME + " (" +
                        Entry._ID + " INTEGER PRIMARY KEY," +
                        Entry.COLUMN_URL + " TEXT," +
                        Entry.COLUMN_TITLE + " TEXT)";

        public static final String SQL_DROP =
                "DROP TABLE IF EXISTS " + Entry.TABLE_NAME;
    }

    public static class Helper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "gallery.db";

        public Helper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Visit.SQL_CREATE);
            db.execSQL(CloudPhoto.SQL_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(Visit.SQL_DROP);
            db.execSQL(CloudPhoto.SQL_DROP);
            onCreate(db);
        }
    }
}

package com.decode.gallery;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.provider.UserDictionary;
import android.support.v4.content.CursorLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lucian.cioroga on 3/7/2018.
 */

public class Media implements Parcelable {
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;
    public static final int TYPE_CLOUD = 3;

    private String mName;
    private int mType;
    private String mUrl;
    private long mDuration;

    public Media(int type, String name, String url, long duration) {
        mType = type;
        mName = name;
        mUrl = url;
        mDuration = duration;
    }

    protected Media(Parcel in) {
        mName = in.readString();
        mType = in.readInt();
        mUrl = in.readString();
        mDuration = in.readLong();
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    public String getName() {
        return mName;
    }

    public int getType() {
        return mType;
    }

    public String getUrl() {
        return mUrl;
    }

    public long getDuration() {
        return mDuration;
    }

    public static List<Media> getMedia(Context context, int type) {
        Cursor cursor;

        // Get relevant columns for use later.
        String[] projection = {
                MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE, MediaStore.Files.FileColumns.MIME_TYPE, MediaStore.Files.FileColumns.TITLE,
                MediaStore.Video.Media.DURATION};

        // Return only video and image metadata.
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + (type == TYPE_IMAGE ? MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE : MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
        Uri queryUri = MediaStore.Files.getContentUri("external");

        CursorLoader cursorLoader = new CursorLoader(context, queryUri, projection, selection, null, MediaStore.Files.FileColumns.DATE_ADDED + " DESC");
        cursor = cursorLoader.loadInBackground();

        List<Media> media = new ArrayList<>();
        cursor.moveToFirst();

        do {
            media.add(new Media(type, cursor.getString(5), cursor.getString(1), cursor.getLong(6)));
        } while (cursor.moveToNext());

        cursor.close();

        return media;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeInt(mType);
        parcel.writeString(mUrl);
        parcel.writeLong(mDuration);
    }
}

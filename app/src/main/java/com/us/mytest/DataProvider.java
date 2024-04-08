package com.us.mytest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * ClsFunction：
 * CreateDate：2024/4/8
 * Author：TimeWillRememberUs
 */
public class DataProvider extends ContentProvider {

    private final String TAG = "DataProvider";

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final String AUTHORITY = "com.us.mytest.DataProvider";
    public static final String SCHEME = "content";

    static {
        mUriMatcher.addURI(AUTHORITY, "user", 1);
    }

    public static Uri getUri(String path) {
        return new Uri.Builder().scheme(SCHEME).authority(AUTHORITY).path(path).build();
    }

    @Override
    public boolean onCreate() {
        Log.e(TAG, "onCreate");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e(TAG, "query");
        int match = mUriMatcher.match(uri);
        switch (match) {
            case 1:
                Log.e(TAG, "user表");
                return null;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

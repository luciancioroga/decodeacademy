package com.decode.gallery;

import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.decode.gallery.v8.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.fabric.sdk.android.Fabric;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.HashMap;

public class GalleryActivity extends AppCompatActivity implements GalleryFragment.IGallery, View.OnClickListener {
    public static final int REQUEST_PREVIEW = 1;
    public static final int REQUEST_CAMERA = 2;
    private static final Gallery[] GALLERIES = {
            Gallery.create("Photos", Media.TYPE_IMAGE, R.id.nav_photo),
            Gallery.create("Videos", Media.TYPE_VIDEO, R.id.nav_video),
    };
    private static final String PREFERENCES_VISITS = "pref-visits";

    private TabLayout mTabs;
    private ViewPager mPager;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private NavigationView mNavigation;
    private FloatingActionButton mBtnCamera;

    private HashMap<String, Integer> mVisits;
    private DB.Helper mDB;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        mDB = new DB.Helper(this);
        if (savedInstanceState != null)
            mVisits = ((HashMap<String, Integer>) savedInstanceState.getSerializable("visits"));
        else {
            mVisits = new HashMap<>();
            SQLiteDatabase db = mDB.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + DB.Visit.Entry.TABLE_NAME, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    mVisits.put(cursor.getString(cursor.getColumnIndex(DB.Visit.Entry.COLUMN_URL)), cursor.getInt(cursor.getColumnIndex(DB.Visit.Entry.COLUMN_VISITS)));
                } while (cursor.moveToNext());
            }
        }

        setContentView(R.layout.activity_gallery);
        setTitle("Gallery");

        mTabs = findViewById(R.id.tabs);
        mPager = findViewById(R.id.pager);
        mPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = new GalleryFragment();
                Bundle arguments = new Bundle();
                arguments.putInt("type", GALLERIES[position].type);
                fragment.setArguments(arguments);
                return fragment;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return GALLERIES[position].label;
            }
        });
        mTabs.setupWithViewPager(mPager);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawer = findViewById(R.id.drawer_layout);
        mNavigation = findViewById(R.id.drawer_navigation);
        mNavigation.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        mDrawer.closeDrawers();

                        onOptionsItemSelected(menuItem);
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        return true;
                    }
                });

        mBtnCamera = findViewById(R.id.btn_camera);
        mBtnCamera.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("visits", mVisits);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == GalleryFragment.PERMISSION_REQUEST_STORAGE) {
            Permissions.reset();
            for (Fragment f : getSupportFragmentManager().getFragments())
                f.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            mDrawer.openDrawer(GravityCompat.START);
        for (int i = 0; i < GALLERIES.length; i++)
            if (GALLERIES[i].action == item.getItemId()) {
                mPager.setCurrentItem(i);
                break;
            }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int result, Intent data) {
        super.onActivityResult(requestCode, result, data);
        if (requestCode == REQUEST_CAMERA && result == RESULT_OK)
            Toast.makeText(this, "Camera done!", Toast.LENGTH_SHORT).show();
        else if (requestCode == Permissions.REQUEST_PERMISSION_SETTING) {
            for (Fragment f : getSupportFragmentManager().getFragments())
                f.onActivityResult(requestCode, result, data);
        } else if (requestCode == REQUEST_PREVIEW && result == RESULT_OK) {
            Media media = data.getParcelableExtra("media");
            int v = mVisits.containsKey(media.getUrl()) ? mVisits.get(media.getUrl()) : 0;
            mVisits.put(media.getUrl(), v + 1);
            for (Fragment f : getSupportFragmentManager().getFragments())
                f.onActivityResult(requestCode, result, data);
        }
    }

    @Override
    public void preview(final View sharedElement, final Media media) {
        Intent intent = new Intent(GalleryActivity.this, PreviewActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(GalleryActivity.this, sharedElement, "thumbnail");
        intent.putExtra("media", media);
        startActivityForResult(intent, REQUEST_PREVIEW, options.toBundle());
    }

    @Override
    public View getRoot() {
        return mDrawer;
    }

    @Override
    public int getVisits(Media media) {
        return mVisits.containsKey(media.getUrl()) ? mVisits.get(media.getUrl()) : 0;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_camera) {
            Intent camera = new Intent(mPager.getCurrentItem() == 0 ? MediaStore.ACTION_IMAGE_CAPTURE : MediaStore.ACTION_VIDEO_CAPTURE);
            if (camera.resolveActivity(getPackageManager()) != null)
                startActivityForResult(camera, REQUEST_CAMERA);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Permissions.reset();
    }

    @Override
    protected void onPause() {
        super.onPause();

//        SharedPreferences prefs = getSharedPreferences(PREFERENCES_VISITS, Context.MODE_PRIVATE);
//        prefs.edit().putString("visits", mGson.toJson(mVisits)).commit();
//        Log.d("Preferences", "wrote " + prefs.getString("visits", ""));

//        try {
//            File file = new File(getDir("data", MODE_PRIVATE), "map");
//            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
//            outputStream.writeObject(mVisits);
//            outputStream.flush();
//            outputStream.close();
//        } catch (Exception e) {
//        }

        SQLiteDatabase db = mDB.getWritableDatabase();
        for (String key : mVisits.keySet()) {
            ContentValues values = new ContentValues();
            values.put(DB.Visit.Entry.COLUMN_URL, key);
            values.put(DB.Visit.Entry.COLUMN_VISITS, mVisits.get(key));

            if (db.update(DB.Visit.Entry.TABLE_NAME, values, DB.Visit.Entry.COLUMN_URL + "= ?", new String[]{key}) <= 0)
                db.insert(DB.Visit.Entry.TABLE_NAME, null, values);
        }
    }

    static class Gallery {
        String label;
        int type;
        int action;

        static Gallery create(String l, int t, int a) {
            Gallery g = new Gallery();
            g.label = l;
            g.type = t;
            g.action = a;
            return g;
        }
    }
}
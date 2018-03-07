package com.decode.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.decode.gallery.com.R;

/**
 * Created by lucian.cioroga on 1/9/2018.
 */

public class PreviewActivity extends AppCompatActivity {
    private View mPreview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        setTitle("Preview");

        mPreview = findViewById(R.id.preview);
        int color = getIntent().getIntExtra("color", 0);

        Log.d("Color", "" + color);
        mPreview.setBackgroundColor(color);
    }
}

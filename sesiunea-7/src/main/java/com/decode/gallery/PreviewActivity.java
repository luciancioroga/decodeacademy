package com.decode.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;

import com.decode.gallery.com.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by lucian.cioroga on 1/9/2018.
 */

public class PreviewActivity extends AppCompatActivity {
    private ImageView mThumb;
    private Picasso mThumbs;
    private Media mMedia;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);

        mThumbs = new Picasso.Builder(this).addRequestHandler(new VideoRequestHandler()).build();

        setContentView(R.layout.activity_preview);
        setTitle("Preview");

        mThumb = findViewById(R.id.thumb);
        supportPostponeEnterTransition();

        mMedia = getIntent().getParcelableExtra("media");
        mThumbs.load((mMedia.getType() == Media.TYPE_IMAGE ? "file://" : "video:") + mMedia.getUrl()).fit().centerInside().into(mThumb, new Callback() {
            @Override
            public void onSuccess() {
                scheduleStartPostponedTransition(mThumb);
            }

            @Override
            public void onError() {
            }
        });
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }

    @Override
    public void finish() {
        Intent result = new Intent();
        result.putExtra("media", mMedia);
        setResult(RESULT_OK, result);
        super.finish();
    }
}

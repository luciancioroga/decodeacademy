package gallery.decode.com.gallery;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int PREVIEW_REQUEST_TYPE = 1;

    private Button mPreview;
    private TextView mResult;
    private int mResultValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        setTitle("Gallery");

        mPreview = findViewById(R.id.preview);
        mPreview.setOnClickListener(this);
        mResult = findViewById(R.id.result);

        if (savedInstanceState != null) {
            mResultValue = savedInstanceState.getInt("result", 0);
            refresh();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.preview) {
            Intent intent = new Intent(this, PreviewActivity.class);
            startActivityForResult(intent, PREVIEW_REQUEST_TYPE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PREVIEW_REQUEST_TYPE) {
            mResultValue = resultCode;
            refresh();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("result", mResultValue);
    }

    private void refresh() {
        if (mResult != null)
            mResult.setText("Result " + (mResultValue > 0 ? mResultValue : ""));
    }
}

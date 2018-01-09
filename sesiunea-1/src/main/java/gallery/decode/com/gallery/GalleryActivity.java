package gallery.decode.com.gallery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_TYPE = 1;

    private Button mPreview;
    private TextView mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mPreview = findViewById(R.id.preview);
        mPreview.setOnClickListener(this);
        mResult = findViewById(R.id.result);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.preview) {
            Intent intent = new Intent(this, PreviewActivity.class);
            startActivityForResult(intent, REQUEST_TYPE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TYPE && mResult != null)
            mResult.setText("Result " + resultCode);
    }
}

package gallery.decode.com.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by lucian.cioroga on 1/9/2018.
 */

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBack1, mBack2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        mBack1 = findViewById(R.id.back_1);
        mBack2 = findViewById(R.id.back_2);
        mBack1.setOnClickListener(this);
        mBack2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_1) {
            setResult(1);
            finish();
        } else if (v.getId() == R.id.back_2) {
            setResult(2);
            finish();
        }
    }
}

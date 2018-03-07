package gallery.decode.com.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by lucian.cioroga on 1/9/2018.
 */

public class GalleryFragment extends Fragment implements View.OnClickListener {
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    private int mType = 0;

    public GalleryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        mType = getArguments() != null ? getArguments().getInt("type", TYPE_IMAGE) : TYPE_IMAGE;
        return root;
    }

    @Override
    public void onClick(View view) {
    }

    public interface ICallback {
        void preview(int type);
    }
}

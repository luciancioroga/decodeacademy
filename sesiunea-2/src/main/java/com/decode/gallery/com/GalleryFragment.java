package com.decode.gallery.com;

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
    private Button mPreview;
    private int mType = 0;

    public GalleryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        mPreview = root.findViewById(R.id.preview);
        mPreview.setOnClickListener(this);

        mType = getArguments() != null ? getArguments().getInt("type", 0) : 0;
        mPreview.setText("Preview " + mType);
        return root;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.preview)
            if (getActivity() instanceof ICallback && !getActivity().isDestroyed() && !getActivity().isFinishing())
                ((ICallback) getActivity()).preview(mType);

    }

    public interface ICallback {
        void preview(int type);
    }
}

package com.decode.gallery;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.decode.gallery.com.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lucian.cioroga on 1/9/2018.
 */

public class GalleryFragment extends Fragment implements View.OnClickListener, Permissions.Callback {
    public final static int PERMISSION_REQUEST_STORAGE = 1;
    private int mType = 0;
    private RecyclerView mRecycler;

    public GalleryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        mType = getArguments() != null ? getArguments().getInt("type", Media.TYPE_IMAGE) : Media.TYPE_IMAGE;
        mRecycler = root.findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.gallery_column_count)));
        load();
        return root;
    }

    private void load() {
        Permissions.check(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_REQUEST_STORAGE, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Permissions.REQUEST_PERMISSION_SETTING)
            load();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_STORAGE)
            Permissions.onPermissionsRequestResult(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE, permissions, grantResults, this);
    }

    @Override
    public void onPermissionNeedMoreInfo(String permission) {
        Snackbar.make(((IGallery) getActivity()).getRoot(), "Gallery requires access to your storage", Snackbar.LENGTH_INDEFINITE).setAction("GRANT", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions.request(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_REQUEST_STORAGE);
            }
        }).show();
    }

    @Override
    public void onPermissionNeverAskAgain(String permission) {
        Snackbar.make(((IGallery) getActivity()).getRoot(), "Gallery requires access to your storage", Snackbar.LENGTH_INDEFINITE).setAction("GRANT", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions.settings(getActivity());
            }
        }).show();
    }

    @Override
    public void onPermissionAllowed(String permission) {
        mRecycler.setAdapter(new Adapter(mType));
    }

    @Override
    public void onPermissionDenied(String permission) {
        load();
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof Media) {
            if (getActivity() instanceof IGallery && !getActivity().isFinishing() && !getActivity().isDestroyed())
                ((IGallery) getActivity()).preview(view.findViewById(R.id.thumb), (Media) view.getTag());
        }
    }

    class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Media> mMedia;
        private Picasso mThumbs;

        private Adapter(int type) {
            mMedia = Media.getMedia(getContext(), type);
            mThumbs = new Picasso.Builder(getContext()).addRequestHandler(new VideoRequestHandler()).build();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mName.setText(U.format(mMedia.get(position).getDuration()));
            holder.itemView.setTag(mMedia.get(position));
            holder.itemView.setOnClickListener(GalleryFragment.this);

            mThumbs.load((mType == Media.TYPE_IMAGE ? "file://" : "video:") + mMedia.get(position).getUrl()).fit().centerInside().into(holder.mThumb);
        }

        @Override
        public int getItemCount() {
            return mMedia.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private ImageView mThumb;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.label);
            mThumb = itemView.findViewById(R.id.thumb);
        }
    }

    public interface IGallery {
        void preview(View sharedElement, Media media);

        View getRoot();
    }
}

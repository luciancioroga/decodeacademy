package com.decode.gallery;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
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

public class GalleryFragment extends Fragment implements View.OnClickListener {
    private int mType = 0;
    private RecyclerView mRecycler;
    private Cursor mMediaCursor;

    public GalleryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        mType = getArguments() != null ? getArguments().getInt("type", Media.TYPE_IMAGE) : Media.TYPE_IMAGE;
        mRecycler = root.findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.gallery_column_count)));
        mRecycler.setAdapter(new Adapter(mType));
        return root;
    }

    private void loadMediaCursor() {
        if (mMediaCursor != null && !mMediaCursor.isClosed())
            mMediaCursor.close();

        // Get relevant columns for use later.
        String[] projection = {
                MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE, MediaStore.Files.FileColumns.MIME_TYPE, MediaStore.Files.FileColumns.TITLE,
                MediaStore.Video.Media.DURATION};

        // Return only video and image metadata.
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + (mType == Media.TYPE_IMAGE ? MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE : MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
        Uri queryUri = MediaStore.Files.getContentUri("external");

        CursorLoader cursorLoader = new CursorLoader(getContext(), queryUri, projection, selection, null, MediaStore.Files.FileColumns.DATE_ADDED + " DESC");
        mMediaCursor = cursorLoader.loadInBackground();
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof Media) {
            if (getActivity() instanceof ICallback && !getActivity().isFinishing() && !getActivity().isDestroyed())
                ((ICallback) getActivity()).preview((Media) view.getTag());
        }
    }

    public interface ICallback {
        void preview(Media media);
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
}

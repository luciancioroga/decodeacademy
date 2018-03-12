package com.decode.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.decode.gallery.com.R;

/**
 * Created by lucian.cioroga on 1/9/2018.
 */

public class GalleryFragment extends Fragment implements View.OnClickListener {
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
        mRecycler.setAdapter(new Adapter(mType));
        return root;
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
        private Media[] mMedia;

        private Adapter(int type) {
            mMedia = Media.getMedia(type);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mName.setText(mMedia[position].getName());
            holder.mContainer.setBackgroundColor(mMedia[position].getColor());
            holder.itemView.setTag(mMedia[position]);
            holder.itemView.setOnClickListener(GalleryFragment.this);
        }

        @Override
        public int getItemCount() {
            return mMedia.length;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private View mContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.label);
            mContainer = itemView.findViewById(R.id.container);
        }
    }
}

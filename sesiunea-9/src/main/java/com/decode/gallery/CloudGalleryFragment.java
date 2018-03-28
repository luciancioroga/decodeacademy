package com.decode.gallery;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.decode.gallery.v8.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucian.cioroga on 1/9/2018.
 */

public class CloudGalleryFragment extends Fragment implements View.OnClickListener {
    private int mType = 0;
    private RecyclerView mRecycler;
    private Adapter mAdapter;

    private BroadcastReceiver mReceiver;
    private DB.Helper mDB;

    public CloudGalleryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        mType = getArguments() != null ? getArguments().getInt("type", Media.TYPE_IMAGE) : Media.TYPE_IMAGE;
        mRecycler = root.findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.gallery_column_count)));

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i) {
                load();
            }
        };

        mDB = new DB.Helper(getContext());

        load();
        return root;
    }

    private void load() {
        SQLiteDatabase db = mDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DB.CloudPhoto.Entry.TABLE_NAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            List<Media> media = new ArrayList<>();
            do {
                Media m = new Media(Media.TYPE_IMAGE, cursor.getString(cursor.getColumnIndex(DB.CloudPhoto.Entry.COLUMN_TITLE)), cursor.getString(cursor.getColumnIndex(DB.CloudPhoto.Entry.COLUMN_URL)), 0);
                media.add(m);
            } while (cursor.moveToNext());

            mRecycler.setAdapter(new Adapter(media));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Cloud.ACTION_CLOUD);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GalleryActivity.REQUEST_PREVIEW && resultCode == Activity.RESULT_OK)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof Media)
            root().preview(view.findViewById(R.id.thumb), (Media) view.getTag());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Media> mMedia;
        private Picasso mThumbs;

        private Adapter(List<Media> media) {
            mMedia = media;
            mThumbs = new Picasso.Builder(getContext()).addRequestHandler(new VideoRequestHandler()).build();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Media media = mMedia.get(position);

            holder.mName.setText(U.format(media.getDuration()));
            holder.itemView.setTag(media);
            holder.itemView.setOnClickListener(CloudGalleryFragment.this);

            mThumbs.load(media.getUrl() + "?w=140").fit().centerInside().into(holder.mThumb);

            holder.mVisits.setVisibility(root().getVisits(media) > 0 ? View.VISIBLE : View.GONE);
            holder.mVisits.setText("" + root().getVisits(media));
        }

        @Override
        public int getItemCount() {
            return mMedia.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private ImageView mThumb;
        private TextView mVisits;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.label);
            mThumb = itemView.findViewById(R.id.thumb);
            mVisits = itemView.findViewById(R.id.visits);
        }
    }

    public interface IGallery {
        void preview(View sharedElement, Media media);

        View getRoot();

        int getVisits(Media media);
    }

    private IGallery root() {
        if (getActivity() instanceof IGallery && !getActivity().isFinishing() && !getActivity().isDestroyed())
            return (IGallery) getActivity();
        else
            return new IGallery() {
                @Override
                public void preview(View sharedElement, Media media) {
                }

                @Override
                public View getRoot() {
                    return null;
                }

                @Override
                public int getVisits(Media media) {
                    return 0;
                }
            };
    }
}

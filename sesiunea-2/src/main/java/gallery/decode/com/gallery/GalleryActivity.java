package gallery.decode.com.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class GalleryActivity extends AppCompatActivity implements GalleryFragment.ICallback {
    public static final int REQUEST_TYPE = 1;

    private TabLayout mTabs;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        setTitle("Gallery");

        mTabs = findViewById(R.id.tabs);
        mPager = findViewById(R.id.pager);
        mPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = new GalleryFragment();
                Bundle arguments = new Bundle();
                arguments.putInt("type", position);
                fragment.setArguments(arguments);
                return fragment;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "Page " + position;
            }
        });
        mTabs.setupWithViewPager(mPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int result, Intent data) {
        super.onActivityResult(requestCode, result, data);
        if (requestCode == REQUEST_TYPE && mPager != null)
            mPager.setCurrentItem(result);
    }

    @Override
    public void preview(int type) {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra("type", type);
        startActivityForResult(intent, REQUEST_TYPE);
    }
}

package gallery.decode.com.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class GalleryActivity extends AppCompatActivity implements GalleryFragment.ICallback, View.OnClickListener {
    public static final int REQUEST_PREVIEW = 1;
    public static final int REQUEST_CAMERA = 2;

    private static final String[] TITLES = {"Photos", "Videos"};

    private TabLayout mTabs;
    private ViewPager mPager;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private NavigationView mNavigation;
    private FloatingActionButton mBtnCamera;

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
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return TITLES[position];
            }
        });
        mTabs.setupWithViewPager(mPager);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawer = findViewById(R.id.drawer_layout);
        mNavigation = findViewById(R.id.drawer_navigation);
        mNavigation.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        mDrawer.closeDrawers();
                        onOptionsItemSelected(menuItem);
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        return true;
                    }
                });

        mBtnCamera = findViewById(R.id.btn_camera);
        mBtnCamera.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            mDrawer.openDrawer(GravityCompat.START);
        else if (item.getItemId() == R.id.nav_photo)
            mPager.setCurrentItem(0);
        else if (item.getItemId() == R.id.nav_video)
            mPager.setCurrentItem(1);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int result, Intent data) {
        super.onActivityResult(requestCode, result, data);
        if (requestCode == REQUEST_PREVIEW && mPager != null)
            mPager.setCurrentItem(result);
        else if (requestCode == REQUEST_CAMERA && result == RESULT_OK)
            Toast.makeText(this, "Camera done!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void preview(int type) {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra("type", type);
        startActivityForResult(intent, REQUEST_PREVIEW);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_camera) {
            Intent camera = new Intent(mPager.getCurrentItem() == 0 ? MediaStore.ACTION_IMAGE_CAPTURE : MediaStore.ACTION_VIDEO_CAPTURE);
            if (camera.resolveActivity(getPackageManager()) != null)
                startActivityForResult(camera, REQUEST_CAMERA);
        }
    }
}
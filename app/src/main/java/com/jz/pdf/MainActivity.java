package com.jz.pdf;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jz.pdf.activity.HomeUrlActivity;
import com.jz.pdf.fragment.OffLineFragment;
import com.jz.pdf.fragment.PdfListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.left_menu_container)
    FrameLayout leftMenuContainer;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar mToolBar;
    private Intent intent;
    private DrawerLayout mDrawerLayout;
    private FragmentManager fm;
    private PdfListFragment pdfListFragment;
    private OffLineFragment offLineFragment;
    private Fragment currentFragment = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initAppBar();
        fm = getFragmentManager();
        if (currentFragment != null) {
            pdfListFragment = (PdfListFragment) fm.findFragmentByTag(PdfListFragment.class.getName());
            offLineFragment = (OffLineFragment) fm.findFragmentByTag(OffLineFragment.class.getName());
            if (currentFragment == pdfListFragment) {
                fm.beginTransaction().show(pdfListFragment).hide(offLineFragment).commit();
            } else if (currentFragment == offLineFragment) {
                fm.beginTransaction().show(offLineFragment).hide(pdfListFragment).commit();
            }
        } else {
            pdfListFragment = new PdfListFragment();
            fm.beginTransaction().add(R.id.fl_container, pdfListFragment, PdfListFragment.class.getName())
                    .commit();
            currentFragment = pdfListFragment;
        }

        /*AmazonS3Client s3Client= S3InitManager.getS3Client();
        transferUtility= S3InitManager.getTransferUtility(this);
        observer = transferUtility.upload(Constants.BUCKET_NAME, photoSaveName, file);*/

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void initAppBar() {
        mToolBar = (Toolbar) findViewById(R.id.toolBar);
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        tvTitle.setText(getResources().getString(R.string.title_main));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawlayout);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar,
                0, 0);
        drawerToggle.syncState();
        mDrawerLayout.addDrawerListener(drawerToggle);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_homeurl:
                intent = new Intent(MainActivity.this, HomeUrlActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick({R.id.left_menu_home, R.id.left_menu_offline, R.id.left_menu_exit})
    public void onClick(View view) {
        mDrawerLayout.closeDrawer(leftMenuContainer);
        switch (view.getId()) {
            case R.id.left_menu_home:
                tvTitle.setText(getResources().getString(R.string.title_main));
                if (currentFragment == pdfListFragment) {
                    break;
                } else if (currentFragment == offLineFragment) {
                    switchFragment(offLineFragment, pdfListFragment);
                }
                currentFragment = pdfListFragment;
                break;
            case R.id.left_menu_offline:
                tvTitle.setText(getResources().getString(R.string.title_offline));
                if (offLineFragment == null) {
                    offLineFragment = new OffLineFragment();
                }
                switchFragment(pdfListFragment, offLineFragment);
                currentFragment = offLineFragment;
                break;
            /*case R.id.left_menu_collection:
                tvTitle.setText(getResources().getString(R.string.title_collection));
                if (collectionFragment == null) {
                    collectionFragment = new CollectionFragment();
                }
                fm.beginTransaction().replace(R.id.fl_container, collectionFragment).commit();
                break;*/
            case R.id.left_menu_exit:
                finish();
                break;
        }
    }

    /**
     * 切换页面的重载，优化了fragment的切换
     *
     * @param from
     * @param to
     */
    public void switchFragment(Fragment from, Fragment to) {
        if (from == null || to == null)
            return;
        android.app.FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        if (!to.isAdded()) {
            // 隐藏当前的fragment，add下一个到Activity中
            transaction.hide(from).add(R.id.fl_container, to, to.getClass().getName()).commit();
        } else {
            // 隐藏当前的fragment，显示下一个
            transaction.hide(from).show(to).commit();
        }
    }
}

package com.example.classroom.activitys;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.classroom.R;
import com.example.classroom.app.App;
import com.example.classroom.databinding.ActivityPrimaryBinding;
import com.example.classroom.fragments.GankFragment;
import com.example.classroom.fragments.GirlFragment;
import com.example.classroom.fragments.HomeFragment;
import com.example.classroom.fragments.MeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PrimaryActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "PrimaryActivity";

    private Fragment[] mFragments = new Fragment[4];
    private Fragment mCurFragment = new Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPrimaryBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_primary);
        mBinding.activityPrimaryBnv.setOnNavigationItemSelectedListener(this);
        initViews();

    }

    private void initViews() {
        mFragments[0] = HomeFragment.newInstance("home");
        mFragments[1] = GirlFragment.newInstance("福利");
        mFragments[2] = GankFragment.newInstance("干货");
        mFragments[3] = MeFragment.newInstance("我的");
        switchFragment(mFragments[0]);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.main_bnav_home:
                switchFragment(mFragments[0]);
                break;
            case R.id.main_bnav_goods:
                switchFragment(mFragments[1]);
                break;

            case R.id.main_bnav_books:
                switchFragment(mFragments[2]);
                break;

            case R.id.main_bnav_me:
                switchFragment(mFragments[3]);
                break;
        }
        return true;
    }

    /**
     * 切换Fragment
     * @param targetFragment 目标Fragment
     */
    private void switchFragment(Fragment targetFragment){
    /*    if (mCurFragment == targetFragment) {
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.activity_primary_fl, targetFragment).commit();
        mCurFragment = targetFragment;*/

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(mCurFragment);
        if (!targetFragment.isAdded())
            transaction.add(R.id.activity_primary_fl, targetFragment, targetFragment.getClass().getName());
        else
            transaction.show(targetFragment);
        transaction.commitAllowingStateLoss();
        // 更新当前Fragment为targetFragment;
        mCurFragment = targetFragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.me.finishAll();
    }
}
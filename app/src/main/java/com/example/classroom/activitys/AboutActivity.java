package com.example.classroom.activitys;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.classroom.R;
import com.example.classroom.databinding.ActivityAboutBinding;

import androidx.databinding.DataBindingUtil;

public class AboutActivity extends BaseActivity {
    private static final String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAboutBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_about);
        binding.activityAboutToolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.activityAboutLink.setOnClickListener(this::onClick);

        binding.setVersion(getLocalVersionName());

    }

    /** 获取当前版本号 */
    private String getLocalVersionName(){
        try {
            PackageManager pm = getPackageManager();
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return getString(R.string.version_v) + info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return getString(R.string.version_v) + "1.0";
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_about_link:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/androidyanghuan"));
                startActivity(intent);
                break;
        }
    }
}
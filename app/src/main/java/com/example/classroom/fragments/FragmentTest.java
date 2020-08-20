package com.example.classroom.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.classroom.R;
import com.example.classroom.viewModels.SeekBarViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/10
 * Description
 */
public class FragmentTest extends Fragment {
    private static final String TAG = "FragmentTest";

    private SeekBar mSeekBar;
    private SeekBarViewModel mSeekBarViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_fragment_layout, container, false);

        mSeekBar = view.findViewById(R.id.seekBar);

        mSeekBarViewModel = new ViewModelProvider(requireActivity()).get(SeekBarViewModel.class);

        subscribeSeekBar();
        return view;
    }

    private void subscribeSeekBar() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    Log.e(TAG, "Progress changed!" + i);
                    mSeekBarViewModel.getSeekValues().setValue(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // 当视图模型发生更改时，更新SeekBar
        mSeekBarViewModel.getSeekValues().observe(requireActivity(), integer -> {
            if (integer != null) {
                mSeekBar.setProgress(integer);
            }
        });
    }


}

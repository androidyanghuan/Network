package com.example.classroom.viewModels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/10
 * Description 数据保存ViewModel
 */

public class SavedStateViewModel extends ViewModel {

    private static final String NAME_KEY = "name";

    private SavedStateHandle mState;

    public SavedStateViewModel(SavedStateHandle state) {
        this.mState = state;
    }

    public LiveData<String> getName() {
        if (!mState.contains(NAME_KEY)) {
            mState.set(NAME_KEY,"none"); // 设置默认值
        }
        return mState.getLiveData(NAME_KEY);
    }

    public void saveNewName(String newName) {
        mState.set(NAME_KEY, newName);
    }

}

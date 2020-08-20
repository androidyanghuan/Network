package com.example.classroom.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import lombok.Data;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/10
 * Description
 */
@Data
public class SeekBarViewModel extends ViewModel {

    private MutableLiveData<Integer> seekValues = new MutableLiveData<>();

}

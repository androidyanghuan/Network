package com.example.classroom.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.classroom.R;
import com.example.classroom.viewModels.SavedStateViewModel;

public class SaveDataActivity extends AppCompatActivity {

    private SavedStateViewModel mSavedStateViewModel;

    private TextView mTextView;
    private EditText mInputEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_data);

        mTextView = findViewById(R.id.saved_vm_tv);
        mInputEdt = findViewById(R.id.name_et);

        mSavedStateViewModel = new ViewModelProvider(this).get(SavedStateViewModel.class);

        mSavedStateViewModel.getName().observe(this, s -> mTextView.setText(getString(R.string.saved_in_vm, s)));

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_bt:
                mSavedStateViewModel.saveNewName(mInputEdt.getText().toString().trim());
                break;

        }
    }
}
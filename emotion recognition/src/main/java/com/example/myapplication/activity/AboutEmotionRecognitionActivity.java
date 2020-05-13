package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.base.BaseActivity;

/**
 * @author 鑫宇
 * @auther: sly
 * @date: 2020/4/1
 * @description: com.example.myapplication.fragment
 * @version: 1.0
 */
public class AboutEmotionRecognitionActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_emotion_recognition_layout);
    }
}

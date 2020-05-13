package com.example.myapplication.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.base.BaseActivity;

/**
 * @auther: sly
 * @date: 2020/4/1
 * @description: com.example.myapplication.fragment
 * @version: 1.0
 */
public class AboutUsActivity extends BaseActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.about_us_layout);
  }



}

package com.example.myapplication.activity;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.fragment.TcpClientFragment;

/**
 * @author 鑫宇
 * @date: 2019/7/10
 * @description: com.example.myapplication
 * @version: 1.0
 */
public class MainActivity extends BaseActivity {
    private AboutUsActivity mAboutUsFragment;

    private static final String TAG = "TcpClientActivity";
    public static Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        mContext = this;
        initView();
        initFragment();
        initListener();
        selectFragment(new TcpClientFragment());
    }

    public void selectFragment(Fragment targetFragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_container,targetFragment,targetFragment.getTag());
        fragmentTransaction.commit();
    }





    /**
     * 关闭程序
     */
    public void exit() {
        super.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private void initListener() {
    }

    private void initFragment() {

    }

    private void initView() {
        FrameLayout mainContainer = findViewById(R.id.main_container);
    }

    @Override
    public void onBackPressed() {
      showExitAlert();
    }



    private void showExitAlert() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // 有白色背景，加这句代码
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setContentView(R.layout.dialog_close_program);
        TextView tv = (TextView) window.findViewById(R.id.tv_no);
        LinearLayout ok = (LinearLayout) window.findViewById(R.id.tv_ok);

        //确定按钮
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit(); // 退出应用
            }
        });

        //取消按钮
        LinearLayout cancel = (LinearLayout) window.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }
}



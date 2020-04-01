package com.example.myapplication;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.fragment.AboutEmotionRecognitionFragment;
import com.example.myapplication.fragment.AboutUsFragment;
import com.example.myapplication.fragment.TcpClientFragment;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @auther: shiliye
 * @date: 2019/7/10
 * @description: com.example.myapplication
 * @version: 1.0
 */
public class MainActivity extends AppCompatActivity {
    private AboutUsFragment mAboutUsFragment;
    private AboutEmotionRecognitionFragment mAboutEmotionRecognition;
    private FrameLayout mMainContainer;

    private static final String TAG = "TcpClientActivity";
    public static Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        mContext = this;
        hide();
        initView();
        initFragment();
        initListener();
        selectFragment(new TcpClientFragment());



    }

    public void selectFragment(Fragment targetFragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_container,targetFragment);
        fragmentTransaction.commit();
    }



    /**
     * 沉浸模式
     */
    public void hide() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
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
        mAboutUsFragment = new AboutUsFragment();
        mAboutEmotionRecognition = new AboutEmotionRecognitionFragment();
    }

    private void initView() {

        mMainContainer = findViewById(R.id.main_container);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitAlert();
        }
        return super.onKeyDown(keyCode, event);
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



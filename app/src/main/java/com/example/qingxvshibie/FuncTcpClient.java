package com.example.qingxvshibie;

/**
 * @Auther: shiliye
 * @Date: 2019/8/19
 * @version: 1.0
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;

import com.example.myapplication.R;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FuncTcpClient extends Activity {
    private String TAG = "FuncTcpClient";
    @SuppressLint("StaticFieldLeak")
    public static Context context ;
    private LinearLayout CleanClientRcv;
    private Button btnStartClient,btnCloseClient;
    private TextView txtRcv;
    private EditText editClientPort,editClientIp;
    private static TcpClient tcpClient = null;
    private MyBtnClicker myBtnClicker = new MyBtnClicker();
    private final MyHandler myHandler = new MyHandler(this);
    private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
    private TextView tips;
    private Button  camera;
    private ImageView iv1;
    private ImageView iv2;
    private Vibrator vibrator;
    ExecutorService exec = Executors.newCachedThreadPool();

    private class MyBtnClicker implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
//                连接按钮
                case R.id.btn_tcpClientConn:
                try{
                        Log.i(TAG, "onClick: 开始");
                        btnStartClient.setEnabled(false);
                        btnCloseClient.setEnabled(true);
//                    得到ip和端口号
                        tcpClient = new TcpClient(editClientIp.getText().toString(),getPort(editClientPort.getText().toString()));
                        exec.execute(tcpClient);
                        break;
                }catch (Exception ex){
                    if (editClientIp.getText().toString().equals("")&&editClientPort.getText().toString().equals("")){
                        orClick();
                        Toast.makeText(FuncTcpClient.this,"请输入ip和端口号",Toast.LENGTH_LONG).show();
                    }else if(editClientIp.getText().toString().equals("")){
                        orClick();
                        Toast.makeText(FuncTcpClient.this,"请输入ip",Toast.LENGTH_LONG).show();
                    }else {
                        orClick();
                        Toast.makeText(FuncTcpClient.this,"请输入正确的端口号",Toast.LENGTH_LONG).show();
                    }

                }
//                    断开连接按钮
                case R.id.btn_tcpClientClose:
                    if (tcpClient!=null){
                        tcpClient.closeSelf();
                    }
                    orClick();
                    break;
                case R.id.btn_camera:
                    Intent intent=new Intent(FuncTcpClient.this,CameraActivity.class);
                    startActivity(intent);
                    break;
                case R.id.lyclean:
                    txtRcv.setText("");
                    break;
            }
        }
    }

    private class MyHandler extends android.os.Handler{
        private WeakReference<FuncTcpClient> mActivity;
        MyHandler(FuncTcpClient activity){
            mActivity = new WeakReference<FuncTcpClient>(activity);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            if (mActivity != null){
                if ("angry".equals(msg.obj.toString().trim())){
                    tips.setText("危险");
                    iv2.setBackground(getDrawable(R.drawable.shape_circle));
                    setAnim1();
                    setAnim2();
                    vibrator = (Vibrator)FuncTcpClient.this.getSystemService(FuncTcpClient.this.VIBRATOR_SERVICE);
                    long[] patter = {100, 1000, 100, 1000};
                    vibrator.vibrate(patter, 1);
               }else if("happy".equals(msg.obj.toString().trim())){
                    tips.setText("开心");
                    iv1.clearAnimation();
                    iv2.clearAnimation();
                    vibrator.cancel();
                    iv2.setBackground(getDrawable(R.drawable.shape_circle_green));
                }else {
                    tips.setText("正常");
                    iv1.clearAnimation();
                    iv2.clearAnimation();
                    vibrator.cancel();
                    iv2.setBackground(getDrawable(R.drawable.shape_circle_green_blue));
                }
                switch (msg.what){
                    case 1:
//                        追加消息
                        txtRcv.append(msg.obj.toString()+"\t");
                        break;
                    case 2:
                        Toast.makeText(FuncTcpClient.context,"连接成功",Toast.LENGTH_LONG);
                }
            }
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            switch (mAction){
                case "tcpClientReceiver":
                    String msg = intent.getStringExtra("tcpClientReceiver");
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = msg;
                    myHandler.sendMessage(message);
                    break;
            }
        }
    }

    private int getPort(String port){
        return Integer.parseInt(port);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tcp_client);
        hide();
        context = this;
        bindID();
        bindListener();
        bindReceiver();
        cannotclick();
    }
    public void orClick(){
//        连接按钮
        btnStartClient.setEnabled(true);
//        断开按钮
        btnCloseClient.setEnabled(false);
    }
//    动画效果
    private void setAnim1() {
        AnimationSet as = new AnimationSet(true);
        //缩放动画，以中心从原始放大到1.4倍
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.4f, 1.0f, 1.4f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.5f);
        scaleAnimation.setDuration(800);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        as.setDuration(800);
        as.addAnimation(scaleAnimation);
        as.addAnimation(alphaAnimation);
        iv1.startAnimation(as);
    }
    private void setAnim2() {
        AnimationSet as = new AnimationSet(true);
        //缩放动画，以中心从1.4倍放大到1.8倍
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.4f, 1.8f, 1.4f, 1.8f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 0.1f);
        scaleAnimation.setDuration(800);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        as.setDuration(800);
        as.addAnimation(scaleAnimation);
        as.addAnimation(alphaAnimation);
        iv2.startAnimation(as);
    }
    private void hide(){
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);
//        ActionBar actionBar =();
//        actionBar.hide();
    }
    private void bindID(){
//        监控按钮
        camera=findViewById(R.id.btn_camera);
//        提示
        tips=findViewById(R.id.tv_tips);
//        连接
        btnStartClient = (Button) findViewById(R.id.btn_tcpClientConn);
//        断开连接
        btnCloseClient = (Button) findViewById(R.id.btn_tcpClientClose);
//        清除接收区
        CleanClientRcv =  findViewById(R.id.lyclean);
//        ip输入框
        editClientPort = (EditText) findViewById(R.id.edit_tcpClientPort);
//        端口号输入框
        editClientIp = (EditText) findViewById(R.id.edit_tcpClientIp);
//        接受的信息
        txtRcv = (TextView) findViewById(R.id.txt_ClientRcv);
        iv1=findViewById(R.id.iv_wave_1);
        iv2=findViewById(R.id.iv_wave_2);
        CleanClientRcv=findViewById(R.id.lyclean);

    }

    private void bindListener(){
        camera.setOnClickListener(myBtnClicker);
        btnStartClient.setOnClickListener(myBtnClicker);
        btnCloseClient.setOnClickListener(myBtnClicker);
        CleanClientRcv.setOnClickListener(myBtnClicker);

    }
    private void bindReceiver(){
        IntentFilter intentFilter = new IntentFilter("tcpClientReceiver");
        registerReceiver(myBroadcastReceiver,intentFilter);
    }
    private void cannotclick(){
        btnCloseClient.setEnabled(false);
    }
}


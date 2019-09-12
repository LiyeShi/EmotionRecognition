package com.example.myapplication;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
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
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: shiliye
 * @Date: 2019/7/10
 * @Description: com.example.myapplication
 * @version: 1.0
 */
public class FuncTcpClientActivity extends AppCompatActivity {
    private String TAG = "FuncTcpClientActivity";
    public static Context context ;
    private LinearLayout CleanClientRcv;
    private Button btnStartClient,btnCloseClient;
    private TextView txtRcv;
    private EditText editClientPort,editClientIp;
    private static TcpClient tcpClient = null;
    private MyBtnClicker myBtnClicker = new MyBtnClicker();
    public final MyHandler myHandler = new MyHandler(this);
    private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
    private TextView tips;
    private Button  camera;
    public  static ImageView iv1;
    public  static ImageView iv2;
    private Vibrator vibrator;
    private BoomMenuButton boomMenuButton;
    ExecutorService exec = Executors.newCachedThreadPool();


    private class MyBtnClicker implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
//                连接按钮
                case R.id.btn_tcpClientConn:
                    try{
                        Log.i(TAG, "onClick: 开始");
                        //                    得到ip和端口号
                        tcpClient = new TcpClient(editClientIp.getText().toString(),getPort(editClientPort.getText().toString()));
                        exec.execute(tcpClient);
                        btnStartClient.setEnabled(false);
                        btnCloseClient.setEnabled(true);
                        Log.i(TAG,"最后一行");
                    }catch (Exception ex){
                        Toast.makeText(FuncTcpClientActivity.this,"请输入正确的ip和端口号",Toast.LENGTH_LONG).show();
                    }finally {
                        break;
                    }
//                    断开连接按钮
                case R.id.btn_tcpClientClose:
                    if (tcpClient!=null){
                        tcpClient.closeSelf();
                    }
                    Toast.makeText(FuncTcpClientActivity.context, "连接断开", Toast.LENGTH_LONG).show();
                    orClick();
                    break;
                case R.id.btn_camera:
                    Intent intent=new Intent(FuncTcpClientActivity.this, SurveillanceActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
    private class MyHandler extends android.os.Handler{
        private WeakReference<FuncTcpClientActivity> mActivity;
        MyHandler(FuncTcpClientActivity activity){
            mActivity = new WeakReference<FuncTcpClientActivity>(activity);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            if (mActivity != null){
                Log.i(TAG,"进入判断");
                try{
                    if ("angry".equals(msg.obj.toString().trim())){
                        tips.setText("危险");
                        PlayVoice.playVoice(FuncTcpClientActivity.context);
                        iv2.setBackground(getDrawable(R.drawable.shape_circle_red));
                        Animation.setAnim1();
                        Animation.setAnim2();
                        vibrator = (Vibrator) FuncTcpClientActivity.this.getSystemService(FuncTcpClientActivity.this.VIBRATOR_SERVICE);
                        long[] patter = {100, 1000, 100, 1000};
                        vibrator.vibrate(patter, 1);
                    }else if("happy".equals(msg.obj.toString().trim())){
                        PlayVoice.stopVoice();
                        tips.setText("开心");
                        Log.i(TAG,"提示后，动画前");
                        iv1.clearAnimation();
                        iv2.clearAnimation();
                        if (vibrator!=null){
                            vibrator.cancel();
                            PlayVoice.stopVoice();
                        }
                        iv2.setBackground(getDrawable(R.drawable.shape_circle_green));
                        PlayVoice.stopVoice();
                    }else {
                        tips.setText("正常");
                        PlayVoice.stopVoice();
                        iv1.clearAnimation();
                        iv2.clearAnimation();
                        if (vibrator!=null){
                            vibrator.cancel();
                            PlayVoice.stopVoice();
                        }
                        iv2.setBackground(getDrawable(R.drawable.shape_circle_blue));
                        PlayVoice.stopVoice();
                    }
                    switch (msg.what){
                        case 1:
//                        追加消息
                            txtRcv.append(msg.obj.toString()+"\t");
                            break;
                    }
                }catch (Exception e){
//                        e.printStackTrace();
                    if (tcpClient!=null){
                        e.printStackTrace();
                        Log.i("判断","出错");
                        tcpClient.closeSelf();
                    }
                    orClick();
                    Toast.makeText(FuncTcpClientActivity.context,"连接失败！",Toast.LENGTH_LONG).show();
                    Vibrator vibrator = (Vibrator) FuncTcpClientActivity.context.getSystemService(FuncTcpClientActivity.VIBRATOR_SERVICE);
                    long[] patter = {0, 100, 200, 100};
                    vibrator.vibrate(patter, -1);
                }
            }
        }
    }
    private class MyBroadcastReceiver extends BroadcastReceiver {
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
        btnCloseClient.setEnabled(false);
        bindListener();
        cleanRec();
        bindReceiver();

        final Intent intent1=new Intent(FuncTcpClientActivity.this, AboutUs.class);
        final Intent intent2=new Intent(FuncTcpClientActivity.this, AboutEmotionRecognition.class);

        for (int i = 0; i < boomMenuButton.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            if (index==0){
                                startActivity(intent1);
                            }else if (index==1){
                                startActivity(intent2);
                            }else if (index==2){
                                exit();
                            }
                        }

                    })
                    .normalImageRes(getImageResource())
                    .normalText(getext())
                    .rotateText(true)
                    .textHeight(80)
                    .textSize(20);
            boomMenuButton.addBuilder(builder);
        }

    }
    private static int index = 0;
    static String getext() {
        if (index >= text.length) index = 0;
        return text[index++];

    }
    private static String [] text = new String[]{"关于我们","什么是情绪识别","退出"};
    private static int imageResourceIndex = 0;

    static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex++];
    }

    private static int[] imageResources = new int[]{
            R.drawable.about_us,
            R.drawable.questionsign,
            R.drawable.exit,
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            showExitGameAlert();
        }
        return super.onKeyDown(keyCode, event);
    }
    private void showExitGameAlert() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  // 有白色背景，加这句代码
        window.setContentView(R.layout.close_program);
        TextView tv = (TextView) window.findViewById(R.id.tv_no);
        LinearLayout ok = (LinearLayout) window.findViewById(R.id.tv_ok);

        //确定按钮
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exit(); // 退出应用
            }
        });

        //取消按钮
        LinearLayout cancel = (LinearLayout) window.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }
    //关闭程序
    private void exit() {
        super.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
    public void orClick(){
//        连接按钮
        btnStartClient.setEnabled(true);
//        断开按钮
        btnCloseClient.setEnabled(false);
    }
    //    沉浸模式
    public  void hide(){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
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
        boomMenuButton = (BoomMenuButton) findViewById(R.id.bmb);


    }

    private void bindListener(){
        camera.setOnClickListener(myBtnClicker);
        btnStartClient.setOnClickListener(myBtnClicker);
        btnCloseClient.setOnClickListener(myBtnClicker);
    }
    private void cleanRec(){
        CleanClientRcv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                txtRcv.setText("");
                return true;
            }
        });
    }
    private void bindReceiver(){
        IntentFilter intentFilter = new IntentFilter("tcpClientReceiver");
        registerReceiver(myBroadcastReceiver,intentFilter);
    }

}

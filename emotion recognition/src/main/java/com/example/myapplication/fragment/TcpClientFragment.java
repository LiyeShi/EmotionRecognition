package com.example.myapplication.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.SurveillanceActivity;
import com.example.myapplication.TcpClient;
import com.example.myapplication.MainActivity;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * @author 鑫宇
 * @auther: sly
 * @date: 2020/4/1
 * @description: com.example.myapplication.fragment
 * @version: 1.0
 */
public class TcpClientFragment extends Fragment {
    private String TAG = "FuncTcpClientActivity";
    public static Context context;
    private LinearLayout CleanClientRcv;
    private Button btnStartClient, btnCloseClient;
    private TextView txtRcv;
    private EditText editClientPort, editClientIp;
    private static TcpClient tcpClient = null;
    private MyBtnClicker myBtnClicker = new MyBtnClicker();
    public final MyHandler myHandler = new MyHandler((MainActivity) getActivity());
    private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
    private TextView tips;
    private BoomMenuButton boomMenuButton;
    private Button camera;
    public static ImageView iv1;
    public static ImageView iv2;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;
    private static final String RECEIVER_MESSAGE_HAPPY = "happy";
    private static final String RECEIVER_MESSAGE_ANGRY = "angry";
    private static final String RECEIVER_MESSAGE_NORMAL = "normal";
    ExecutorService exec = Executors.newCachedThreadPool();
    private MainActivity mActivity;

    private static String[] text = new String[]{"关于我们", "什么是情绪识别", "退出"};
    private static int imageResourceIndex = 0;
    private static int index = 0;
    private static int[] imageResources = new int[]{
            R.drawable.about_us,
            R.drawable.questionsign,
            R.drawable.exit,};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tcp_client,container,false);
        initView(view);
        for (int i = 0; i < boomMenuButton.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            if (index == 0) {
                                Log.d(TAG, "onBoomButtonClick: 点击==>" + index);
                                mActivity = (MainActivity) getActivity();
                                mActivity.selectFragment(new AboutUsFragment());
                            } else if (index == 1) {
                                Log.d(TAG, "onBoomButtonClick: 点击==>" + index);
                                mActivity. selectFragment(new AboutEmotionRecognitionFragment());
                            } else if (index == 2) {
                              getActivity().finish();
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
        btnCloseClient.setEnabled(false);
        initListener();
        cleanRec();
        bindReceiver();
        return view;

}
    static String getext() {
        if (index >= text.length) {
            index = 0;
        }
        return text[index++];

    }

    static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) {
            imageResourceIndex = 0;
        }
        return imageResources[imageResourceIndex++];
    }
    private void initListener() {
        camera.setOnClickListener(myBtnClicker);
        btnStartClient.setOnClickListener(myBtnClicker);
        btnCloseClient.setOnClickListener(myBtnClicker);
    }
    public  Activity getBActivity(){
        return this.getActivity();
    }
    private void cleanRec() {
        CleanClientRcv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                txtRcv.setText("");
                return true;
            }
        });
    }
    private void bindReceiver() {
        IntentFilter intentFilter = new IntentFilter("tcpClientReceiver");
       getActivity(). registerReceiver(myBroadcastReceiver, intentFilter);
    }

    public void orClick() {
//        连接按钮
        btnStartClient.setEnabled(true);
//        断开按钮
        btnCloseClient.setEnabled(false);
    }

    /**
     *
     */
    private void initView(View view) {
//        监控按钮
        camera = view.findViewById(R.id.btn_camera);
//        提示
        tips =  view.findViewById(R.id.tv_tips);
//        连接
        btnStartClient =  view.findViewById(R.id.btn_tcpClientConn);
//        断开连接
        btnCloseClient =  view.findViewById(R.id.btn_tcpClientClose);
//        清除接收区
        CleanClientRcv = view. findViewById(R.id.lyclean);
//        ip输入框
        editClientPort =  view. findViewById(R.id.edit_tcpClientPort);
//        端口号输入框
        editClientIp = view. findViewById(R.id.edit_tcpClientIp);
//        接受的信息
        txtRcv =  view.findViewById(R.id.txt_ClientRcv);
        iv1 =  view.findViewById(R.id.iv_wave_1);
        iv2 =  view.findViewById(R.id.iv_wave_2);
        CleanClientRcv =  view.findViewById(R.id.lyclean);
        boomMenuButton =view.findViewById(R.id.bmb);
    }



    public void playVoice(Context context) {
        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.warning1);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止播放声音
     */

    public void stopVoice() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public static void setAnim1() {
        AnimationSet as = new AnimationSet(true);
        //缩放动画，以中心从原始放大到1.4倍
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.4f, 1.0f, 1.4f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.5f);
        scaleAnimation.setDuration(800);
        scaleAnimation.setRepeatCount(android.view.animation.Animation.INFINITE);
        alphaAnimation.setRepeatCount(android.view.animation.Animation.INFINITE);
        as.setDuration(800);
        as.addAnimation(scaleAnimation);
        as.addAnimation(alphaAnimation);
        iv1.startAnimation(as);
    }

    public static void setAnim2() {
        AnimationSet as = new AnimationSet(true);
        //缩放动画，以中心从1.4倍放大到1.8倍
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.4f, 1.8f, 1.4f, 1.8f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 0.1f);
        scaleAnimation.setDuration(800);
        scaleAnimation.setRepeatCount(android.view.animation.Animation.INFINITE);
        alphaAnimation.setRepeatCount(android.view.animation.Animation.INFINITE);
        as.setDuration(800);
        as.addAnimation(scaleAnimation);
        as.addAnimation(alphaAnimation);
        iv2.startAnimation(as);
    }
    private class MyBtnClicker implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
//                连接按钮
                case R.id.btn_tcpClientConn:
                    try {
                        Log.i(TAG, "onClick: 开始");
//                 得到ip和端口号
                        tcpClient = new TcpClient(editClientIp.getText().toString(), getPort(editClientPort.getText().toString()));
                        exec.execute(tcpClient);
                        btnStartClient.setEnabled(false);
                        btnCloseClient.setEnabled(true);
                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), "请输入正确的ip和端口号", Toast.LENGTH_LONG).show();
                    } finally {
                        break;
                    }
//                    断开连接按钮
                case R.id.btn_tcpClientClose:
                    if (tcpClient != null) {
                        tcpClient.closeSelf();
                    }
                    Toast.makeText(getActivity(), "连接断开", Toast.LENGTH_LONG).show();
                    orClick();
                    break;
                case R.id.btn_camera:
                    Intent intent = new Intent(getActivity(), SurveillanceActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    private class MyHandler extends android.os.Handler {
        private WeakReference<MainActivity> mActivity;

        MyHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            if (mActivity != null) {
                Log.i(TAG, "进入判断");
                try {
                    if (RECEIVER_MESSAGE_ANGRY.equals(msg.obj.toString().trim())) {
                        tips.setText("危险");
                        playVoice(getActivity());
                        iv2.setBackground(getActivity().getDrawable(R.drawable.shape_circle_red));
                        setAnim1();
                        setAnim2();
                        vibrator = (Vibrator)getActivity().getSystemService(VIBRATOR_SERVICE);
                        long[] patter = {100, 1000, 100, 1000};
                        vibrator.vibrate(patter, 1);
                    } else if (RECEIVER_MESSAGE_HAPPY.equals(msg.obj.toString().trim())) {
                        stopVoice();
                        tips.setText("开心");
                        Log.i(TAG, "提示后，动画前");
                        iv1.clearAnimation();
                        iv2.clearAnimation();
                        if (vibrator != null) {
                            vibrator.cancel();
                            stopVoice();
                        }
                        iv2.setBackground(getActivity().getDrawable(R.drawable.shape_circle_green));
                        stopVoice();
                    } else {
                        tips.setText("正常");
                        stopVoice();
                        iv1.clearAnimation();
                        iv2.clearAnimation();
                        if (vibrator != null) {
                            vibrator.cancel();
                            stopVoice();
                        }
                        iv2.setBackground(getActivity().getDrawable(R.drawable.shape_circle_blue));
                        stopVoice();
                    }
//                        追加消息
                    if (msg.what == 1) {
                        txtRcv.append(msg.obj.toString() + "\t");
                    }
                } catch (Exception e) {
                    if (tcpClient != null) {
                        e.printStackTrace();
                        Log.i("判断", "出错");
                        tcpClient.closeSelf();
                    }
                    orClick();
                    Toast.makeText(getActivity(), "连接失败！", Toast.LENGTH_LONG).show();
                    Vibrator vibrator = (Vibrator)getActivity().getSystemService(VIBRATOR_SERVICE);
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
            switch (mAction) {
                case "tcpClientReceiver":
                    String msg = intent.getStringExtra("tcpClientReceiver");
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = msg;
                    myHandler.sendMessage(message);
                    break;
                default:
                    break;
            }
        }
    }




    private int getPort(String port) {
        return Integer.parseInt(port);
    }
}

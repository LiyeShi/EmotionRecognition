package com.example.myapplication;

/**
 * @Auther: shiliye
 * @Date: 2019/7/10
 * @version: 1.0
 */

import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class TcpClient implements Runnable  {
    private String TAG = "TcpClient";
    private String serverIP;
    private int serverPort;
    private PrintWriter pw;
    private InputStream is;
    private DataInputStream dis;
    private boolean isRun = true;
    private Socket socket = null;
    byte buff[] = new byte[4096];
    private String rcvMsg;
    private int rcvLen;


    public TcpClient(String ip, int port) {
        this.serverIP = ip;
        this.serverPort = port;
    }

    public void closeSelf() {
        isRun = false;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(serverIP, serverPort);
            Log.i(TAG, "socket连接成功");
//            连接成功振动0.5s
            Vibrator vibrator = (Vibrator) FuncTcpClientActivity.context.getSystemService(FuncTcpClientActivity.context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
            socket.setSoTimeout(5000);
            pw = new PrintWriter(socket.getOutputStream(), true);
            is = socket.getInputStream();
            dis = new DataInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "断开连接");
        }
        while (isRun) {
            try {
                if (dis != null) {
                    rcvLen = dis.read(buff);
                    rcvMsg = new String(buff, 0, rcvLen, "utf-8");
                    Log.i(TAG, "run: 收到消息:" + rcvMsg);
                }
                Intent intent = new Intent();
                intent.setAction("tcpClientReceiver");
                intent.putExtra("tcpClientReceiver", rcvMsg);
                FuncTcpClientActivity.context.sendBroadcast(intent);//将消息发送给主界面
                if (("stop".equals(rcvMsg))) {   //服务器要求客户端结束
                    isRun = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (pw != null) {
            pw.close();
        }
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (dis != null) {
                dis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

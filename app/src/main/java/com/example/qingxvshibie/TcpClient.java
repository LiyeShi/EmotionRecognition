package com.example.qingxvshibie;

/**
 * @Auther: shiliye
 * @Date: 2019/8/19
 * @version: 1.0
 */

import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class TcpClient implements Runnable {
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
    private static Toast toast = null;


    public TcpClient(String ip, int port) {
        this.serverIP = ip;
        this.serverPort = port;
    }

    public void closeSelf() {
        isRun = false;
        Toast.makeText(FuncTcpClient.context, "连接断开", Toast.LENGTH_LONG).show();
    }

    @Override
    public void run() {
        try {
            socket = new Socket(serverIP, serverPort);
            Log.i("socket", "连接成功");
            socket.setSoTimeout(5000);
            pw = new PrintWriter(socket.getOutputStream(), true);
            is = socket.getInputStream();
            dis = new DataInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("断开连接", "qwe");
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
                FuncTcpClient.context.sendBroadcast(intent);//将消息发送给主界面
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

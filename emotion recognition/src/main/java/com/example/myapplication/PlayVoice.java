package com.example.myapplication;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * @Auther: shiliye
 * @Date: 2019/8/21
 * @version: 1.0
 */
//开始播放声音
public class PlayVoice {
    private static MediaPlayer mediaPlayer;

    public static void playVoice(Context context){
        try {
            mediaPlayer= MediaPlayer.create(context,R.raw.warning1);
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
    //停止播放声音
    public  static void stopVoice(){
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }
}

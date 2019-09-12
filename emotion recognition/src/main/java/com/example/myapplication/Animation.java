package com.example.myapplication;

import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

/**
 * @Auther: shiliye
 * @Date: 2019/8/22
 * @Description: com.example.myapplication
 * @version: 1.0
 */
public class Animation {
    //    动画效果
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
        FuncTcpClientActivity.iv1.startAnimation(as);
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
        FuncTcpClientActivity.iv2.startAnimation(as);
    }
}

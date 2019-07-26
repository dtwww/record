package com.example.shouye;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {
    private ImageView iv_start;
    private Button tiao_guo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,//隐藏状态栏
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        iv_start = (ImageView)findViewById(R.id.iv_start);
        tiao_guo = (Button)findViewById(R.id.tiao_guo);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 1);//初始化操作，参数传入1和1，即由透明度1变化到透明度为1
        iv_start.startAnimation(alphaAnimation);//开始动画

        alphaAnimation.setFillAfter(true);//动画结束后保持状态
        alphaAnimation.setDuration(3000);//动画持续时间

        tiao_guo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StartActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {//结束之后才到mainactivity
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent();
                intent.setClass(StartActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}

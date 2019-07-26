package com.example.shouye.diary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shouye.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ASUS on 2017/12/27.
 */

public class DiaryDetail extends AppCompatActivity implements  MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {
    private MediaPlayer mediaPlayer;
    private MediaController mediaController;;
    private boolean isAttached;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_detail);

        Intent intent = getIntent();
        String diaryYear = intent.getStringExtra("year");
        String diaryMonth = intent.getStringExtra("month");
        String diaryDay = intent.getStringExtra("day");
        final String diaryTime = intent.getStringExtra("time");
        String diaryTitle = intent.getStringExtra("title");
        String diaryContent = intent.getStringExtra("content");
        final String diaryAudio = intent.getStringExtra("audio");
        final int position = intent.getIntExtra("position", 0);
        TextView yearText = (TextView) findViewById(R.id.diary_detail_year);
        TextView monthText = (TextView) findViewById(R.id.diary_detail_month);
        TextView monthTextBig = (TextView) findViewById(R.id.diary_detail_month_big);
        TextView dayText = (TextView) findViewById(R.id.diary_detail_day);
        TextView dayTextBig = (TextView) findViewById(R.id.diary_detail_day_big);
        TextView timeText = (TextView) findViewById(R.id.diary_detail_time);
        final TextView titleText = (TextView) findViewById(R.id.diary_detail_title);
        final TextView contentText = (TextView) findViewById(R.id.diary_detail_content);
        final String id = intent.getStringExtra("id");
        yearText.setText(diaryYear);
        monthText.setText(diaryMonth);
        monthTextBig.setText(diaryMonth+"月");
        dayText.setText(diaryDay);
        dayTextBig.setText(diaryDay);
        timeText.setText(diaryTime);
        titleText.setText(diaryTitle);
        contentText.setText(diaryContent);

        titleText.setMovementMethod(new ScrollingMovementMethod());
        contentText.setMovementMethod(new ScrollingMovementMethod());

        ImageView imageView = (ImageView) findViewById(R.id.close);
        TextView editText = (TextView) findViewById(R.id.diary_detail_edit);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        editText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(DiaryDetail.this, DiaryEdit.class);
                intent.putExtra("id", id);
                intent.putExtra("title", titleText.getText().toString());
                intent.putExtra("content", contentText.getText().toString());
                intent.putExtra("audio", diaryAudio);
                intent.putExtra("position", position);
                startActivity(intent);
                finish();
            }
        });

        //将录音显示到详情界面
        loadAudio(diaryAudio);
    }

    private void loadAudio(String audio) {
       // final ImageView record = (ImageView) findViewById(R.id.record);
        //final ImageView deleteAudio = (ImageView) findViewById(R.id.delete_audio);

        //判断该备忘录是否有录音
        if (audio != null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(this);
            //File file = new File(audio);
            try {
                mediaPlayer.setDataSource(audio);
                mediaPlayer.prepare();
            } catch (IOException e) {
                Toast.makeText(DiaryDetail.this, "无法打开文件", Toast.LENGTH_SHORT);
                System.out.println(e.fillInStackTrace());
            }
        }
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }
    @Override
    public void pause() {
        mediaPlayer.pause();
    }
    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }
    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }
    @Override
    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }
    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }
    @Override
    public int getBufferPercentage() {
        return 0;
    }
    @Override
    public boolean canPause() {
        return true;
    }
    @Override
    public boolean canSeekBackward() {
        return true;
    }
    @Override
    public boolean canSeekForward() {
        return true;
    }
    @Override
    public int getAudioSessionId() {
        return 0;
    }

    //录音前的准备工作，显示播放器
    public void onPrepared(MediaPlayer mediaplayer) {
        final TextView content = (TextView) findViewById(R.id.diary_detail_content);
        mediaController = new MediaController(this) {
            @Override
            public void hide() {
                super.show();
            }
        };
        mediaController.setMediaPlayer(this);
        //将录音文件绑定并显示在文本框下面
        mediaController.setAnchorView(content);

        if (isAttached) {
            mediaController.show(0);
        }
    }

    //录音播放控制
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mediaController != null && !mediaController.isShowing()) {
            mediaController.show(0);
        }

        isAttached = true;
    }
}

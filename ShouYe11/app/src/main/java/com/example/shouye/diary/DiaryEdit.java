package com.example.shouye.diary;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.shouye.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by ASUS on 2017/12/27.
 */

public class DiaryEdit extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {
    private MediaRecorder recorder = new MediaRecorder();
    private MediaPlayer mediaPlayer;
    private MediaController mediaController;;
    private String localAudio = null;
    private boolean permissionToRecordAccepted = false;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private boolean isAttached = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit);
        //显示toolbar
        setSupportActionBar(toolbar);
        //隐藏toolbar上的默认title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final EditText titleEdit = (EditText) findViewById(R.id.edit_title);
        ImageView back = (ImageView) findViewById(R.id.back);
        ImageView save = (ImageView) findViewById(R.id.save);
        final EditText contentEdit = (EditText) findViewById(R.id.edit_content);
        final ImageView record = (ImageView) findViewById(R.id.record);
        titleEdit.setMovementMethod(new ScrollingMovementMethod());
        contentEdit.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        final int position = intent.getIntExtra("position", 0);
        localAudio = intent.getStringExtra("audio");
        titleEdit.setText(title);
        contentEdit.setText(content);

        requestAudioPermissions();

        loadAudio();

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Diary diaryToSave = new Diary(titleEdit.getText().toString(), contentEdit.getText().toString());
                if (diaryToSave.getAudio() != null && localAudio != diaryToSave.getAudio()) {
                    //判断原来是否有录音，若有则删除，重新录制
                    File oldAudio = new File(diaryToSave.getAudio());
                    if (oldAudio.exists()) {
                        oldAudio.delete();
                    }
                }
                diaryToSave.setAudio(localAudio);
                if (id != null) {
                    diaryToSave.setId(id);
                }
                //把diarytosave保存到数据库
                diaryToSave.save(DiaryEdit.this);
                Toast.makeText(DiaryEdit.this, "日记保存成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void loadAudio() {
        final ImageView record = (ImageView) findViewById(R.id.record);
        final ImageView deleteAudio = (ImageView) findViewById(R.id.delete_audio);
        if (localAudio != null) {
            record.setVisibility(View.INVISIBLE);
            record.setOnClickListener(null);
            deleteAudio.setVisibility(View.VISIBLE);
            deleteAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(DiaryEdit.this, android.R.style.Theme_Material_Dialog_Alert);
                    builder.setTitle("删除音频")
                            .setMessage("确定删除音频?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    localAudio = null;
                                    mediaController.hide();
                                    loadAudio();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(this);
            File file = new File(localAudio);
            try {
                mediaPlayer.setDataSource(localAudio);
                mediaPlayer.prepare();
            } catch (IOException e) {
                Toast.makeText(DiaryEdit.this, "无法打开文件", Toast.LENGTH_SHORT);
                System.out.println(e.fillInStackTrace());
            }
        } else {
            record.setVisibility(View.VISIBLE);
            deleteAudio.setVisibility(View.INVISIBLE);
            deleteAudio.setOnClickListener(null);
            mediaController = null;
            record.setOnTouchListener(new View.OnTouchListener() {
                String outputFileName;
                File outputFile;
                @Override
                public boolean onTouch(View arg0, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
                        outputFileName = "shouye_audio_" + timeFormat.format(new Date()) + ".3gp";
                        outputFile = new File(DiaryEdit.this.getFilesDir(), outputFileName);

                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                        try {
                            outputFile.createNewFile();
                            recorder.setOutputFile(outputFile.getAbsolutePath());
                            recorder.prepare();
                        } catch (IOException exception) {
                            Toast.makeText(DiaryEdit.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        recorder.start();
                        Toast.makeText(DiaryEdit.this, "开始录音", Toast.LENGTH_SHORT).show();
                        record.setImageResource(R.drawable.speak);
                    } else if (event.getAction() == MotionEvent.ACTION_UP){
                        recorder.stop();

                        localAudio = outputFile.getAbsolutePath();
                        Toast.makeText(DiaryEdit.this, "录音已保存", Toast.LENGTH_SHORT).show();
                        record.setImageResource(R.drawable.mic);
                        loadAudio();
                    }
                    return true;
                }
            });
        }
    }

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_AUDIO_PERMISSION);

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_AUDIO_PERMISSION);
            }
        }
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                return;
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

    //若有录音文件存在，则显示播放器，否则隐藏
    public void onPrepared(MediaPlayer mediaplayer) {
        final EditText contentEdit = (EditText) findViewById(R.id.edit_content);
        mediaController = new MediaController(this) {
            @Override
            public void hide() {
                if (localAudio != null) {
                    super.show();
                } else {
                    super.hide();
                }
            }
        };
        mediaController.setMediaPlayer(this);
        //将录音文件绑定并显示在文本框下面
        mediaController.setAnchorView(contentEdit);

        if (isAttached) {
            mediaController.show(0);
        }
    }

    @Override
    //录音播放控制
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mediaController != null && !mediaController.isShowing()) {
            mediaController.show(0);
        }

        isAttached = true;
    }
}

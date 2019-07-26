package com.example.shouye.diary;

import java.util.Date;
import java.util.List;
import android.content.Context;

import com.example.shouye.util.Persistable;
import com.example.shouye.util.Persister;

/**
 * Created by ASUS on 2017/12/26.
 */

public class Diary extends Persistable {
    private String id;
    private Date date;
    private String title;
    private String content;
    private String audio;

    public Diary(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Diary() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public String getAudio() {
        return audio;
    }

    public void setDate (Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent (String content) {
        this.content = content;
    }

    public void setAudio (String audio) {
        this.audio = audio;
    }

    public static List<Diary> getAllDiaries(Context context) {
        Persister persister = Persister.getInstance(context);
        return persister.list(Diary.class);
    }

    public static List<Diary> filterDiaries(Context context, String keyword) {
        Persister persister = Persister.getInstance(context);
        return persister.filterDiary(keyword);
    }
}

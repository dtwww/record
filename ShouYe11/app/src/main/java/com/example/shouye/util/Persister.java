package com.example.shouye.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.shouye.accounting.Amount;
import com.example.shouye.diary.Diary;

//创建数据库及对数据库的一些操作
public class Persister extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "main_db";
    public static final String TABLE_DIARY= "diary";
    public static final String DIARY_ID= "id";
    public static final String DIARY_DATE= "date";
    public static final String DIARY_TITLE= "title";
    public static final String DIARY_CONTENT= "content";
    public static final String DIARY_AUDIO= "audio";
    public static final String TABLE_AMOUNT= "amount";
    public static final String AMOUNT_ID= "id";
    public static final String AMOUNT_TYPE= "type";
    public static final String AMOUNT_PRICE= "price";
    public static final String AMOUNT_AMOUNT= "amount";
    public static final String AMOUNT_CONTENT= "content";
    public static final String AMOUNT_TIME= "time";

    private static Persister instance = null;

    //构造函数
    private Persister(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static Persister getInstance(Context context) {
        if (instance == null) {
            instance = new Persister(context.getApplicationContext());
        }

        return instance;
    }

    //创建表
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DIARY_TABLE = "CREATE TABLE " + TABLE_DIARY + "("
                + DIARY_ID + " INTEGER PRIMARY KEY," + DIARY_TITLE + " TEXT,"
                + DIARY_CONTENT + " TEXT," + DIARY_AUDIO + " TEXT," + DIARY_DATE + " DATE)";
        String CREATE_AMOUNT_TABLE = "CREATE TABLE " + TABLE_AMOUNT + "("
                + AMOUNT_ID + " INTEGER PRIMARY KEY," + AMOUNT_TYPE + " INTEGER,"
                + AMOUNT_AMOUNT + " INTEGER," + AMOUNT_CONTENT + " INTEGER,"
                + AMOUNT_PRICE + " REAL," + AMOUNT_TIME + " DATE)";
        db.execSQL(CREATE_DIARY_TABLE);
        db.execSQL(CREATE_AMOUNT_TABLE);
    }
    //数据库更新，删除旧的创建新的
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIARY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AMOUNT);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //保存方法
    public void save(Persistable itemToSave) {
        //判断是日记还是记账
        if (Diary.class.isInstance(itemToSave)) {
            Diary diaryToSave = (Diary) itemToSave;
            SQLiteDatabase db = this.getWritableDatabase();

            //values负责存储键值对
            ContentValues values = new ContentValues();
            values.put(DIARY_TITLE, diaryToSave.getTitle());
            values.put(DIARY_CONTENT, diaryToSave.getContent());
            values.put(DIARY_AUDIO, diaryToSave.getAudio());

            //不为空更新，为空创建
            if (diaryToSave.getId() != null) {
                //数据库、recyclerview、日记的id是一样的
                db.update(TABLE_DIARY, values, DIARY_ID + " = ?", new String[]{diaryToSave.getId()});

            } else {
                //为空则需要插入创建时的日期
                values.put(DIARY_DATE, String.valueOf(new Date()));
                //数据库不允许插入内容为空，故需指定一个null才能插入
                db.insert(TABLE_DIARY, null, values);
                //Cursor cursor = db.rawQuery("SELECT id, title, content, audio, date FROM " + TABLE_DIARY , null );
                //cursor.moveToLast();
                //System.out.println("id"+cursor.getString(0));
                //System.out.println("title"+cursor.getString(1));
                //System.out.println("content"+cursor.getString(2));
                //System.out.println("audio"+cursor.getString(3));
                //System.out.println("date"+cursor.getString(4));
            }
        } else if (Amount.class.isInstance(itemToSave)) {
            Amount amountToSave = (Amount) itemToSave;
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(AMOUNT_AMOUNT, amountToSave.getAmount());
            values.put(AMOUNT_CONTENT, amountToSave.getContent());
            values.put(AMOUNT_PRICE, amountToSave.getPrice());
            values.put(AMOUNT_TYPE, amountToSave.getType());

            if (amountToSave.getId() != null) {
                db.update(TABLE_AMOUNT, values, AMOUNT_ID + " = ?", new String[]{amountToSave.getId()});
            } else {
                values.put(AMOUNT_TIME, String.valueOf(new Date()));
                db.insert(TABLE_AMOUNT, null, values);
            }
        }
    }

    //得到数据库全部数据
    public List list(Class itemClass) {
        if (itemClass.getSimpleName().equals("Diary")) {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT id, title, content, audio, date FROM " + TABLE_DIARY, null);
            //因查询日记时也要用到通过游标查询数据库内容，故把下面的语句封装成一个方法放在该文件最后
            return getDiariesFromCursor(cursor);
        } else if (itemClass.getSimpleName().equals("Amount")) {
            SQLiteDatabase db = this.getReadableDatabase();

            //通过游标从数据库中进行查询,null:无特殊要求
            Cursor cursor = db.rawQuery("SELECT id, type, amount, price , content, time FROM " + TABLE_AMOUNT, null);
            List<Amount> allAmounts = new ArrayList();
            //若游标不在最开始的地方，将它移动到最开始
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                //通过游标的移动读数据库中amount类型的每一条数据
                do {
                    Amount amount = new Amount();
                    amount.setId(cursor.getString(0));
                    amount.setType(Integer.parseInt(cursor.getString(1)));
                    amount.setAmount(Integer.parseInt(cursor.getString(2)));
                    amount.setPrice(Double.parseDouble(cursor.getString(3)));
                    amount.setContent(cursor.getString(4));
                    amount.setTime(new Date(cursor.getString(5)));
                    allAmounts.add(amount);
                } while (cursor.moveToNext());
            }

            return allAmounts;
        }
        return new ArrayList();
    }

    //返回所有带有keyword的日记列表
    public List filterDiary(String keyword) {
        SQLiteDatabase db = this.getWritableDatabase();
        //查询题目或内容中带有keyword的条目
        Cursor cursor = db.rawQuery("SELECT id, title, content, audio, date FROM " + TABLE_DIARY + " WHERE title LIKE ? OR content LIKE ?", new String[] {"%" + keyword + "%", "%" + keyword + "%"});
        return getDiariesFromCursor(cursor);
    }

    //删除Id=传入参数的条目
    public void deleteDiary(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("Delete from " + TABLE_DIARY + " where id =" + id);
    }
    public void deleteAmount(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("Delete from " + TABLE_AMOUNT + " where id =" + id);
    }

    private List<Diary> getDiariesFromCursor(Cursor cursor) {
        List<Diary> diaries = new ArrayList();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Diary diary = new Diary(cursor.getString(1), cursor.getString(2));
                diary.setDate(new Date(cursor.getString(4)));
                diary.setId(cursor.getString(0));
                diary.setAudio(cursor.getString(3));
                diaries.add(diary);
                //System.out.println("id"+cursor.getString(0));
                //System.out.println("title"+cursor.getString(1));
                //System.out.println("content"+cursor.getString(2));
                //System.out.println("audio"+cursor.getString(3));
                //System.out.println("date"+cursor.getString(4));
            } while (cursor.moveToNext());
        }
        return diaries;
    }
}

package com.example.shouye.diary;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.shouye.R;

import java.util.List;

public class DiaryListActivity extends AppCompatActivity {

    private DiaryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
        ImageView back = (ImageView) findViewById(R.id.back);
        ImageView search = (ImageView) findViewById(R.id.search);
        final EditText searchKeyword = (EditText) findViewById(R.id.search_keyword);

        //显示toolbar
        setSupportActionBar(toolbar);
        //隐藏toolbar上的默认title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(DiaryListActivity.this, DiaryEdit.class);
                startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchKeyword.getWindowToken(), 0);
                String keyword = searchKeyword.getText().toString();
                loadDiaries(keyword);

            }
        });

        this.loadDiaries(null);
    }

    //可交互状态
    @Override
    protected void onResume() {
        this.loadDiaries(null);
        super.onResume();
    }

    private void loadDiaries(String keyword) {
        List<Diary> diaries;
        if (keyword == null || keyword.trim() == "") {
            diaries = Diary.getAllDiaries(DiaryListActivity.this);
        } else {
            diaries = Diary.filterDiaries(DiaryListActivity.this, keyword);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.diary_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DiaryAdapter(diaries);
        recyclerView.setAdapter(adapter);
    }
}

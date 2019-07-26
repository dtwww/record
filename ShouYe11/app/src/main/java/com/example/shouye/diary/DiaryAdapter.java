package com.example.shouye.diary;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shouye.R;
import com.example.shouye.util.Persister;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ASUS on 2017/12/26.
 */

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {
    private Context mContext;
    private List<Diary> mDiaryList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View diaryView;
        CardView cardView;
        TextView diaryYear;
        TextView diaryMonth;
        TextView diaryDay;
        TextView diaryTime;
        TextView diaryTitle;
        ImageView diaryWeather;
        ImageView diaryMood;

        public ViewHolder(View view){
            super(view);
            diaryView = view;
            cardView = (CardView) view;
            diaryYear = (TextView) view.findViewById(R.id.diary_year);
            diaryMonth = (TextView) view.findViewById(R.id.diary_month);
            diaryDay = (TextView) view.findViewById(R.id.diary_day);
            diaryTime = (TextView) view.findViewById(R.id.diary_time);
            diaryTitle = (TextView) view.findViewById(R.id.diary_title);
            diaryWeather = (ImageView) view.findViewById(R.id.diary_weather);
            diaryMood = (ImageView) view.findViewById(R.id.diary_mood);
        }
    }

    public DiaryAdapter(List<Diary> diaryList){
        mDiaryList = diaryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.diary_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        //长按事件
        holder.diaryView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext,v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_item,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Persister persister= Persister.getInstance(mContext);
                        int position=holder.getAdapterPosition();
                        Diary diary = mDiaryList.get(holder.getAdapterPosition());
                        String id = diary.getId();
                        //若音频不为空，需要删除音频文件
                        if (diary.getAudio() != null) {
                            File audioFile = new File(diary.getAudio());
                            if (audioFile.exists()) {
                                audioFile.delete();
                            }
                        }
                        //分别在数据库和界面上删除该条目并刷新
                        persister.deleteDiary(id);
                        mDiaryList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemChanged(position);
                        return true;
                    }
                });
                //重写框消失方法
                popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        Toast.makeText(mContext, "成功删除", Toast.LENGTH_SHORT).show();
                    }
                });
                popupMenu.show();
                return true;
            }
        });
        //点击事件
        holder.diaryView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                Diary diary = mDiaryList.get(position);
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
                Intent intent = new Intent(mContext, DiaryDetail.class);
                intent.putExtra("id", diary.getId());
                intent.putExtra("title", diary.getTitle());
                intent.putExtra("content", diary.getContent());
                intent.putExtra("audio", diary.getAudio());
                intent.putExtra("year", yearFormat.format(diary.getDate()));
                intent.putExtra("month", monthFormat.format(diary.getDate()));
                intent.putExtra("day", dayFormat.format(diary.getDate()));
                intent.putExtra("time", timeFormat.format(diary.getDate()));
//                intent.putExtra("month", diary.getMonth());
//                intent.putExtra("day", diary.getDay());
//                intent.putExtra("time", diary.getTime());
                intent.putExtra("title", diary.getTitle());
                intent.putExtra("position", position);
                mContext.startActivity(intent);
//                Toast.makeText(v.getContext(), "日记详情：" + diary.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    //让该条目显示题目、内容、时间啥的
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Diary diary = mDiaryList.get(position);
        Calendar cal = Calendar.getInstance();
        cal.setTime(diary.getDate());

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
        holder.diaryYear.setText(yearFormat.format(diary.getDate()));
        holder.diaryMonth.setText(monthFormat.format(diary.getDate()));
        holder.diaryDay.setText(dayFormat.format(diary.getDate()));
        holder.diaryTime.setText(timeFormat.format(diary.getDate()));
        holder.diaryTitle.setText(diary.getTitle());
//        Glide.with(mContext).load(diary.getWeatherId()).into(holder.diaryWeather);
//        Glide.with(mContext).load(diary.getMoodId()).into(holder.diaryMood);
    }

    @Override
    public int getItemCount(){
        return mDiaryList.size();
    }
}
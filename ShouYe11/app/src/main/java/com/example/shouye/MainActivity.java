package com.example.shouye;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.shouye.accounting.AccountingMain;
import com.example.shouye.diary.DiaryListActivity;
import com.example.shouye.weather.WeatherMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shouye.R.id.itemname;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private Context mContext;
    private GridView gv;
    private WaterSharedHelper sh;
    private ImageView water;
    private Button clear;

    //图片轮循
    protected static final String TAG = "MainActivity";
    private List<ImageView> imageViewList;
    private ViewPager mViewPager;
    private final int[] imageResIDs = {
            R.drawable.cartoon1,
            R.drawable.cartoon2,
            R.drawable.cartoon3,
            R.drawable.cartoon4,
            R.drawable.cartoon5
    };
    private LinearLayout llPointGroup;  // 点控件的组
    private int previousPosition = 0;  // viewpager选中的前一个position
    private boolean isStop = false; // 控制循环的子线程是否停止

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;
        sh = new WaterSharedHelper(mContext);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        water = (ImageView) findViewById(R.id.water);

        clear = (Button)findViewById(R.id.clear_water);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击置零
                sh.save(0);
                water.setImageResource(R.drawable.water0);
            }
        });
        switch(sh.read()) {//从数据库里读，读了哪个数就置哪个图片
            case 0:
                water.setImageResource(R.drawable.water0);
                break;
            case 1:
                water.setImageResource(R.drawable.water1);
                break;
            case 2:
                water.setImageResource(R.drawable.water2);
                break;
            case 3:
                water.setImageResource(R.drawable.water3);
                break;
            case 4:
                water.setImageResource(R.drawable.water4);
                break;
            case 5:
                water.setImageResource(R.drawable.water5);
                break;
            case 6:
                water.setImageResource(R.drawable.water6);
                break;
            default:
        }

        //显示toolbar
        setSupportActionBar(toolbar);
        //隐藏toolbar上默认的title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //准备要添加的数据条目
        List<Map<String, Object>> menus = new ArrayList<Map<String,Object>>();

        Map<String, Object> diaryMenu = new HashMap<String, Object>();
        diaryMenu.put("itemicon", R.drawable.text);
        diaryMenu.put("itemname", "日记");

        Map<String, Object> accountingMenu = new HashMap<String, Object>();
        accountingMenu.put("itemicon", R.drawable.table);
        accountingMenu.put("itemname", "记账");

        Map<String, Object> weatherMenu = new HashMap<String, Object>();
        weatherMenu.put("itemicon", R.drawable.weather);
        weatherMenu.put("itemname", "天气");

        menus.add(diaryMenu);
        menus.add(accountingMenu);
        menus.add(weatherMenu);
        //实例化一个适配器
        SimpleAdapter adapter = new SimpleAdapter(this, menus, R.layout.grid_item, new String[]{"itemicon", "itemname"}, new int[]{R.id.itemicon, itemname});

        //获得GridView实例
        gv = (GridView)findViewById(R.id.grid_view);

        //为GridView设置适配器
        gv.setAdapter(adapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent diaryIntent = new Intent(MainActivity.this, DiaryListActivity.class);
                        startActivity(diaryIntent);
                        break;
                    case 1:
                        Intent accountingIntent = new Intent(MainActivity.this, AccountingMain.class);
                        startActivity(accountingIntent);
                        break;
                    case 2:
                        Intent weatherIntent = new Intent(MainActivity.this, WeatherMain.class);
                        startActivity(weatherIntent);
                        break;
                }
            }
        });

        water.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int number = sh.read();
                System.out.print(number);
                switch(number) {
                    case 0:
                        water.setImageResource(R.drawable.water1);
                        sh.save(1);
                        break;
                    case 1:
                        water.setImageResource(R.drawable.water2);
                        sh.save(2);
                        break;
                    case 2:
                        water.setImageResource(R.drawable.water3);
                        sh.save(3);
                        break;
                    case 3:
                        water.setImageResource(R.drawable.water4);
                        sh.save(4);
                        break;
                    case 4:
                        water.setImageResource(R.drawable.water5);
                        sh.save(5);
                        break;
                    case 5:
                        water.setImageResource(R.drawable.water6);
                        sh.save(6);
                        break;
                    case 6:
                        Toast.makeText(mContext,"今天已经喝了6杯水哦~",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
            }
        });

        initView();

        new Thread(new Runnable() {

            @Override
            public void run() {
                // 每两秒钟向主线程发送一条消息, 切换viewpager的界面
                while(!isStop) {
                    SystemClock.sleep(2000);

                    handler.sendEmptyMessage(0);
                }
                Log.i(TAG, "循环线程停止了");
            }}).start();
    }

    //查找menu文件夹里的toolbar文件，并将菜单显示出来
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    //menu中toolbar文件里item的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.change_day:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                NightModeConfig.getInstance().setNightMode(getApplicationContext(),false);
                recreate();
                break;
            case R.id.change_night:
                //保存夜间模式状态,Application中可以根据这个值判断是否设置夜间模式
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                //ThemeConfig主题配置，这里只是保存了是否是夜间模式的boolean值
                NightModeConfig.getInstance().setNightMode(getApplicationContext(),true);
                recreate();
                break;
            default:
        }
        return true;
    }

    //图片轮循
    @Override
    protected void onDestroy() {
        isStop = true;//如果线程停止
        super.onDestroy();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);

        imageViewList = new ArrayList<ImageView>();

        ImageView iv;
        View pointView;
        LinearLayout.LayoutParams params;
        for (int i = 0; i < imageResIDs.length; i++) {
            iv = new ImageView(this);
            iv.setBackgroundResource(imageResIDs[i]);
            imageViewList.add(iv);

            // 根据图片添加点
            pointView = new View(this);
            params = new LinearLayout.LayoutParams(5, 5);
            params.leftMargin = 5;
            pointView.setLayoutParams(params);
            pointView.setEnabled(false);
            pointView.setBackgroundResource(R.drawable.cartoon5);
            llPointGroup.addView(pointView);
        }

        MyPagerAdapter mAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);

        int item = (Integer.MAX_VALUE / 2) - ((Integer.MAX_VALUE / 2) % imageViewList.size());
        mViewPager.setCurrentItem(item);  // 设置当前选中的item的position

        llPointGroup.getChildAt(previousPosition).setEnabled(true); // 第一点被选中
    }


    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        /**
         * 移动的对象和进来的对象如果是同一个就返回true, 代表复用view对象
         * false 使用object对象
         */
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        /**
         * 需要销毁的对象的position传进来
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 移除掉指定position的对象
            mViewPager.removeView(imageViewList.get(position % imageViewList.size()));
        }

        /**
         * 加载position位置的view对象
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 添加指定position的对象
            mViewPager.addView(imageViewList.get(position % imageViewList.size()));
            return imageViewList.get(position % imageViewList.size());
        }
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    /**
     * 当viewpager页面切换时回调该函数，显示下一张图片，隐藏上一张图片
     */
    @Override
    public void onPageSelected(int position) {
        llPointGroup.getChildAt(position % imageViewList.size()).setEnabled(true);
        llPointGroup.getChildAt(previousPosition).setEnabled(false);

        previousPosition = position % imageViewList.size();
    }
}

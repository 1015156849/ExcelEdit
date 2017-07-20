package com.yzy.exceledit;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yzy.exceledit.util.ExcelUtils;
import com.yzy.exceledit.util.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    private AlertView mAlertViewExt;//窗口拓展例子
    RecyclerView recyclerView;
    Button clean;
    Button add;
    Button excel;

    private ArrayList<String> list = new ArrayList<String>() {{
        add("");
    }};
    private List<ArrayList<String>> arrayMap = new ArrayList<>();
    private RecycleViewAdapter recycleViewAdapter;

    public static String filePath;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnTouchListener(this);
        context = this;
        Init();

    }

    private void Init() {
        initEx();
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        clean = (Button) findViewById(R.id.clean);
        add = (Button) findViewById(R.id.add);
        excel = (Button) findViewById(R.id.excel);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        recycleViewAdapter = new RecycleViewAdapter(list);
        recycleViewAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerView.setAdapter(recycleViewAdapter);

        recyclerView.setOnTouchListener(this);
        clean.setOnTouchListener(this);
        add.setOnTouchListener(this);
        excel.setOnTouchListener(this);


        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                arrayMap.clear();
                list.add("");
                recycleViewAdapter.notifyDataSetChanged();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {

                                       if (list.size() == 1 && (list.get(0).isEmpty() || list.get(0).trim().isEmpty())) {
                                           new AlertView("提示", "请输入内容", "我知道了", null, null, context, AlertView.Style.Alert, null)
                                                   .show();
                                           return;
                                       }
                                       ArrayList<String> tempList = new ArrayList<String>();
                                       for (String s : list) {
                                           tempList.add(s);
                                       }
                                       arrayMap.add(tempList);
                                       list.clear();
                                       list.add("");
                                       Toast.makeText(context, "已添加,当前行数" + arrayMap.size(), Toast.LENGTH_SHORT).show();
                                       recycleViewAdapter.notifyDataSetChanged();

                                   }
                               }
        );

        excel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ExcelUtils.writeXLS(getFilePath(), arrayMap);

                Toast.makeText(context, "文件已导出，名称:" + filePath, Toast.LENGTH_LONG).show();
                FileUtils.openFile(context, new File(filePath));

            }
        });

    }


    private String getFilePath() {
        String path = Environment.getExternalStorageDirectory() + "/Excel_" + getNewDate() + ".xls";
        filePath = path;
        return path;
    }

    private String getNewDate() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = df.format(date);
        return time;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                mAlertViewExt.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initEx(){
        //拓展窗口
        mAlertViewExt =  new AlertView("提示", "请输入内容", "我知道了", null, null, context, AlertView.Style.Alert, null);
        ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.dialog_layout,null);

        mAlertViewExt.addExtView(extView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        return onTouchEvent(motionEvent);
    }
}

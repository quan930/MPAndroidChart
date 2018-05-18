package com.example.daquan.mpandroidchart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //折线图
    private Button pieChart;//饼图
    private Button barChart;
    private LineChart lineChart;
    private List<Entry> entries = new ArrayList<Entry>();
    private List<String> xx;
    private LineData lineData;
    private int num = 0;
    private String url = "http://60.205.223.43:7000/getTemp";
    private JSONObject jsonObject;
    private Response.Listener<JSONObject> listener;
    private Response.ErrorListener errorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lineChart = findViewById(R.id.lineChart);
        pieChart = findViewById(R.id.main1_start_pieChart);
        barChart = findViewById(R.id.main1_start_bar);

        lineChart.setDragEnabled(true);
        lineChart.setScaleXEnabled(true);//启用/禁用缩放在x轴上。
        lineChart.setScaleYEnabled(false);//y轴
        lineChart.setDescription(""); //数据描述

        //x轴y轴
        YAxis leftYAxis = lineChart.getAxisLeft();
        YAxis rightYAxis = lineChart.getAxisRight();
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//显示x轴下方
        leftYAxis.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, YAxis yAxis) {
                return (int) v + "℃";
            }
        });
        rightYAxis.setEnabled(false); //右侧Y轴不显示

        //设置数据
        entries = new ArrayList<Entry>();
        xx = new ArrayList<String>();
        //一个LineDataSet就是一条线
        LineDataSet lineDataSet = new LineDataSet(entries, "温度");
        lineData = new LineData(xx,lineDataSet);
        lineChart.setData(lineData);

        jsonObject = null;
        listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String time = jsonObject.getString("time");
                    float val = (float)jsonObject.getDouble("tem");
                    entries.add(new Entry(val,num));
                    xx.add(time);
                    //通知数据已经改变
                    lineData.notifyDataChanged();
                    lineChart.notifyDataSetChanged();
                    //设置在曲线图中显示的最大数量
                    lineChart.setVisibleXRangeMaximum(5);
                    //移到某个位置
                    lineChart.moveViewToX(lineData.getXValCount() - 5);
                    num++;
                    lineChart.setVisibleXRangeMaximum(num);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,url,jsonObject,listener,errorListener);
                Volley.newRequestQueue(MainActivity.this).add(jsonObjectRequest);
                if(num>20){
                    cancel();//销毁定时器
                }
            }
        },0,1000);
        //饼图
        pieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
                finish();
            }
        });
        //条形图
        barChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Main3Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

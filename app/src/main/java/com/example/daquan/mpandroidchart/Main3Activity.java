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
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//柱形图

public class Main3Activity extends AppCompatActivity {
    Button startLine;
    Button startPie;
    BarChart barChart;
    String url = "http://60.205.223.43:7000/getBreak";
    private JSONObject jsonObject;
    Response.Listener<JSONObject> listener;
    Response.ErrorListener errorListener;
    private int munPost = 0;
    private int mun = 0;
    private List<BarEntry> entryList = new ArrayList<>();
    private List<String> nameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        startLine = findViewById(R.id.main3_start_lineChart);
        startPie = findViewById(R.id.main3_start_pieChart);
        barChart = findViewById(R.id.quan_barChart);


        jsonObject = null;
        listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    float val= (float) jsonObject.getDouble("break");
                    entryList.add(new BarEntry(val,mun));
                    nameList.add(String.valueOf(mun));
                    mun++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (mun == 9){
                    barChart.setDrawBarShadow(false);//阴影
                    barChart.setEnabled(false);
                    barChart.setDescription("");// 数据描述
                    barChart.setDrawValueAboveBar(false);//柱状图上面的数值是否在柱子上面
                    barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//显示x轴下方
                    BarDataSet barDataSet = new BarDataSet(entryList,"违章情况");
                    barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    barChart.animateY(1000, Easing.EasingOption.EaseInBack);
                    BarData barData = new BarData(nameList,barDataSet);
                    barChart.setData(barData);
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
                Volley.newRequestQueue(Main3Activity.this).add(jsonObjectRequest);
                munPost++;
                if(munPost==9){
                    cancel();
                }
            }
        },0,1);

        startLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main3Activity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        startPie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main3Activity.this,Main2Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

package com.example.daquan.mpandroidchart;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

//饼图
public class Main2Activity extends AppCompatActivity {
    private Button startline;
    private Button startBar;
    private PieChart pieChart;
    private List<Entry> entries = new ArrayList<>();
    private List<String> nameX = new ArrayList<>();
    private PieData pieData;
    private String url = "http://60.205.223.43:7000/getData";
    private int num = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        startline = findViewById(R.id.main2_start_lineChart);
        startBar = findViewById(R.id.main2_start_barChart);
        pieChart = findViewById(R.id.pieChart);

        JSONObject jsonObject = null;
        Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                for (int i = 0 ;i < jsonArray.length() ; i++){
                    try {
                        float data =(float) jsonArray.getJSONObject(i).getDouble("data");
                        entries.add(new Entry(data,num));
                        nameX.add(jsonArray.getJSONObject(i).getString("name"));
                        num++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                pieChart.setRotationEnabled(true); //可以手动旋转
                pieChart.setUsePercentValues(true); //显示百分比
                pieChart.setDescription("");
                pieChart.setDrawHoleEnabled(false);//设置有无圆圈


                PieDataSet pieDataSet = new PieDataSet(entries,"违章情况");
                pieDataSet.setSliceSpace(4f);//空格
                pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);//为DataSet中的数据匹配上颜色集(饼图Item颜色)

                pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);//动画
                pieData = new PieData(nameX,pieDataSet);
                pieChart.setData(pieData);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        };
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,jsonArrayListener,errorListener);
        Volley.newRequestQueue(this).add(jsonArrayRequest);

        //折线图
        startline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //饼形图
        startBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main2Activity.this,Main3Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

package com.zhiyuan3g.daynewsdemo.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhiyuan3g.daynewsdemo.R;
import com.zhiyuan3g.daynewsdemo.activity.DetialActivity;
import com.zhiyuan3g.daynewsdemo.adapter.MyNewsTopAdapter;
import com.zhiyuan3g.daynewsdemo.db.SCDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kkkkk on 2016/10/13.
 */
public class NewsYule extends Fragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2{
    private PullToRefreshListView listView;
    private List<Map<String, Object>> list;
    private Map<String, Object> map;
    private RequestQueue requestQueue;
    private MyNewsTopAdapter adapter;
    private SCDB scdb;
    private SQLiteDatabase writeDB, readDB;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_news_yule,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化数据库
        initDB();
        //初始化控件
        initView(view);
        //初始化上下拉刷新
        initPullToRefrshView();
        //如果数据库中有数据则读取数据库，否则发送网络请求
        initDate();
        listView.setOnItemClickListener(this);
        listView.setOnRefreshListener(this);
    }

    private void initDate() {
        Cursor cursor = readDB.rawQuery("SELECT * FROM myyule order by _id desc", null);
        if(cursor.getCount() != 0){
            while(cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String author_name = cursor.getString(cursor.getColumnIndex("author_name"));
                String thumbnail_pic_s = cursor.getString(cursor.getColumnIndex("thumbnail_pic_s"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                map = new HashMap<>();
                map.put("title",title);
                map.put("author_name",author_name);
                map.put("thumbnail_pic_s",thumbnail_pic_s);
                map.put("url",url);
                list.add(map);
            }
            adapter = new MyNewsTopAdapter(getContext(), list);
            listView.setAdapter(adapter);
        }else{
            initGetRequest();
        }
    }

    private void initDB() {
        scdb = new SCDB(getContext());
        writeDB = scdb.getWritableDatabase();
        readDB = scdb.getReadableDatabase();
    }

    private void initPullToRefrshView() {
        listView.getLoadingLayoutProxy(false, true).setPullLabel("上拉刷新");
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多");

        listView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在加载");
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel("松开加载更多");
    }

    private void initView(View view) {
        list = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());
        listView = (PullToRefreshListView) view.findViewById(R.id.pullToRefresh_yule);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setScrollingWhileRefreshingEnabled(true);
    }

    private void initGetRequestUp() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://v.juhe.cn/yule/index?type=junshi&key=8b13d671c5a2c4ffb94321ea8dcab509", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            writeDB.delete("myyule",null,null);
                            JSONObject result = jsonObject.getJSONObject("result");
                            JSONArray data = result.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                ContentValues cv = new ContentValues();
                                map = new HashMap<>();
                                JSONObject jsonDate = data.getJSONObject(i);
                                String title = jsonDate.getString("title");
                                String author_name = jsonDate.getString("author_name");
                                String thumbnail_pic_s = jsonDate.getString("thumbnail_pic_s");
                                String url = jsonDate.getString("url");
                                map.put("title", title);
                                map.put("author_name", author_name);
                                map.put("thumbnail_pic_s", thumbnail_pic_s);
                                map.put("url", url);
                                list.add(map);
                                cv.put("title",title);
                                cv.put("author_name",author_name);
                                cv.put("thumbnail_pic_s",thumbnail_pic_s);
                                cv.put("url",url);
                                writeDB.insert("myyule",null,cv);
                            }
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listView.onRefreshComplete();
                        Toast.makeText(getContext(), "网络加载错误", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void initGetRequestDown() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://v.juhe.cn/toutiao/index?type=shehui&key=8b13d671c5a2c4ffb94321ea8dcab509", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            writeDB.delete("myyule",null,null);
                            JSONObject result = jsonObject.getJSONObject("result");
                            JSONArray data = result.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                ContentValues cv = new ContentValues();
                                map = new HashMap<>();
                                JSONObject jsonDate = data.getJSONObject(i);
                                String title = jsonDate.getString("title");
                                String author_name = jsonDate.getString("author_name");
                                String thumbnail_pic_s = jsonDate.getString("thumbnail_pic_s");
                                String url = jsonDate.getString("url");
                                map.put("title", title);
                                map.put("author_name", author_name);
                                map.put("thumbnail_pic_s", thumbnail_pic_s);
                                map.put("url", url);
                                list.add(0, map);
                                cv.put("title",title);
                                cv.put("author_name",author_name);
                                cv.put("thumbnail_pic_s",thumbnail_pic_s);
                                cv.put("url",url);
                                writeDB.insert("myyule",null,cv);
                            }
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listView.onRefreshComplete();
                        Toast.makeText(getContext(), "网络加载错误", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void initGetRequest() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://v.juhe.cn/toutiao/index?type=yule&key=8b13d671c5a2c4ffb94321ea8dcab509", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONObject result = jsonObject.getJSONObject("result");
                            JSONArray data = result.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                ContentValues cv = new ContentValues();
                                map = new HashMap<>();
                                JSONObject jsonDate = data.getJSONObject(i);
                                String title = jsonDate.getString("title");
                                String author_name = jsonDate.getString("author_name");
                                String thumbnail_pic_s = jsonDate.getString("thumbnail_pic_s");
                                String url = jsonDate.getString("url");
                                map.put("title", title);
                                map.put("author_name", author_name);
                                map.put("thumbnail_pic_s", thumbnail_pic_s);
                                map.put("url", url);
                                list.add(map);
                                cv.put("title",title);
                                cv.put("author_name",author_name);
                                cv.put("thumbnail_pic_s",thumbnail_pic_s);
                                cv.put("url",url);
                                writeDB.insert("myyule",null,cv);
                            }
                            adapter = new MyNewsTopAdapter(getContext(), list);
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listView.onRefreshComplete();
                        Toast.makeText(getContext(), "网络加载错误", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), DetialActivity.class);
        System.out.println(position);
        intent.putExtra("url", list.get((int) id).get("url").toString());
        intent.putExtra("title", list.get((int) id).get("title").toString());
        intent.putExtra("author_name", list.get((int) id).get("author_name").toString());
        intent.putExtra("thumbnail_pic_s", list.get((int) id).get("thumbnail_pic_s").toString());
        startActivity(intent);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        //获取每次刷新的时间记录
        String date = "上次刷新：" + DateUtils.formatDateTime(getContext(),
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME);
        //每次刷新后更新时间
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(date);
        initGetRequestDown();

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        //获取每次刷新的时间记录
        String date = "上次刷新：" + DateUtils.formatDateTime(getContext(),
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME);
        //每次刷新后更新时间
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(date);
        initGetRequestUp();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scdb.close();
    }
}

package com.zhiyuan3g.daynewsdemo.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.zhiyuan3g.daynewsdemo.R;
import com.zhiyuan3g.daynewsdemo.db.SCDB;

import butterknife.BindView;

public class MySCActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView listView;
    private SCDB scdb;
    private SQLiteDatabase readDB;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    private ImageView img_mysc_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sc);
        initView();
    }

    private void initListView() {
        cursor = readDB.rawQuery("SELECT * FROM sc order by _id desc", null);
        adapter = new SimpleCursorAdapter(this, R.layout.layout_mysc_item, cursor, new String[]{"title"}, new int[]{R.id.tv_mysc_item});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void initView() {
        scdb = new SCDB(this);
        readDB = scdb.getReadableDatabase();
        listView = (ListView) findViewById(R.id.listView_mysc);
        img_mysc_back = (ImageView) findViewById(R.id.img_mysc_back);
        img_mysc_back.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = adapter.getCursor();
        cursor.moveToPosition(position);
        String title = cursor.getString(cursor.getColumnIndex("title"));
        String url = cursor.getString(cursor.getColumnIndex("url"));
        String author_name = cursor.getString(cursor.getColumnIndex("author_name"));
        String thumbnail_pic_s = cursor.getString(cursor.getColumnIndex("thumbnail_pic_s"));
        Intent intent = new Intent(MySCActivity.this, DetialActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.putExtra("author_name", author_name);
        intent.putExtra("thumbnail_pic_s", thumbnail_pic_s);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}

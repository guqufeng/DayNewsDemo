package com.zhiyuan3g.daynewsdemo.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhiyuan3g.daynewsdemo.R;
import com.zhiyuan3g.daynewsdemo.db.SCDB;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by kkkkk on 2016/10/14.
 */
public class DetialActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView webView;
    private ImageView img_back, img_share, img_sc;
    private String title, url, author_name, thumbnail_pic_s;
    private SCDB scdb;
    private SQLiteDatabase writeDB, readDB;
    private Cursor cursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detial);
        initDB();
        initView();
        initSC();
        ShareSDK.initSDK(this);
    }

    private void initSC() {
        cursor = readDB.rawQuery("SELECT * FROM sc where title = ?", new String[]{title});
        if (cursor.getCount()!=0) {
            img_sc.setImageResource(R.drawable.sc_true);
        }
        cursor = null;
    }

    private void initDB() {
        scdb = new SCDB(this);
        writeDB = scdb.getWritableDatabase();
        readDB = scdb.getReadableDatabase();
    }

    private void initView() {
        img_back = (ImageView) findViewById(R.id.img_datial_back);
        img_share = (ImageView) findViewById(R.id.img_detial_share);
        img_sc = (ImageView) findViewById(R.id.img_datial_sc);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        author_name = intent.getStringExtra("author_name");
        thumbnail_pic_s = intent.getStringExtra("thumbnail_pic_s");
        webView = (WebView) findViewById(R.id.webView_detial);
        webView.loadUrl(url);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        img_back.setOnClickListener(this);
        img_share.setOnClickListener(this);
        img_sc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_datial_back:
                finish();
                break;
            case R.id.img_detial_share:
                showShare();
                break;
            case R.id.img_datial_sc:
                cursor = readDB.rawQuery("SELECT * FROM sc where title = ?", new String[]{title});
                if (cursor.getCount()!=0) {
                    img_sc.setImageResource(R.drawable.sc_false);
                    writeDB.delete("sc", "title = ?", new String[]{title});
                    Toast.makeText(DetialActivity.this,"已取消收藏",Toast.LENGTH_LONG).show();
                } else {
                    img_sc.setImageResource(R.drawable.sc_true);
                    ContentValues cv = new ContentValues();
                    cv.put("title", title);
                    cv.put("url",url);
                    cv.put("author_name",author_name);
                    cv.put("thumbnail_pic_s",thumbnail_pic_s);
                    writeDB.insert("sc", null, cv);
//                    String sql="insert into sc(title,url,author_name,thumbnail_pic_s) values('title','url','author_name','thumbail_pic_s')";
//                    writeDB.execSQL(sql);
                    Toast.makeText(DetialActivity.this,"收藏成功",Toast.LENGTH_LONG).show();
                }
                cursor = null;
                break;
        }
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
//关闭sso授权
        oks.disableSSOWhenAuthorize();

// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(title);
// titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(url);
// text是分享文本，所有平台都需要这个字段
        oks.setText("");
        oks.setImageUrl(thumbnail_pic_s);
// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
// url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
// comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("");
// site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(author_name);
// siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

// 启动分享GUI
        oks.show(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scdb.close();
    }
}

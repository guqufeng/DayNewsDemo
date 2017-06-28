package com.zhiyuan3g.daynewsdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kkkkk on 2016/10/14.
 */
public class SCDB extends SQLiteOpenHelper {
    public SCDB(Context context) {
        super(context, "sc.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS sc(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "url TEXT NOT NULL," +
                "author_name TEXT NOT NULL," +
                "thumbnail_pic_s TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS mydate(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "thumbnail_pic_s TEXT NOT NULL," +
                "title TEXT NOT NULL," +
                "author_name TEXT NOT NULL," +
                "url TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS myshehui(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "thumbnail_pic_s TEXT NOT NULL," +
                "title TEXT NOT NULL," +
                "author_name TEXT NOT NULL," +
                "url TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS myguonei(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "thumbnail_pic_s TEXT NOT NULL," +
                "title TEXT NOT NULL," +
                "author_name TEXT NOT NULL," +
                "url TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS myguoji(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "thumbnail_pic_s TEXT NOT NULL," +
                "title TEXT NOT NULL," +
                "author_name TEXT NOT NULL," +
                "url TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS myjunshi(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "thumbnail_pic_s TEXT NOT NULL," +
                "title TEXT NOT NULL," +
                "author_name TEXT NOT NULL," +
                "url TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS myyule(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "thumbnail_pic_s TEXT NOT NULL," +
                "title TEXT NOT NULL," +
                "author_name TEXT NOT NULL," +
                "url TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS mytiyu(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "thumbnail_pic_s TEXT NOT NULL," +
                "title TEXT NOT NULL," +
                "author_name TEXT NOT NULL," +
                "url TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

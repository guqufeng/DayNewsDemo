package com.zhiyuan3g.daynewsdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.zhiyuan3g.daynewsdemo.R;

import java.util.List;
import java.util.Map;

/**
 * Created by kkkkk on 2016/10/13.
 */
public class MyNewsTopAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> list;
    private ImageLoader imageLoader;

    public MyNewsTopAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue,new BitMapCache());

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolders holders;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_top_item, null);
            holders = new ViewHolders();
            holders.tv_top_title_item = (TextView) convertView.findViewById(R.id.tv_top_title_item);
            holders.tv_top_date_item = (TextView) convertView.findViewById(R.id.tv_top_date_item);
            holders.img_top_item = (ImageView) convertView.findViewById(R.id.img_top_item);
            convertView.setTag(holders);
        } else {
            holders = (ViewHolders) convertView.getTag();
        }
        holders.tv_top_title_item.setText((CharSequence) list.get(position).get("title"));
        holders.tv_top_date_item.setText((CharSequence) list.get(position).get("author_name"));
        ImageLoader.ImageListener imageListener = imageLoader.getImageListener(holders.img_top_item,R.drawable.ic_launcher,R.drawable.ic_launcher);
        imageLoader.get(String.valueOf(list.get(position).get("thumbnail_pic_s")),imageListener);

        return convertView;
    }

    private class ViewHolders {
        TextView tv_top_title_item;
        TextView tv_top_date_item;
        ImageView img_top_item;
    }

    public class BitMapCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> mCache;

        public BitMapCache() {
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String s) {
            return mCache.get(s);
        }

        @Override
        public void putBitmap(String s, Bitmap bitmap) {
            mCache.put(s,bitmap);
        }
    }
}

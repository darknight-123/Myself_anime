package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

public class SaveAdapter extends BaseAdapter {
    private LayoutInflater mLayInf;
    List<Map<String, Object>> mItemList;
    public SaveAdapter(Context context, List<Map<String, Object>> itemList)
    {
        mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemList = itemList;
    }

    @Override
    public int getCount()
    {
        //取得 ListView 列表 Item 的數量
        return mItemList.size();
    }

    @Override
    public Object getItem(int position)
    {
        //取得 ListView 列表於 position 位置上的 Item
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        //取得 ListView 列表於 position 位置上的 Item 的 ID
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //設定與回傳 convertView 作為顯示在這個 position 位置的 Item 的 View。
        View v = mLayInf.inflate(R.layout.save_view_item, parent, false);


        TextView txtView = (TextView) v.findViewById(R.id.save_title);
        TextView ep=v.findViewById(R.id.current_ep);
        TextView watch=v.findViewById(R.id.status);

        //imgView.setImageResource(Integer.valueOf(mItemList.get(position).get("Item icon").toString()));
        txtView.setText(mItemList.get(position).get("Item title").toString());
        ep.setText(ep.getText()+mItemList.get(position).get("Item ep").toString());
        watch.setText(watch.getText()+mItemList.get(position).get("Item status").toString());
        return v;
    }
}

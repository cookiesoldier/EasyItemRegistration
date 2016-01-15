package com.example.martinvieth.easyitemregistration;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Martin Vieth on 14-01-2016.
 */
public class customListAdapter extends BaseAdapter {
    private ArrayList listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public customListAdapter(Context context, ArrayList listData) {
        this.listData = listData;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new ViewHolder();
            holder.headlineView = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.headlineView.setText(listData.get(position).toString().substring(0, listData.get(position).toString().lastIndexOf(" ")));
        String url = listData.get(position).toString().substring(listData.get(position).toString().lastIndexOf(" "), listData.get(position).toString().length());
        url = url.substring(1);
        if (!url.contains("null")) {
            //Log.d("nrHeadUrl data--->", url);
            Picasso.with(context).load(url).placeholder(R.drawable.ic_placeholder).fit().into(holder.imageView);
        }


        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        ImageView imageView;
    }
}
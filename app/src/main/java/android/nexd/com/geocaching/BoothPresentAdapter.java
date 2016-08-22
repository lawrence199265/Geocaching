package android.nexd.com.geocaching;

import android.content.Context;
import android.nexd.com.geocaching.model.BoothMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxu on 16/8/8.
 */
public class BoothPresentAdapter extends BaseAdapter {
    private static final String TAG = "BoothPresentAdapter";
    List<BoothMessage> lists;
    Context context;

    public BoothPresentAdapter(Context context) {
        this.context = context;
        this.lists = new ArrayList<>();
    }

    public void setLists(List<BoothMessage> boothMessages) {
        lists.clear();
        Log.d(TAG, "setLists: " + boothMessages.size());
        lists.addAll(boothMessages);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public BoothMessage getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);

            viewHolder.boothName = (TextView) convertView.findViewById(R.id.boothName);
            viewHolder.boothPresent = (TextView) convertView.findViewById(R.id.boothPresent);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.boothName.setText(getItem(position).getBoothName());
        viewHolder.boothPresent.setText(getItem(position).getBoothPresent());
        return convertView;
    }


    static class ViewHolder {
        TextView boothName;
        TextView boothPresent;
    }
}

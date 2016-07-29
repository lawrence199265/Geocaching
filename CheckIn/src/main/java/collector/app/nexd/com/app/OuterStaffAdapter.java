package collector.app.nexd.com.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import collector.app.nexd.com.app.model.StaffModel;

/**
 * Created by wangxu on 16/7/29.
 */
public class OuterStaffAdapter extends BaseAdapter {
    private List<StaffModel> exitStaffs;
    private Context context;

    public OuterStaffAdapter(Context context, List<StaffModel> outerLists) {
        this.context = context;
        this.exitStaffs = outerLists;
    }

    @Override
    public int getCount() {
        return exitStaffs.size();
    }

    @Override
    public StaffModel getItem(int position) {
        return exitStaffs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(android.R.layout.test_list_item, null);
            holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(getItem(position).getBeaconName());
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }
}

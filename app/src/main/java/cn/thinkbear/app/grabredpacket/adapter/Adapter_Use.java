package cn.thinkbear.app.grabredpacket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;
import cn.thinkbear.app.grabredpacket.R;

/**
 *
 * @author ThinkBear
 */

public class Adapter_Use extends BaseAdapter {
    private LayoutInflater li = null;
    private List<String> all = null;


    public Adapter_Use(Context context, List<String> all){
        this.li = LayoutInflater.from(context);
        this.all = all;
    }

    @Override
    public int getCount() {
        return this.all.size();
    }

    @Override
    public Object getItem(int position) {
        return this.all.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = li.inflate(R.layout.adapter_use, parent,false);
            holder = new ViewHolder();
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.num = (TextView) convertView
                    .findViewById(R.id.num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.num.setText(String.valueOf(position+1));
        holder.content.setText(this.all.get(position));
        return convertView;
    }

    private final class ViewHolder {
        private TextView num = null;
        private TextView content = null;
    }

}

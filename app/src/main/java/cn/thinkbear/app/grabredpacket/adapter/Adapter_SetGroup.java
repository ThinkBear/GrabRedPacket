package cn.thinkbear.app.grabredpacket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import cn.thinkbear.app.grabredpacket.R;

/**
 *
 * @author ThinkBear
 */
public class Adapter_SetGroup extends BaseAdapter {
    private LayoutInflater li = null;
    private List<String> all = null;

    private OnCallback onCallback = null;

    public void setOnCallback(OnCallback onCallback) {
        this.onCallback = onCallback;
    }

    public Adapter_SetGroup(Context context, List<String> all){
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
        final String groupNameStr = this.all.get(position);
        if (convertView == null) {
            convertView = li.inflate(R.layout.adapter_set_group, parent,false);
            holder = new ViewHolder();
            holder.delete = (ImageView) convertView.findViewById(R.id.delete);
            holder.groupName = (TextView) convertView
                    .findViewById(R.id.groupName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.groupName.setText(groupNameStr);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCallback != null){
                    onCallback.deleteEvent(groupNameStr);
                }
            }
        });
        return convertView;
    }

    private final class ViewHolder {
        private ImageView delete = null;
        private TextView groupName = null;
    }

    public interface OnCallback{
        public void deleteEvent(String groupNameStr);
    }
}

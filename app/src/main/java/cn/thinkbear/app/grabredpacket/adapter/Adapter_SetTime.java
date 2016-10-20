package cn.thinkbear.app.grabredpacket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.thinkbear.app.grabredpacket.R;
import cn.thinkbear.app.grabredpacket.utils.Utils_Time;
import cn.thinkbear.app.grabredpacket.vo.Time;

import java.util.List;

/**
 *
 * @author ThinkBear
 */

public class Adapter_SetTime extends BaseAdapter {
    private LayoutInflater li = null;
    private List<Time> all = null;

    private OnCallback onCallback = null;

    public void setOnCallback(OnCallback onCallback) {
        this.onCallback = onCallback;
    }

    public Adapter_SetTime(Context context, List<Time> all){
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
        final Time t = this.all.get(position);
        if (convertView == null) {
            convertView = li.inflate(R.layout.adapter_set_time, parent, false);
            holder = new ViewHolder();
            holder.delete = (ImageView) convertView.findViewById(R.id.delete);
            holder.start = (TextView) convertView
                    .findViewById(R.id.start);
            holder.end = (TextView) convertView.findViewById(R.id.end);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.start.setText(Utils_Time.formatTimeToString(t.getStart()));
        holder.end.setText(Utils_Time.formatTimeToString(t.getEnd()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCallback != null){
                    onCallback.deleteEvent(t);
                }
            }
        });
        return convertView;
    }

    private final class ViewHolder {
        private ImageView delete = null;
        private TextView start = null;
        private TextView end = null;
    }

    public interface OnCallback{
        public void deleteEvent(Time t);
    }
}

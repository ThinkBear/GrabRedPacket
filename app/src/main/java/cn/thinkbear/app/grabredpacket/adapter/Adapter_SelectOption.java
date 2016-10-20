package cn.thinkbear.app.grabredpacket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.thinkbear.app.grabredpacket.R;

/**
 *
 * @author ThinkBear
 */
public class Adapter_SelectOption extends BaseAdapter {

    private int items[] = null;
    private int selectIndex = 0;
    private LayoutInflater li = null;
    public Adapter_SelectOption(Context context, int items[], int selectIndex){
        this.li = LayoutInflater.from(context);
        this.items = items;
        this.selectIndex = selectIndex;
    }

    @Override
    public int getCount() {
        return this.items.length;
    }

    @Override
    public Object getItem(int i) {
        return this.items[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = li.inflate(R.layout.adapter_select_option, viewGroup, false);
            holder = new ViewHolder();
            holder.flag = (ImageView) view.findViewById(R.id.flag);
            holder.content = (TextView) view
                    .findViewById(R.id.content);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.content.setText(this.items[i]);
        holder.flag.setVisibility(this.selectIndex == i ? View.VISIBLE:View.INVISIBLE);

        return view;
    }

    private final class ViewHolder {
        private ImageView flag = null;
        private TextView content = null;

    }
}

package uk.co.ribot.androidboilerplate.ui.adapter;


import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;

import uk.co.ribot.androidboilerplate.R;

public class TypeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private int checked;
    private List<String> data;
    public boolean fromClick;
    private String typeStr;
    private String category;
    private HashMap<String, Long> badges = new HashMap<>();

    public TypeAdapter(@Nullable List<String> data, String category) {
        super(R.layout.item_subcategory, data);
        this.data = data;
        this.category = category;
        if (data != null && data.size() > 0) {
            typeStr = data.get(0);
        }
    }

    public void updateBadge(HashMap<String, Long> badges) {
        this.badges = badges;
        notifyDataSetChanged();
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name, item)
                .setTag(R.id.item_main, item);
        if (helper.getAdapterPosition() == checked) {
            helper.setBackgroundColor(R.id.item_main, Color.WHITE)
                    .setTextColor(R.id.tv_name, ContextCompat.getColor(mContext, R.color.tab_text_on_color))
                    .setTypeface(R.id.tv_name, Typeface.DEFAULT_BOLD)
            ;
        } else {
            helper.setBackgroundColor(R.id.item_main, ContextCompat.getColor(mContext, R.color.type_gray))
                    .setTextColor(R.id.tv_name, ContextCompat.getColor(mContext, R.color.type_normal))
                    .setTypeface(R.id.tv_name, Typeface.DEFAULT)
            ;
        }
        String key = category+"&"+item;
        if (badges != null){
            if (badges.containsKey(key) && badges.get(key) > 0) {
                helper.setVisible(R.id.item_badge, true).setText(R.id.item_badge, String.valueOf(badges.get(key)));
            } else {
                helper.setVisible(R.id.item_badge, false);
            }
        }

    }

    public void setChecked(int checked) {
        this.checked = checked;
        if (data != null){
            typeStr = data.get(checked);
        }
        notifyDataSetChanged();
    }

    public void setType(String type) {
        if (fromClick) {
            fromClick = !type.equals(typeStr);
            return;
        }
        if (type.equals(typeStr)) {
            return;
        }
        if (data != null){
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).equals(type) && i != checked) {
                    setChecked(i);
                    moveToPosition(i);
                    break;
                }
            }
        }
    }

    private void moveToPosition(int i) {
        LinearLayoutManager linlm = (LinearLayoutManager) getRecyclerView().getLayoutManager();
        int firstItem = linlm.findFirstVisibleItemPosition();
        int lastItem = linlm.findLastVisibleItemPosition();
        if (getItemCount() > 5) {//提前把9滑出来
            lastItem -= 3;
        }
        if (i <= firstItem) {
            getRecyclerView().scrollToPosition(i);
        } else if (i <= lastItem) {
            //当要置顶的项已经在屏幕上显示时不处理
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            getRecyclerView().scrollToPosition(i);
            int n = i - linlm.findFirstVisibleItemPosition();
            if (0 <= n && n < getRecyclerView().getChildCount()) {
                int top = getRecyclerView().getChildAt(n).getTop();
                getRecyclerView().smoothScrollBy(0, top);
            }
        }


    }

}

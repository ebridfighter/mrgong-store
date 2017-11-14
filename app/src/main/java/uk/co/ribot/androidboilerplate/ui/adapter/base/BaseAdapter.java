package uk.co.ribot.androidboilerplate.ui.adapter.base;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mike on 2017/11/8.
 */

public abstract class BaseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    @Override
    public abstract T onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(T holder, int position);

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    protected OnItemClickListener mOnItemClickListener = null;

    protected void setOnItemListener(View itemView, final int position){
        itemView.setTag(position);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(view, (Integer) view.getTag());
                }
            }
        });
    }

    protected Resources getResource(T holder){
       return holder.itemView.getContext().getResources();
    }

    protected View getLayout(Context context, int layoutId){
        return LayoutInflater.from(context).inflate(layoutId,null);
    }



}

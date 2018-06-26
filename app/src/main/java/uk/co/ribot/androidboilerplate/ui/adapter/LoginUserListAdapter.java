package uk.co.ribot.androidboilerplate.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.database.UserBean;
import uk.co.ribot.androidboilerplate.ui.adapter.base.BaseAdapter;

public class LoginUserListAdapter extends BaseAdapter<LoginUserListAdapter.ViewHolder> {

    List<UserBean> mUserBeans = new ArrayList<>();

    public LoginUserListAdapter(List<UserBean> userBeans){
        mUserBeans.addAll(userBeans);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayout(parent.getContext(),R.layout.item_login_user);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserBean userBean = mUserBeans.get(position);
        holder.mUserName.setText(userBean.getUserName());
        setOnChildItemListener(holder.mDeleteImg,position);
        setOnItemListener(holder.itemView,position);
    }

    @Override
    public int getItemCount() {
        return mUserBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.userName)
        TextView mUserName;
        @BindView(R.id.deleteImg)
        ImageView mDeleteImg;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}

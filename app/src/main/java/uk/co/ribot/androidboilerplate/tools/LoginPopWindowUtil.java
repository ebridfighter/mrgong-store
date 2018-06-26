package uk.co.ribot.androidboilerplate.tools;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import javax.inject.Inject;

import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.ui.adapter.LoginUserListAdapter;
import uk.co.ribot.androidboilerplate.ui.adapter.base.BaseAdapter;

/**
 * Created by myChaoFile on 16/5/19.
 */
public class LoginPopWindowUtil {
    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private int popWidth, popHeight;
    private BaseAdapter.OnItemClickListener mOnItemClickListener;
    private BaseAdapter.OnChildItemClickListener mOnChildItemClickListener;
    private boolean hideSweetOption;
    private float preWid;

    private LoginUserListAdapter remListAdapter;

    public LoginPopWindowUtil(Context context) {
        inflater = LayoutInflater.from(context);
        Resources resources = context.getResources();
//        popWidth = (int) (GlobalConstant.screenW - resources.getDimension(R.dimen.classcircle_item_img_left) + resources.getDimension(R.dimen.classcircle_item_img_right));
        popHeight = (int) (resources.getDimension(R.dimen.classcircle_option_layout_hieght));
    }

    public void setAdapter(LoginUserListAdapter remListAdapter) {
        this.remListAdapter = remListAdapter;
    }

    private void initPop() {
        View layout = inflater.inflate(R.layout.list_login_user, null);
        if (hideSweetOption) {
            preWid = 0.6f;
        } else {
            preWid = 0.7f;
        }
        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, popHeight, false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        popupWindow.setAnimationStyle(R.style.classActionPopStyle);
//        classActionZanLayout =  layout.findViewById(R.id.classActionZanLayout);
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(layout.getContext()));
        recyclerView.setAdapter(remListAdapter);
//        classActionZanLayout.setTag(zanView);
        if (mOnItemClickListener != null) {
            remListAdapter.setOnItemClickListener(mOnItemClickListener);
        }
        if (mOnChildItemClickListener != null) {
            remListAdapter.setOnChildItemClickListener(mOnChildItemClickListener);
        }
    }

    /**
     * 为按钮添加事件
     *
     * @param listener
     */
    public void addOnItemClickListener(BaseAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void addOnChildItemClickListener(BaseAdapter.OnChildItemClickListener listener) {
        mOnChildItemClickListener = listener;
    }

    public void showPop(View parentView, final ImageView imageView) {
        initPop();
        if (!popupWindow.isShowing()) {
            popupWindow.showAsDropDown(parentView, 0, 0);
            imageView.setImageResource(R.drawable.login_btn_dropup);
        } else {
            popupWindow.dismiss();
        }
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                imageView.setImageResource(R.drawable.login_btn_dropdown);
            }
        });

    }

    public void hidePop() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
}


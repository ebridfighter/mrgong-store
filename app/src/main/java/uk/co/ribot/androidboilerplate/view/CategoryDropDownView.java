package uk.co.ribot.androidboilerplate.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;

import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.tools.DensityUtil;

/**
 * 对下拉类别View的封装
 *
 * Created by Dong on 2017/11/15.
 */

public class CategoryDropDownView extends FrameLayout {
    private static final int TAB_EXPAND_COUNT = 4;

    protected int mScreenHeight;
    protected TabLayout mTabLayout;
    protected ProductTypePopup mPopup;//选择类别popup
    protected ViewPager mViewPager;//绑定的viewpager
    protected ImageView mIvOpen;
    protected List<String> mTitles;
    protected TabLayout.OnTabSelectedListener mTabListener;

    public CategoryDropDownView(@NonNull Context context) {
        super(context);
        init();
    }

    public CategoryDropDownView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CategoryDropDownView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.view_category_tabs,this);
        mTabLayout = (TabLayout)findViewById(R.id.tl_tabs);
        mIvOpen = (ImageView)findViewById(R.id.iv_open_dropdown_tab);
    }

    /**
     * 初始化弹窗
     */
    protected void initPopup(){
        final int[] location = new int[2];
        mTabLayout.getLocationOnScreen(location);
        int y = location[1] + mTabLayout.getHeight();
        mPopup = new ProductTypePopup(getContext(),
                ViewGroup.LayoutParams.MATCH_PARENT,
                mScreenHeight - y,
                mTitles,0);
        mPopup.setViewPager(mViewPager);
        mPopup.setOnDismissListener(()->mIvOpen.setImageResource(R.drawable.arrow));
    }

    public void setOnTabSelectedListener(TabLayout.OnTabSelectedListener listener){
        mTabListener = listener;
    }

    public void setup(Activity activity, ViewPager viewPager, List<String> titles){
        mScreenHeight = DensityUtil.getScreenH(activity);
        mTitles = titles;
        mViewPager = viewPager;
        mTabLayout.removeAllTabs();
        mTabLayout.setupWithViewPager(viewPager);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //转换tab
                int position = tab.getPosition();
                mViewPager.setCurrentItem(position);
                mPopup.dismiss();
                if(mTabListener!=null)mTabListener.onTabSelected(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(mTabListener!=null)mTabListener.onTabUnselected(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(mTabListener!=null)mTabListener.onTabReselected(tab);
            }
        });

        if(mTitles.size()<=TAB_EXPAND_COUNT){
            mIvOpen.setVisibility(View.GONE);
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        }

        initPopup();

        mIvOpen.setOnClickListener(v->{
            if (mPopup == null){
                return;
            }
            if (!mPopup.isShowing()){
                showPopWindow();
            }else{
                mPopup.dismiss();
            }
        });
    }

    /**
     * 打开弹窗
     */
    private void showPopWindow(){
        final int[] location = new int[2];
        mTabLayout.getLocationOnScreen(location);
        int y = location[1] + mTabLayout.getHeight();
        mPopup.showAtLocation(this, Gravity.NO_GRAVITY,0,y);
        mPopup.setSelect(mViewPager.getCurrentItem());
        mIvOpen.setImageResource(R.drawable.arrow_up);
    }
}

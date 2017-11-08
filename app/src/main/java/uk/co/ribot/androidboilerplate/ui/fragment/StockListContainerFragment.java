package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.tools.DensityUtil;
import uk.co.ribot.androidboilerplate.ui.activity.StockSearchActivity;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.StockListContainerPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.StockListContainerMvpView;
import uk.co.ribot.androidboilerplate.view.ProductTypePopup;
import uk.co.ribot.androidboilerplate.view.SystemUpgradeLayout;

import static uk.co.ribot.androidboilerplate.ui.fragment.AbstractStockListFragment.ARG_CATEGORY;
import static uk.co.ribot.androidboilerplate.ui.fragment.StockListFragment.ARG_CURRENT;

/**
 *
 * todo:1.搜索；
 * todo:2.更新库存时刷新；
 * todo:3.用户登录时刷新；
 *
 * Created by Dong on 2017/11/6.
 */

public class StockListContainerFragment extends BaseFragment implements StockListContainerMvpView {
    private static final int TAB_EXPAND_COUNT = 4;
    private View mRootView;
    @BindView(R.id.indicator)
    TabLayout smartTabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.iv_open)
    ImageView ivOpen;
    @BindView(R.id.layout_system_upgrade_notice)
    SystemUpgradeLayout mLayoutUpgradeNotice;
    @Inject
    StockListContainerPresenter mStockListContainerPresenter;
    AbstractStockListFragment mCurrentFragment;//当前显示的类别fragment

    private TabPageIndicatorAdapter adapter;
    private ProductTypePopup mTypeWindow;//商品类型弹出框

    private String mKeyword = "";
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_stock_list_container,container,false);
        unbinder = ButterKnife.bind(this,mRootView);
        mLayoutUpgradeNotice.setPageName("盘点功能");
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFragmentBaseComponent
                .stockListContainerFragmentComponent(new ActivityModule(getActivity()))
                .inject(this);
        mStockListContainerPresenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mStockListContainerPresenter.detachView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_open})
    public void openTypeWindow(View view) {
        if (mTypeWindow == null){
            return;
        }
        if (!mTypeWindow.isShowing()){
            showPopWindow();
        }else{
            mTypeWindow.dismiss();
        }
    }

    @Override
    public void showCategories(List<String> categoryList) {
        List<AbstractStockListFragment> repertoryEntityFragmentList = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        titles.add("全部");
        AbstractStockListFragment allStockListFragment = newRepertoryListFragment("");
        allStockListFragment.getArguments().putBoolean(ARG_CURRENT,true);//全部为打开tab
        repertoryEntityFragmentList.add(0, allStockListFragment);

        for(String category:categoryList){
            titles.add(category);
            repertoryEntityFragmentList.add(newRepertoryListFragment(category));
        }

        initUI(titles,repertoryEntityFragmentList);
        initTypeWindow((ArrayList<String>) titles);
    }

    private void initTypeWindow(ArrayList<String> typeList) {
        final int[] location = new int[2];
        smartTabLayout.getLocationOnScreen(location);
        int y = (int) (location[1] + smartTabLayout.getHeight());
        mTypeWindow = new ProductTypePopup(getActivity(),
                ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtil.getScreenH(getActivity()) - y,
                typeList,0);
        mTypeWindow.setViewPager(viewPager);
        mTypeWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivOpen.setImageResource(R.drawable.arrow);
            }
        });
    }

    private void showPopWindow(){
        final int[] location = new int[2];
        smartTabLayout.getLocationOnScreen(location);
        int y = (int) (location[1] + smartTabLayout.getHeight());
//        mProductTypeWindow.showAtLocation(mainView, Gravity.NO_GRAVITY, 0, y);
//        mProductTypeAdapter.setSelectIndex(viewPager.getCurrentItem());
        mTypeWindow.showAtLocation(mRootView, Gravity.NO_GRAVITY,0,y);
        mTypeWindow.setSelect(viewPager.getCurrentItem());
        ivOpen.setImageResource(R.drawable.arrow_up);
    }

    private void initUI(List<String> titles, List<AbstractStockListFragment> repertoryEntityFragmentList){
        adapter = new TabPageIndicatorAdapter(this.getActivity().getSupportFragmentManager(),titles,repertoryEntityFragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(repertoryEntityFragmentList.size());
        smartTabLayout.removeAllTabs();
        smartTabLayout.setupWithViewPager(viewPager);
        smartTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
                mTypeWindow.dismiss();

                //刷新当前fragment
                AbstractStockListFragment fragment = adapter.getFragmentList().get(position);
                fragment.refresh(mKeyword);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if(titles.size()<=TAB_EXPAND_COUNT){
            ivOpen.setVisibility(View.GONE);
            smartTabLayout.setTabMode(TabLayout.MODE_FIXED);
            ((ViewGroup.MarginLayoutParams)smartTabLayout.getLayoutParams()).setMargins(0,0,0,0);
        }else{
            ivOpen.setVisibility(View.VISIBLE);
            smartTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }


    public AbstractStockListFragment newRepertoryListFragment(String category) {
        AbstractStockListFragment repertoryListFragment;
        if(getActivity() instanceof StockSearchActivity){
            repertoryListFragment = new StockListSearchFragment();//搜索
        }else{
            repertoryListFragment = new StockListFragment();//正常显示
        }
        Bundle bundle = new Bundle();
        bundle.putString(ARG_CATEGORY,category);
        repertoryListFragment.setArguments(bundle);
        return repertoryListFragment;
    }

    /**
     * tab的adapter
     */
    private class TabPageIndicatorAdapter extends FragmentStatePagerAdapter {
        private List<String> titleList = new ArrayList<>();
        private List<AbstractStockListFragment> fragmentList = new ArrayList<>();
        public TabPageIndicatorAdapter(FragmentManager fm, List<String> titles, List<AbstractStockListFragment> repertoryEntityFragmentList) {
            super(fm);
            titleList = titles;
            fragmentList.addAll(repertoryEntityFragmentList);
        }
        public List<AbstractStockListFragment> getFragmentList() {
            return  fragmentList;
        }
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        @Override
        public int getCount() {
            return titleList.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mCurrentFragment = fragmentList.get(position);
            super.setPrimaryItem(container, position, object);
        }
    }
}

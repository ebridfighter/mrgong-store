package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.injection.component.OrderListActivityComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.tools.TimeUtils;
import uk.co.ribot.androidboilerplate.ui.adapter.FragmentAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.fragment.OrderListFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.OrderListPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.OrderListMvpView;

import static uk.co.ribot.androidboilerplate.ui.fragment.OrderListFragment.ARGUMENT_KEY_END_TIME;
import static uk.co.ribot.androidboilerplate.ui.fragment.OrderListFragment.ARGUMENT_KEY_START_TIME;


/**
 * Created by mike on 2017/11/22.
 */
public class OrderListActivity extends BaseActivity implements OrderListMvpView {
    @Inject
    OrderListPresenter mOrderListPresenter;
    @BindView(R.id.tl)
    TabLayout mTl;
    @BindView(R.id.vp)
    ViewPager mVp;

    List<Fragment> mFragmentList;
    List<String> mTitleList;

    public static final int ORDER_TIME_ALL = 1 << 0;
    public static final int ORDER_TIME_BENZHOU = 1 << 1;
    public static final int ORDER_TIME_SHANGZHOU = 1 << 2;
    public static final int ORDER_TIME_GENGZAO = 1 << 3;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, OrderListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        setTitle(R.string.title_order_list);
        showBackBtn();
        OrderListActivityComponent activityComponent = configPersistentComponent.orderListActivityComponent(new ActivityModule(this));
        activityComponent.inject(this);
        mOrderListPresenter.attachView(this);
        setUpFragmentList();
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),mTitleList,mFragmentList);
        mVp.setAdapter(fragmentAdapter);
        mVp.setOffscreenPageLimit(mFragmentList.size());
        mTl.setupWithViewPager(mVp);
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mFragmentList.size(); i++) {
                    if (i != position) {
                        mFragmentList.get(i).setUserVisibleHint(false);
                    } else {
                        mFragmentList.get(i).setUserVisibleHint(true);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private OrderListFragment newOrderListFragment(int type) {
        OrderListFragment orderListFragment = new OrderListFragment();
        Bundle bundle = new Bundle();
        switch (type) {
            case ORDER_TIME_ALL:
                break;
            case ORDER_TIME_BENZHOU:
                bundle.putString(ARGUMENT_KEY_START_TIME, TimeUtils.getThisWeekStart());
                bundle.putString(ARGUMENT_KEY_END_TIME, TimeUtils.getThisWeekEnd());
                break;
            case ORDER_TIME_SHANGZHOU:
                bundle.putString(ARGUMENT_KEY_START_TIME, TimeUtils.getPerWeekStart());
                bundle.putString(ARGUMENT_KEY_END_TIME, TimeUtils.getPerWeekEnd());
                break;
            case ORDER_TIME_GENGZAO:
                bundle.putString(ARGUMENT_KEY_END_TIME, TimeUtils.getPerWeekStart());
                break;
        }
        orderListFragment.setArguments(bundle);
        return orderListFragment;
    }

    private void setUpFragmentList() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(newOrderListFragment(ORDER_TIME_ALL));
        mFragmentList.add(newOrderListFragment(ORDER_TIME_BENZHOU));
        mFragmentList.add(newOrderListFragment(ORDER_TIME_SHANGZHOU));
        mFragmentList.add(newOrderListFragment(ORDER_TIME_GENGZAO));

        mTitleList = new ArrayList<>();
        mTitleList.add(getString(R.string.fragment_title_order_all));
        mTitleList.add(getString(R.string.fragment_title_order_this_week));
        mTitleList.add(getString(R.string.fragment_title_order_last_week));
        mTitleList.add(getString(R.string.fragment_title_order_earlier));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrderListPresenter.detachView();
    }
}

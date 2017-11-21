package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.SyncService;
import uk.co.ribot.androidboilerplate.injection.component.MainActivityComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.MainFragmentAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.fragment.HomePageFragment;
import uk.co.ribot.androidboilerplate.ui.fragment.MessageFragment;
import uk.co.ribot.androidboilerplate.ui.fragment.MoreFragment;
import uk.co.ribot.androidboilerplate.ui.fragment.PlaceOrderFragment;
import uk.co.ribot.androidboilerplate.ui.fragment.StockFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.MainPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.MainMvpView;

public class MainActivity extends BaseActivity implements MainMvpView {

    @Inject
    MainPresenter mMainPresenter;
    @BindView(R.id.tl)
    TabLayout mTl;
    @BindView(R.id.vp)
    ViewPager mVp;
    @BindView(R.id.constraintLayout)
    RelativeLayout mConstraintLayout;
    @Inject
    HomePageFragment mHomePageFragment;
    @Inject
    PlaceOrderFragment mPlaceOrderFragment;
    @Inject
    StockFragment mStockFragment;
    @Inject
    MessageFragment mMessageFragment;
    @Inject
    MoreFragment mMoreFragment;

    List<Fragment> mFragmentList;
    List<String> mTitleList;

    public static final int FRAGMENT_COUNT = 5;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        hideTitleBar();
        MainActivityComponent activityComponent = configPersistentComponent.mainActivityComponent(new ActivityModule(this));
        activityComponent.inject(this);
        mMainPresenter.attachView(this);
        startService(SyncService.getStartIntent(this));

        setUpFragmentList();
        MainFragmentAdapter fragmentAdapter = new MainFragmentAdapter(getActivityContext(), getSupportFragmentManager(), mFragmentList);
        mVp.setAdapter(fragmentAdapter);
        mVp.setOffscreenPageLimit(mFragmentList.size());
        mTl.setupWithViewPager(mVp);
        for (int i = 0; i < FRAGMENT_COUNT; i++) {
            mTl.getTabAt(i).setCustomView(newTabView(i));
        }
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

    public View newTabView(int position) {
        View tabView = getLayout(R.layout.item_main_tab);
        ImageView imageView = (ImageView) tabView.findViewById(R.id.iv_tab);
        TextView textView = (TextView) tabView.findViewById(R.id.tv_tab);
        switch (position) {
            case 0:
                imageView.setImageResource(R.drawable.tab_1_selector);
                textView.setText(R.string.fragment_home_page);
                break;
            case 1:
                imageView.setImageResource(R.drawable.tab_2_selector);
                textView.setText(R.string.fragment_palce_order);
                break;
            case 2:
                imageView.setImageResource(R.drawable.tab_3_selector);
                textView.setText(R.string.fragment_stock);
                break;
            case 3:
                imageView.setImageResource(R.drawable.tab_4_selector);
                textView.setText(R.string.fragment_message);
                break;
            case 4:
                imageView.setImageResource(R.drawable.tab_5_selector);
                textView.setText(R.string.fragment_more);
                break;
        }
        return tabView;
    }

    private void setUpFragmentList() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(mHomePageFragment);
        mFragmentList.add(mPlaceOrderFragment);
        mFragmentList.add(mStockFragment);
        mFragmentList.add(mMessageFragment);
        mFragmentList.add(mMoreFragment);

        mTitleList = new ArrayList<>();
        mTitleList.add(getString(R.string.fragment_home_page));
        mTitleList.add(getString(R.string.fragment_palce_order));
        mTitleList.add(getString(R.string.fragment_stock));
        mTitleList.add(getString(R.string.fragment_message));
        mTitleList.add(getString(R.string.fragment_more));
    }

}

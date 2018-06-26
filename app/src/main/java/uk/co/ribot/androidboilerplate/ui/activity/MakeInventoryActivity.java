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
import uk.co.ribot.androidboilerplate.injection.component.MakeInventoryActivityComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.FragmentAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.fragment.MakeInventoryFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.MakeInventoryPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.MakeInventoryMvpView;

import static uk.co.ribot.androidboilerplate.ui.fragment.MakeInventoryFragment.BUNDLE_KEY_TYPE;
import static uk.co.ribot.androidboilerplate.ui.fragment.MakeInventoryFragment.TYPE_ALL;
import static uk.co.ribot.androidboilerplate.ui.fragment.MakeInventoryFragment.TYPE_BEN_ZHOU;
import static uk.co.ribot.androidboilerplate.ui.fragment.MakeInventoryFragment.TYPE_GENG_ZAO;
import static uk.co.ribot.androidboilerplate.ui.fragment.MakeInventoryFragment.TYPE_SHANG_ZHOU;


/**
 * Created by mike on 2017/11/28.
 */
public class MakeInventoryActivity extends BaseActivity implements MakeInventoryMvpView {
    @Inject
    MakeInventoryPresenter mMakeInventoryPresenter;
    @BindView(R.id.tl)
    TabLayout mTl;
    @BindView(R.id.vp)
    ViewPager mVp;

    List<Fragment> mFragmentList;
    List<String> mTitleList;

    public static final int MAKE_INVENTORY_TIME_ALL = 1 << 0;
    public static final int MAKE_INVENTORY_BENZHOU = 1 << 1;
    public static final int MAKE_INVENTORY_SHANGZHOU = 1 << 2;
    public static final int OMAKE_INVENTORY_GENGZAO = 1 << 3;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MakeInventoryActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_inventory);
        ButterKnife.bind(this);
        showBackBtn();
        setTitle(R.string.title_make_inventory);
        MakeInventoryActivityComponent activityComponent = configPersistentComponent.makeInventoryActivityComponent(new ActivityModule(this));
        activityComponent.inject(this);
        mMakeInventoryPresenter.attachView(this);
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

    private void setUpFragmentList() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(newMakeInventoryFragment(MAKE_INVENTORY_TIME_ALL));
        mFragmentList.add(newMakeInventoryFragment(MAKE_INVENTORY_BENZHOU));
        mFragmentList.add(newMakeInventoryFragment(MAKE_INVENTORY_SHANGZHOU));
        mFragmentList.add(newMakeInventoryFragment(OMAKE_INVENTORY_GENGZAO));

        mTitleList = new ArrayList<>();
        mTitleList.add(getString(R.string.fragment_title_order_all));
        mTitleList.add(getString(R.string.fragment_title_order_this_week));
        mTitleList.add(getString(R.string.fragment_title_order_last_week));
        mTitleList.add(getString(R.string.fragment_title_order_earlier));
    }

    private MakeInventoryFragment newMakeInventoryFragment(int type) {
        MakeInventoryFragment makeInventoryFragment = new MakeInventoryFragment();
        Bundle bundle = new Bundle();
        switch (type) {
            case MAKE_INVENTORY_TIME_ALL:
                bundle.putInt(BUNDLE_KEY_TYPE,TYPE_ALL);
                break;
            case MAKE_INVENTORY_BENZHOU:
                bundle.putInt(BUNDLE_KEY_TYPE,TYPE_BEN_ZHOU);
                break;
            case MAKE_INVENTORY_SHANGZHOU:
                bundle.putInt(BUNDLE_KEY_TYPE,TYPE_SHANG_ZHOU);
                break;
            case OMAKE_INVENTORY_GENGZAO:
                bundle.putInt(BUNDLE_KEY_TYPE,TYPE_GENG_ZAO);
                break;
        }
        makeInventoryFragment.setArguments(bundle);
        return makeInventoryFragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMakeInventoryPresenter.detachView();
    }
}

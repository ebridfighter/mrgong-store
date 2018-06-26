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
import uk.co.ribot.androidboilerplate.injection.component.ProcurementActivityComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.FragmentAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.fragment.ProcurementFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.ProcurementPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.ProcurementMvpView;

import static uk.co.ribot.androidboilerplate.data.model.net.request.ProcurementRequest.TYPE_ALL;
import static uk.co.ribot.androidboilerplate.data.model.net.request.ProcurementRequest.TYPE_EARLIER;
import static uk.co.ribot.androidboilerplate.data.model.net.request.ProcurementRequest.TYPE_LAST_WEEK;
import static uk.co.ribot.androidboilerplate.data.model.net.request.ProcurementRequest.TYPE_THIS_WEEK;
import static uk.co.ribot.androidboilerplate.ui.fragment.ProcurementFragment.ARGUMENT_KEY_TYPE;


/**
 * Created by mike on 2018/1/4.
 */
public class ProcurementActivity extends BaseActivity implements ProcurementMvpView {
    @Inject
    ProcurementPresenter mProcurementPresenter;
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
        Intent intent = new Intent(context, ProcurementActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurement);
        ButterKnife.bind(this);
        ProcurementActivityComponent activityComponent = configPersistentComponent.procurementActivityComponent(new ActivityModule(this));
        activityComponent.inject(this);
        mProcurementPresenter.attachView(this);
        showBackBtn();
        setTitle(getString(R.string.purchase_record));
        setUpFragmentList();
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mTitleList, mFragmentList);
        mVp.setAdapter(fragmentAdapter);
        mVp.setOffscreenPageLimit(mFragmentList.size());
        mTl.setupWithViewPager(mVp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProcurementPresenter.detachView();
    }

    private void setUpFragmentList() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(newProcurementFragment(TYPE_ALL));
        mFragmentList.add(newProcurementFragment(TYPE_THIS_WEEK));
        mFragmentList.add(newProcurementFragment(TYPE_LAST_WEEK));
        mFragmentList.add(newProcurementFragment(TYPE_EARLIER));

        mTitleList = new ArrayList<>();
        mTitleList.add(getString(R.string.fragment_title_order_all));
        mTitleList.add(getString(R.string.fragment_title_order_this_week));
        mTitleList.add(getString(R.string.fragment_title_order_last_week));
        mTitleList.add(getString(R.string.fragment_title_order_earlier));
    }

    private ProcurementFragment newProcurementFragment(int type) {
        ProcurementFragment procurementFragment = new ProcurementFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGUMENT_KEY_TYPE, type);
        procurementFragment.setArguments(bundle);
        return procurementFragment;
    }
}

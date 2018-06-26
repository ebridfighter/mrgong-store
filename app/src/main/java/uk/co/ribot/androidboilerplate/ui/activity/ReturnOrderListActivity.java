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
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnDataResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.injection.component.ReturnOrderListActivityComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.FragmentAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.fragment.ReturnOrderListFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.ReturnOrderListPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.ReturnOrderListMvpView;


/**
 * Created by mike on 2017/11/22.
 */
public class ReturnOrderListActivity extends BaseActivity implements ReturnOrderListMvpView {
    @Inject
    ReturnOrderListPresenter mReturnOrderListPresenter;
    @BindView(R.id.tl)
    TabLayout mTl;
    @BindView(R.id.vp)
    ViewPager mVp;
    UserInfoResponse mUserInfoResponse;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ReturnOrderListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_order_list);
        ButterKnife.bind(this);
        showBackBtn();
        setTitle(R.string.title_return_record);
        mTl.setTabMode(TabLayout.MODE_FIXED);
        ReturnOrderListActivityComponent activityComponent = configPersistentComponent.returnOrderListActivityComponent(new ActivityModule(this));
        activityComponent.inject(this);
        mReturnOrderListPresenter.attachView(this);
        showLoadingDialog();
        mReturnOrderListPresenter.getReturnOrderList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReturnOrderListPresenter.detachView();
    }

    private void setUpData(ReturnDataResponse returnDataResponse) {
        mUserInfoResponse = mReturnOrderListPresenter.loadUser();
        List<String> mTitles = new ArrayList<>();
        mTitles.add("全部(" + returnDataResponse.getAllList().size() + ")");
        mTitles.add("本周(" + returnDataResponse.getThisWeekList().size() + ")");
        mTitles.add("上周(" + returnDataResponse.getLastWeekList().size() + ")");
        mTitles.add("更早(" + returnDataResponse.getEarlierList().size() + ")");
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(newReturnOrderListFragment((ArrayList<ReturnOrderListResponse.ListBean>) returnDataResponse.getAllList()));
        fragments.add(newReturnOrderListFragment((ArrayList<ReturnOrderListResponse.ListBean>) returnDataResponse.getThisWeekList()));
        fragments.add(newReturnOrderListFragment((ArrayList<ReturnOrderListResponse.ListBean>) returnDataResponse.getLastWeekList()));
        fragments.add(newReturnOrderListFragment((ArrayList<ReturnOrderListResponse.ListBean>) returnDataResponse.getEarlierList()));
        FragmentAdapter adapter = new FragmentAdapter(this.getSupportFragmentManager(), mTitles, fragments);
        mVp.setAdapter(adapter);
        mVp.setOffscreenPageLimit(mTitles.size());
        mTl.setupWithViewPager(mVp);
    }

    private ReturnOrderListFragment newReturnOrderListFragment(ArrayList<ReturnOrderListResponse.ListBean> listBeans) {
        ReturnOrderListFragment returnOrderListFragment = new ReturnOrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ReturnOrderListFragment.BUNDLE_KEY_LIST, listBeans);
        bundle.putBoolean(ReturnOrderListFragment.BUNDLE_KEY_CAN_SEE_PRICE, mUserInfoResponse.isCanSeePrice());
        returnOrderListFragment.setArguments(bundle);
        return returnOrderListFragment;
    }


    @Override
    public void showReturnOrders(ReturnDataResponse returnDataResponse) {
        dismissLoadingDialog();
        setUpData(returnDataResponse);
    }

    @Override
    public void showReturnOrdersError(String error) {
        dismissLoadingDialog();
        toast(error);
    }
}

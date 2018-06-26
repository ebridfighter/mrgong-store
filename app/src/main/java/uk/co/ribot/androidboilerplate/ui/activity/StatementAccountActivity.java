package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.runwise.commomlibrary.swipetoloadlayout.RefreshRecyclerView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.StatementAccountListResponse;
import uk.co.ribot.androidboilerplate.injection.component.StatementAccountActivityComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.StatementAccountAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.presenter.StatementAccountPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.StatementAccountMvpView;


/**
 * Created by mike on 2017/11/28.
 */
public class StatementAccountActivity extends BaseActivity implements StatementAccountMvpView {
    @Inject
    StatementAccountPresenter mStatementAccountPresenter;
    @BindView(R.id.rv_account_list)
    RefreshRecyclerView mRvAccountList;
    @BindView(R.id.include_loading)
    View mIncludeLoading;
    @Inject
    StatementAccountAdapter mStatementAccountAdapter;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, StatementAccountActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement_account);
        ButterKnife.bind(this);
        showBackBtn();
        setTitle(R.string.title_statement_account);
        StatementAccountActivityComponent activityComponent = configPersistentComponent.statementAccountActivityComponent(new ActivityModule(this));
        activityComponent.inject(this);
        mStatementAccountPresenter.attachView(this);
        mRvAccountList.init(new LinearLayoutManager(getActivityContext()), this, null);
        mRvAccountList.setAdapter(mStatementAccountAdapter);
        mIncludeLoading.setVisibility(View.VISIBLE);
        mStatementAccountPresenter.getStatementAccountList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStatementAccountPresenter.detachView();
    }

    @Override
    public void showStatementAccountList(StatementAccountListResponse statementAccountListResponse) {
        mStatementAccountAdapter.setData(statementAccountListResponse.getBankStatement());
        mRvAccountList.setRefreshing(false);
        mIncludeLoading.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mStatementAccountPresenter.getStatementAccountList();
    }

    @Override
    public void showStatementAccountListError(String error) {
        toast(error);
        mIncludeLoading.setVisibility(View.GONE);
    }

    @Override
    public void showStatementAccountListEmpty() {
        toast(R.string.toast_no_data);
        mIncludeLoading.setVisibility(View.GONE);
    }
}

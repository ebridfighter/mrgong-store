package uk.co.ribot.androidboilerplate.ui.activity;

import android.os.Bundle;

import javax.inject.Inject;

import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.injection.component.ReturnOrderListActivityComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.presenter.ReturnOrderListPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.ReturnOrderListMvpView;


/**
 * Created by mike on 2017/11/22.
 */
public class ReturnOrderListActivity extends BaseActivity implements ReturnOrderListMvpView {
    @Inject
    ReturnOrderListPresenter mReturnOrderListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_order_list);
        ReturnOrderListActivityComponent activityComponent = configPersistentComponent.returnOrderListActivityComponent(new ActivityModule(this));
        activityComponent.inject(this);
        mReturnOrderListPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReturnOrderListPresenter.detachView();
    }
}

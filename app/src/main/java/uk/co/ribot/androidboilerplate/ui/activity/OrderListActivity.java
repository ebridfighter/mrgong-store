package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.injection.component.OrderListActivityComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.presenter.OrderListPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.OrderListMvpView;


/**
 * Created by mike on 2017/11/22.
 */
public class OrderListActivity extends BaseActivity implements OrderListMvpView {
    @Inject
    OrderListPresenter mOrderListPresenter;

    public static Intent getStartIntent(Context context){
        Intent intent = new Intent(context,OrderListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        setTitle(R.string.title_order_list);
        showBackBtn();
        OrderListActivityComponent activityComponent = configPersistentComponent.orderListActivityComponent(new ActivityModule(this));
        activityComponent.inject(this);
        mOrderListPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrderListPresenter.detachView();
    }
}

package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.SyncService;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.injection.component.MainActivityComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.ProductsAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.presenter.MainPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.MainMvpView;

public class MainActivity extends BaseActivity implements MainMvpView {


    @BindView(R.id.rv_product)
    RecyclerView mRvProduct;
    @Inject
    MainPresenter mMainPresenter;
    @Inject
    ProductsAdapter mProductsAdapter;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MainActivityComponent activityComponent = configPersistentComponent.mainActivityComponent(new ActivityModule(this));
        activityComponent.inject(this);

        mRvProduct.setAdapter(mProductsAdapter);
        mRvProduct.setLayoutManager(new LinearLayoutManager(this));

        mMainPresenter.attachView(this);
        mMainPresenter.loadProducts();
        startService(SyncService.getStartIntent(this));

    }

    @Override
    public void showProducts(List<ProductListResponse.Product> products) {
        mProductsAdapter.setRibots(products);
        mProductsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProductsEmpty() {

    }

    @Override
    public void showError() {

    }
}

package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.OvershootInterpolator;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.SyncService;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.injection.component.ProductListActivityComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.ProductsAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.presenter.ProductListPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.ProductListMvpView;

public class ProductListActivity extends BaseActivity implements ProductListMvpView {

    @BindView(R.id.rv_product)
    RecyclerView mRvProduct;
    @Inject
    ProductListPresenter mProductListPresenter;
    @Inject
    ProductsAdapter mProductsAdapter;

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context,ProductListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        showBackBtn();
        setTitle(R.string.title_activity_product_list);

        ProductListActivityComponent activityComponent = configPersistentComponent.productListActivityComponent(new ActivityModule(this));
        activityComponent.inject(this);

        mRvProduct.setItemAnimator(new SlideInLeftAnimator(new OvershootInterpolator(1f)));
        mRvProduct.getItemAnimator().setAddDuration(500);
        mRvProduct.getItemAnimator().setRemoveDuration(500);
        mRvProduct.setLayoutManager(new LinearLayoutManager(this));
        mRvProduct.setAdapter(mProductsAdapter);

        mProductListPresenter.attachView(this);
        mProductListPresenter.loadProducts();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

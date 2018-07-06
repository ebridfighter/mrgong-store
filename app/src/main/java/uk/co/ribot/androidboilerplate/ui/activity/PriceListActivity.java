package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.SyncService;
import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;
import uk.co.ribot.androidboilerplate.data.model.net.response.CategoryResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.injection.component.ProductListActivityComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.FragmentAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.fragment.PriceListFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.ProductListPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.ProductListMvpView;
import uk.co.ribot.androidboilerplate.view.CategoryDropDownView;

public class PriceListActivity extends BaseActivity implements ProductListMvpView {

    @Inject
    ProductListPresenter mProductListPresenter;

    int mSynFlag = 0;
    public static final int MAX_SYN_FLAG = 2;
    CategoryResponse mCategoryResponse;
    List<ProductBean> mProducts;
    FragmentAdapter mFragmentAdapter;
    @BindView(R.id.cddv)
    CategoryDropDownView mCddv;
    @BindView(R.id.vp)
    ViewPager mVp;
    UserInfoResponse mUserInfoResponse;


    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, PriceListActivity.class);
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
        mProductListPresenter.attachView(this);

        startService(SyncService.getStartIntent(this));
        mUserInfoResponse = mProductListPresenter.loadUser();
    }

    private void setUpDataForViewPage(List<ProductBean> products) {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        HashMap<String, ArrayList<ProductBean>> map = new HashMap<>();
        titles.add(getString(R.string.category_all));
        for (String category : mCategoryResponse.getCategoryList()) {
            titles.add(category);
            map.put(category, new ArrayList<>());
        }

        for (ProductBean Product : products) {
            if (!TextUtils.isEmpty(Product.getCategory())) {
                ArrayList<ProductBean> tempListBeen = map.get(Product.getCategory());
                if (tempListBeen == null) {
                    tempListBeen = new ArrayList<>();
                    map.put(Product.getCategory(), tempListBeen);
                }
                tempListBeen.add(Product);
            }
        }

        for (String category : mCategoryResponse.getCategoryList()) {
            ArrayList<ProductBean> value = map.get(category);
            fragments.add(newPriceListFragment(value));
        }

        fragments.add(0, newPriceListFragment((ArrayList<ProductBean>) products));
        initUI(titles, fragments);
    }


    private void initUI(List<String> titles, List<Fragment> priceFragmentList) {
        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), titles, priceFragmentList);
        mVp.setAdapter(mFragmentAdapter);
        mVp.setOffscreenPageLimit(priceFragmentList.size());
        mCddv.setUp(getActivityContext(),mVp,titles);
        mCddv.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mVp.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public PriceListFragment newPriceListFragment(ArrayList<ProductBean> value) {
        PriceListFragment priceListFragment = new PriceListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PriceListFragment.BUNDLE_KEY_LIST, value);
        bundle.putBoolean(PriceListFragment.BUNDLE_KEY_CAN_SEE_PRICE, mUserInfoResponse.isCanSeePrice());
        priceListFragment.setArguments(bundle);
        return priceListFragment;
    }

    private void setUpFragment() {
        if (mSynFlag < MAX_SYN_FLAG || mCategoryResponse == null || mProducts == null) {
            return;
        }
        setUpDataForViewPage(mProducts);
    }

    @Override
    public void showProducts(List<ProductBean> products) {
        mSynFlag++;
        mProducts = products;
        setUpFragment();
    }

    @Override
    public void showProductsEmpty() {
        mSynFlag++;
        setUpFragment();
    }

    @Override
    public void showError() {
        mSynFlag++;
    }

    @Override
    public void showCategorys(CategoryResponse categoryResponse) {
        mSynFlag++;
        mCategoryResponse = categoryResponse;
        setUpFragment();
    }

    @Override
    public void showCategorysError() {
        mSynFlag++;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

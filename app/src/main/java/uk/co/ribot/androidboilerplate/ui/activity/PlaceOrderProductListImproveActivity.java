package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.runwise.commomlibrary.util.DensityUtil;
import com.runwise.commomlibrary.view.DrapBubble.DragBubbleView;
import com.runwise.commomlibrary.view.LoadingLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.database.CategoryBean;
import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;
import uk.co.ribot.androidboilerplate.data.model.event.ProductCountUpdateEvent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.FragmentAdapter;
import uk.co.ribot.androidboilerplate.ui.adapter.ProductListAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.base.ProductCountSetter;
import uk.co.ribot.androidboilerplate.ui.fragment.ProductListFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.PlaceOrderProductListImprovePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.PlaceOrderProductListImproveMvpView;
import uk.co.ribot.androidboilerplate.view.CategoryDropDownView;

import static uk.co.ribot.androidboilerplate.ui.fragment.ProductListFragment.ARGUMENT_KEY_CATEGORY;

public class PlaceOrderProductListImproveActivity extends BaseActivity implements PlaceOrderProductListImproveMvpView {
    @Inject
    PlaceOrderProductListImprovePresenter mPlaceOrderProductListImprovePresenter;
    @BindView(R.id.category_drop_down_view)
    CategoryDropDownView mCategoryDropDownView;
    @BindView(R.id.vp_product_fragments)
    ViewPager mVpProductFragments;
    @BindView(R.id.loadingImg)
    ImageView mLoadingImg;
    @BindView(R.id.loadingTv)
    TextView mLoadingTv;
    @BindView(R.id.ttt)
    RelativeLayout mTtt;
    @BindView(R.id.loadingLayout)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.rl_content_container)
    RelativeLayout mRlContentContainer;
    @BindView(R.id.cb_cart_select_all)
    CheckBox mCbCartSelectAll;
    @BindView(R.id.tv_cart_select_all)
    TextView mTvCartSelectAll;
    @BindView(R.id.tv_cart_del)
    TextView mTvCartDel;
    @BindView(R.id.rv_cart)
    RecyclerView mRvCart;
    @BindView(R.id.tv_header)
    TextView mTvHeader;
    @BindView(R.id.stick_header)
    FrameLayout mStickHeader;
    @BindView(R.id.rl_cart_container)
    RelativeLayout mRlCartContainer;
    @BindView(R.id.iv_product_cart)
    ImageView mIvProductCart;
    @BindView(R.id.tv_cart_count)
    DragBubbleView mTvCartCount;
    @BindView(R.id.tv_product_total_price)
    TextView mTvProductTotalPrice;
    @BindView(R.id.tv_product_total_count)
    TextView mTvProductTotalCount;
    @BindView(R.id.tv_order_commit)
    TextView mTvOrderCommit;
    @BindView(R.id.tv_order_resume)
    TextView mTvOrderResume;
    @BindView(R.id.rl_bottom_bar)
    RelativeLayout mRlBottomBar;
    @BindView(R.id.rl_root)
    ViewGroup mRlRoot;

    HashMap<ProductBean, Double> mCounterMap = new HashMap<>();

    public static final Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, PlaceOrderProductListImproveActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_product_list_improve);
        ButterKnife.bind(this);
        configPersistentComponent.placeOrderProductListImproveActivityComponent(new ActivityModule(this)).inject(this);
        setTitle(getString(R.string.title_all_product));
        showBackBtn();
        mPlaceOrderProductListImprovePresenter.attachView(this);
        showLoadingDialog(getString(R.string.loading_products));
        mPlaceOrderProductListImprovePresenter.loadCategorys();
        setUpCartCountListener();
    }

    private void setUpCartCountListener(){
        mTvCartCount.setOnBubbleStateListener(new DragBubbleView.OnBubbleStateListener() {
            @Override
            public void onDrag() {

            }

            @Override
            public void onMove() {

            }

            @Override
            public void onRestore() {

            }

            @Override
            public void onDismiss() {
//                通知各个分类fragment更新商品编辑信息
               Set<ProductBean> productBeanList = mCounterMap.keySet();
                for (ProductBean productBean:productBeanList){
                    mCounterMap.put(productBean,new Double(0));
                    getRxBus().post(productBean);
                }
                mCounterMap.clear();
            }
        });
    }

    private void updateCart() {
        int productTypeCount = mCounterMap.size();
        if (productTypeCount > 0 && mTvCartCount.getState() == DragBubbleView.STATE_DISMISS){
            mTvCartCount.reCreate();
        }
        mTvCartCount.setVisibility(productTypeCount > 0 ? View.VISIBLE : View.GONE);
        mTvCartCount.setText(String.valueOf(productTypeCount));
    }


    ProductCountSetter mProductCountSetter = new ProductCountSetter() {
        @Override
        public void setCount(ProductBean productBean, double count) {
            if (count <= 0){
                mCounterMap.remove(productBean);
            }else{
                mCounterMap.put(productBean, count);
            }
//            通知fragment去更新
            getRxBus().post(productBean);
            updateCart();
        }

        @Override
        public double getCount(ProductBean bean) {
            Double count = mCounterMap.get(bean);
            if (count == null) {
                return 0;
            }
            return count;
        }

    };

    @Override
    public void showCategorys(List<CategoryBean> categoryBeans) {
        dismissLoadingDialog();
        //获取购物车图标的屏幕坐标
        int[] shopCarLocation = new int[2];
        mIvProductCart.getLocationInWindow(shopCarLocation);
        shopCarLocation[0] = shopCarLocation[0] + mIvProductCart.getWidth() / 2 - DensityUtil.dip2px(getActivityContext(),10);
        shopCarLocation[1] = shopCarLocation[1] - mIvProductCart.getHeight();

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        for (CategoryBean categoryBean : categoryBeans) {
            ProductListFragment productListFragment = new ProductListFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARGUMENT_KEY_CATEGORY, categoryBean);
            productListFragment.setArguments(bundle);
            productListFragment.setProductCountSetter(mProductCountSetter);
            productListFragment.setShopCartLocation(shopCarLocation);
            productListFragment.setRootView(mViewHolder.mLlRoot);
            fragmentArrayList.add(productListFragment);
            titles.add(categoryBean.getCategoryParent());
        }
        mVpProductFragments.setOffscreenPageLimit(titles.size());
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), titles, fragmentArrayList);
        mVpProductFragments.setAdapter(fragmentAdapter);
        mCategoryDropDownView.setUp(getActivityContext(), mVpProductFragments, titles);
    }

    @Override
    public void showCategorysError() {
        dismissLoadingDialog();
    }

    @Override
    public void showCategorysEmpty() {
        dismissLoadingDialog();
    }

}

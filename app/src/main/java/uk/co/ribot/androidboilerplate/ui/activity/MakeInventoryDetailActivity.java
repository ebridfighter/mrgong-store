package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import github.chenupt.dragtoplayout.DragTopLayout;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.CategoryResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.InventoryResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.PandianResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.injection.component.MakeInventoryDetailActivityComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.tools.TimeUtils;
import uk.co.ribot.androidboilerplate.ui.adapter.FragmentAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.fragment.MakeInventoryDetailFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.MakeInventoryDetailPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.MakeInventoryDetailMvpView;
import uk.co.ribot.androidboilerplate.view.CategoryDropDownView;


/**
 * Created by mike on 2017/11/28.
 */
public class MakeInventoryDetailActivity extends BaseActivity implements MakeInventoryDetailMvpView {
    @Inject
    MakeInventoryDetailPresenter mMakeInventoryDetailPresenter;
    @BindView(R.id.tv_1)
    TextView mTv1;
    @BindView(R.id.tv_2)
    TextView mTv2;
    @BindView(R.id.tv_3)
    TextView mTv3;
    @BindView(R.id.tv_4)
    TextView mTv4;
    @BindView(R.id.tv_5)
    TextView mTv5;
    @BindView(R.id.ll_top_view)
    LinearLayout mLlTopView;
    @BindView(R.id.vp)
    ViewPager mVp;
    @BindView(R.id.drag_content_view)
    LinearLayout mDragContentView;
    @BindView(R.id.drag_layout)
    DragTopLayout mDragLayout;
    @BindView(R.id.cddv)
    CategoryDropDownView mCategoryDropDownView;

    public static final String INTENT_KEY_INVENTORY_RESPONSE_LISTBEAN = "intent_key_inventory_response_listbean";
    CategoryResponse mCategoryResponse;

    public static Intent getStartIntent(Context context, InventoryResponse.ListBean listBean) {
        Intent intent = new Intent(context, MakeInventoryDetailActivity.class);
        intent.putExtra(INTENT_KEY_INVENTORY_RESPONSE_LISTBEAN, listBean);
        return intent;
    }

    InventoryResponse.ListBean mListBean;
    int mSynCount = 0;
    static final int SYN_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_inventory_detail);
        ButterKnife.bind(this);
        MakeInventoryDetailActivityComponent activityComponent = configPersistentComponent.makeInventoryDetailActivityComponent(new ActivityModule(this));
        activityComponent.inject(this);
        mMakeInventoryDetailPresenter.attachView(this);

        mListBean = (InventoryResponse.ListBean) getIntent().getSerializableExtra(INTENT_KEY_INVENTORY_RESPONSE_LISTBEAN);
        UserInfoResponse userInfoResponse = mMakeInventoryDetailPresenter.loadUser();
        if (userInfoResponse.isCanSeePrice()) {
            mTv4.setText("盘点结果(元)");
        } else {
            mTv4.setText("盘点结果(件)");
        }
        setTitle("盘点记录详情");
        showBackBtn();
        mTv1.setText("盘点人员：" + mListBean.getCreateUser());
        mTv2.setText("盘点单号：" + mListBean.getName());
        mTv3.setText("盘点日期：" + TimeUtils.getTimeStamps3(mListBean.getCreateDate()));
        if (userInfoResponse.isCanSeePrice()) {
            mTv5.setText("¥" + mListBean.getValue() + "");
            if (mListBean.getValue() >= 0) {
                mTv5.setTextColor(Color.parseColor("#9cb62e"));
            } else {
                mTv5.setTextColor(Color.parseColor("#e75967"));
            }
        } else {
            mTv5.setText(mListBean.getNum() + "");
            if (Double.parseDouble(mListBean.getNum()) >= 0) {
                mTv5.setTextColor(Color.parseColor("#9cb62e"));
            } else {
                mTv5.setTextColor(Color.parseColor("#e75967"));
            }
        }
        if ("confirm".equals(mListBean.getState())) {
            mTv5.setTextColor(Color.parseColor("#999999"));
            mTv5.setText("--");
        }
        mDragLayout.setOverDrag(false);
        mMakeInventoryDetailPresenter.loadProducts();
        mMakeInventoryDetailPresenter.getCategorys();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMakeInventoryDetailPresenter.detachView();
    }

    @Override
    public void showCategorys(CategoryResponse categoryResponse) {
        mCategoryResponse = categoryResponse;
        mSynCount++;
        if (mSynCount == SYN_COUNT) {
            setUpDataForViewPage();
        }
    }

    @Override
    public void showProductsEmpty() {

    }

    List<ProductListResponse.Product> mProducts;

    @Override
    public void showProducts(List<ProductListResponse.Product> products) {
        mProducts = products;
        for (PandianResponse.InventoryBean.LinesBean lines : mListBean.getLines()) {
            ProductListResponse.Product product = findProductById(lines.getProductID());
            if (product == null) {
                product = new ProductListResponse.Product();
                product.setStockType("gege");
            }
            lines.setProduct(product);
        }
        mSynCount++;
        if (mSynCount == SYN_COUNT) {
            setUpDataForViewPage();
        }
    }

    private ProductListResponse.Product findProductById(int productId) {
        for (ProductListResponse.Product product : mProducts) {
            if (product.getProductID() == productId) {
                return product;
            }
        }
        return null;
    }

    @Override
    public void showProductsError(String error) {

    }

    private void setUpDataForViewPage() {
        List<Fragment> orderProductFragmentList = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        HashMap<String, ArrayList<PandianResponse.InventoryBean.LinesBean>> map = new HashMap<>();
        titles.add(getString(R.string.category_all));
        for (String category : mCategoryResponse.getCategoryList()) {
            titles.add(category);
            map.put(category, new ArrayList<>());
        }
        for (PandianResponse.InventoryBean.LinesBean linesBean : mListBean.getLines()) {
            ArrayList<PandianResponse.InventoryBean.LinesBean> linesBeen = map.get(linesBean.getProduct().getCategory());
            if (linesBeen == null) {
                linesBeen = new ArrayList<>();
                map.put(linesBean.getProduct().getCategory(), linesBeen);
            }
            linesBeen.add(linesBean);
        }

        for (String category : mCategoryResponse.getCategoryList()) {
            ArrayList<PandianResponse.InventoryBean.LinesBean> value = map.get(category);
            orderProductFragmentList.add(newMakeInventoryDetailFragment(value));
        }
        orderProductFragmentList.add(0, newMakeInventoryDetailFragment((ArrayList<PandianResponse.InventoryBean.LinesBean>) mListBean.getLines()));

        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), titles, orderProductFragmentList);
        mVp.setAdapter(fragmentAdapter);//给ViewPager设置适配器
        mCategoryDropDownView.setup(getActivityContext(), mVp, titles);//将TabLayout和ViewPager关联起来
        mCategoryDropDownView.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    public MakeInventoryDetailFragment newMakeInventoryDetailFragment(ArrayList<PandianResponse.InventoryBean.LinesBean> value) {
        MakeInventoryDetailFragment makeInventoryDetailFragment = new MakeInventoryDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MakeInventoryDetailFragment.BUNDLE_KEY_LIST,value);
        makeInventoryDetailFragment.setArguments(bundle);
        return makeInventoryDetailFragment;
    }


}

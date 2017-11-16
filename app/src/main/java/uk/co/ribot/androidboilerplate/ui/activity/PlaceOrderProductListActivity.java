package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.runwise.commomlibrary.util.DensityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.business.AddedProduct;
import uk.co.ribot.androidboilerplate.data.model.event.ProductCountChangeEvent;
import uk.co.ribot.androidboilerplate.data.model.event.ProductQueryEvent;
import uk.co.ribot.androidboilerplate.data.model.net.response.CategoryResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.FragmentAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.fragment.OrderProductFragment;
import uk.co.ribot.androidboilerplate.ui.fragment.ProductListFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.PlaceOrderProductListPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.PlaceOrderProductListMvpView;
import uk.co.ribot.androidboilerplate.view.ProductTypePopup;

public class PlaceOrderProductListActivity extends BaseActivity implements PlaceOrderProductListMvpView {

    private static final int TAB_EXPAND_COUNT = 4;
    @BindView(R.id.title_iv_left)
    ImageButton mTitleIvLeft;
    @BindView(R.id.rt_product_name)
    EditText mRtProductName;
    @BindView(R.id.btn_add)
    Button mBtnAdd;
    @BindView(R.id.ll_search_bar)
    LinearLayout mLlSearchBar;
    @BindView(R.id.indicator)
    TabLayout mIndicator;
    @BindView(R.id.iv_open)
    ImageView mIvOpen;
    @BindView(R.id.vp)
    ViewPager mVp;
    @Inject
    PlaceOrderProductListPresenter mPlaceOrderProductListPresenter;
    CategoryResponse mCategoryResponse;
    private ArrayList<AddedProduct> addedPros;       //从前面页面传来的数组。
    private ArrayList<ProductListResponse.Product> dataList = new ArrayList<>();//全部的商品信息
    private ProductTypePopup mTypeWindow;//商品类型弹出框

    public static final String INTENT_KEY_BACKAP = "backap";

    public final int SYNC_COUNT = 2;
    public int mCurrentSyncCount = 0;

    public ArrayList<ProductListResponse.Product> getDataList() {
        return dataList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_product_list);
        ButterKnife.bind(this);
        configPersistentComponent.placeOrderProductListCompoent(new ActivityModule(this)).inject(this);
        mPlaceOrderProductListPresenter.attachView(this);
        mPlaceOrderProductListPresenter.getCategorys();
        mPlaceOrderProductListPresenter.loadProducts();
//        startService(SyncService.getStartIntent(this));
        hideTitleBar();
        //获取上一个页面传来的Parcelable
        Intent fromIntent = getIntent();
        Bundle bundle = fromIntent.getBundleExtra("apbundle");
        if (bundle != null) {
            addedPros = bundle.getParcelableArrayList("ap");
        }

        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //通知子fragment更新list产品数量
                getRxBus().post(new ProductCountChangeEvent());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mRtProductName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getRxBus().post(new ProductQueryEvent(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.btn_add, R.id.iv_open,R.id.title_iv_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                //回值给调用的页面
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                //当前选中的商品信息
                ArrayList<AddedProduct> addedList = new ArrayList<>();
                HashMap<String, Integer> countMap = ProductListFragment.getCountMap();
                Iterator iter = countMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, Integer> entry = (Map.Entry) iter.next();
                    String key = entry.getKey();
                    Integer count = entry.getValue();
                    Parcel parcel = Parcel.obtain();
                    AddedProduct pro = AddedProduct.CREATOR.createFromParcel(parcel);
                    if (count != 0) {
                        pro.setCount(count);
                        pro.setProductId(key);
                        addedList.add(pro);
                    }
                }
                bundle.putParcelableArrayList(INTENT_KEY_BACKAP, addedList);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.title_iv_left:
                finish();
                break;
            case R.id.iv_open:
                if (mTypeWindow == null) {
                    return;
                }
                if (!mTypeWindow.isShowing()) {
                    showPopWindow();
                } else {
                    mTypeWindow.dismiss();
                }
                break;
        }
    }

    private void setUpDataForViewPage() {
        List<Fragment> repertoryEntityFragmentList = new ArrayList<>();
        HashMap<String, ArrayList<ProductListResponse.Product>> map = new HashMap<>();
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.category_all));
        for (String category : mCategoryResponse.getCategoryList()) {
            titles.add(category);
            map.put(category, new ArrayList<ProductListResponse.Product>());
        }
        for (ProductListResponse.Product listBean : dataList) {
            if (!TextUtils.isEmpty(listBean.getCategory())) {
                ArrayList<ProductListResponse.Product> listBeen = map.get(listBean.getCategory());
                if (listBeen == null) {
                    listBeen = new ArrayList<>();
                    map.put(listBean.getCategory(), listBeen);
                }
                listBeen.add(listBean);
            }
        }
        for (String category : mCategoryResponse.getCategoryList()) {
            ArrayList<ProductListResponse.Product> value = map.get(category);
            repertoryEntityFragmentList.add(newProductListFragment(value));
        }
        repertoryEntityFragmentList.add(0, newProductListFragment((ArrayList<ProductListResponse.Product>) dataList));
        initUI(titles, repertoryEntityFragmentList);
        initPopWindow((ArrayList<String>) titles);
    }

    public ProductListFragment newProductListFragment(ArrayList<ProductListResponse.Product> value) {
        ProductListFragment productListFragment = new ProductListFragment();
        Bundle bundle = new Bundle();
        if (addedPros != null && addedPros.size() > 0) {
            bundle.putParcelableArrayList("ap", addedPros);
        }
        bundle.putSerializable(OrderProductFragment.BUNDLE_KEY_LIST, value);
        productListFragment.setArguments(bundle);
        return productListFragment;
    }


    private void initPopWindow(ArrayList<String> typeList) {
        final int[] location = new int[2];
        mIndicator.getLocationOnScreen(location);
        int y = (int) (location[1] + mIndicator.getHeight());
        mTypeWindow = new ProductTypePopup(this,
                ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtil.getScreenHeight(getActivityContext()) - y,
                typeList, 0);
        mTypeWindow.setViewPager(mVp);
        mTypeWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mIvOpen.setImageResource(R.drawable.arrow);
            }
        });
    }

    private void showPopWindow() {
        final int[] location = new int[2];
        mIndicator.getLocationOnScreen(location);
        int y = (int) (location[1] + mIndicator.getHeight());
        mTypeWindow.showAtLocation(mRootView, Gravity.NO_GRAVITY, 0, y);
        mTypeWindow.setSelect(mVp.getCurrentItem());
        mIvOpen.setImageResource(R.drawable.arrow_up);
    }

    private void initUI(List<String> titles, List<Fragment> repertoryEntityFragmentList) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), titles, repertoryEntityFragmentList);
        mVp.setAdapter(adapter);
        mIndicator.setupWithViewPager(mVp);
        mVp.setOffscreenPageLimit(titles.size());
        mIndicator.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mVp.setCurrentItem(position);
//                mProductTypeWindow.dismiss();
                mTypeWindow.dismiss();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if (titles.size() <= TAB_EXPAND_COUNT) {
            mIvOpen.setVisibility(View.GONE);
            mIndicator.setTabMode(TabLayout.MODE_FIXED);
        } else {
            mIvOpen.setVisibility(View.VISIBLE);
            mIndicator.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    @Override
    public void showCategorys(CategoryResponse categoryResponse) {
        mCategoryResponse = categoryResponse;
        showProductListAndCategorysTab();
    }

    @Override
    public void showProducts(List<ProductListResponse.Product> products) {
        if (products != null && products.size() != 0) {
            dataList.clear();
            dataList.addAll(products);
        }
        showProductListAndCategorysTab();
    }

    private void showProductListAndCategorysTab() {
        mCurrentSyncCount++;
        if (mCurrentSyncCount == SYNC_COUNT) {
            setUpDataForViewPage();
        }
    }

    @Override
    public void showProductsEmpty() {

    }

    @Override
    public void showError() {

    }
}

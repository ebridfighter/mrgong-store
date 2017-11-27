package uk.co.ribot.androidboilerplate.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runwise.commomlibrary.view.LoadingLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.business.AddedProduct;
import uk.co.ribot.androidboilerplate.data.model.event.ProductCountChangeEvent;
import uk.co.ribot.androidboilerplate.data.model.event.ProductQueryEvent;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.PlaceOrderProductAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.ProductListPresenter;


public class ProductListFragment extends BaseFragment {

    @BindView(R.id.rv_product)
    RecyclerView mRvProduct;
    @BindView(R.id.loadingLayout)
    LoadingLayout mLoadingLayout;
    Unbinder unbinder;
    @Inject
    PlaceOrderProductAdapter mPlaceOrderProductAdapter;
    @Inject
    ProductListPresenter mProductListPresenter;
    private ArrayList<AddedProduct> addedPros;
    //选中数量map
    private static HashMap<String, AddedProduct> countMap = new HashMap<>();
    //缓存全部商品列表
    private ArrayList<ProductListResponse.Product> arrayList;

    public static final String BUNDLE_KEY_LIST = "bundle_key_list";


    public static HashMap<String, AddedProduct> getCountMap() {
        return countMap;
    }

    private boolean canSeePrice = true;             //默认价格中可见

    @Inject
    public ProductListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFragmentBaseComponent.productListFragmentComponent(new ActivityModule(getActivity())).inject(this);
        addedPros = (ArrayList<AddedProduct>) getArguments().getSerializable("ap");
        mRvProduct.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvProduct.setAdapter(mPlaceOrderProductAdapter);
        canSeePrice = mProductListPresenter.loadUser().isCanSeePrice();
        setUpListData();

        getRxBusObservable().subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object object) {
                //收到了登出事件
                if (ProductCountChangeEvent.class.isInstance(object)) {
                    mPlaceOrderProductAdapter.notifyDataSetChanged();
                }
                if (ProductQueryEvent.class.isInstance(object)) {
                    ProductQueryEvent productQueryEvent = (ProductQueryEvent) object;
                    String word = productQueryEvent.getSearchWord();
                    //只在当前类型下面找名称包括的元素
                    List<ProductListResponse.Product> findArray = findArrayByWord(word);
                    mPlaceOrderProductAdapter.setProducts(findArray);
                }
            }
        });
    }

    public void setUpListData() {
        //得到数据，更新UI
        if (arrayList == null) {
            arrayList = (ArrayList<ProductListResponse.Product>) getArguments().getSerializable(BUNDLE_KEY_LIST);
        }
        //先统计一次id,个数
        for (ProductListResponse.Product bean : arrayList) {
            //同时根据上个页面传值更新一次
            int count = existInLastPager(bean);
            AddedProduct addedProduct = new AddedProduct();
            addedProduct.setProduct(bean);
            addedProduct.setCount(count);
            countMap.put(String.valueOf(bean.getProductID()), addedProduct);
        }

        mPlaceOrderProductAdapter.setProducts(arrayList);
        mPlaceOrderProductAdapter.setActivity(getActivity());
        mPlaceOrderProductAdapter.setCanSeePrice(canSeePrice);
        mPlaceOrderProductAdapter.setCountMap(getCountMap());
        mLoadingLayout.onSuccess(mPlaceOrderProductAdapter.getItemCount(), "这里是空哒~~", R.drawable.default_ico_none);
    }

    //返回当前标签下名称包含的
    private List<ProductListResponse.Product> findArrayByWord(String word) {
        List<ProductListResponse.Product> findList = new ArrayList<>();
        for (ProductListResponse.Product bean : arrayList) {
            if (bean.getName().contains(word)) {
                findList.add(bean);
            }
        }
        return findList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    private int existInLastPager(ProductListResponse.Product bean) {
        if (addedPros != null) {
            for (AddedProduct product : addedPros) {
                if (product.getProductId().equals(String.valueOf(bean.getProductID()))) {
                    return product.getCount();
                }
            }
        }
        return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countMap.clear();
    }


}

package uk.co.ribot.androidboilerplate.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runwise.commomlibrary.view.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.database.CategoryBean;
import uk.co.ribot.androidboilerplate.data.model.database.CategoryChildBean;
import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;
import uk.co.ribot.androidboilerplate.data.model.net.response.CategoryResponse;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.activity.PlaceOrderProductListImproveActivity;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.base.ProductCountSetter;
import uk.co.ribot.androidboilerplate.ui.presenter.ProductListPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.ProductListMvpView;
import uk.co.ribot.androidboilerplate.view.LinkageListContainer;


public class ProductListFragment extends BaseFragment implements ProductListMvpView {

    @Inject
    ProductListPresenter mProductListPresenter;
    @BindView(R.id.linkage_list_container)
    LinkageListContainer mLinkageListContainer;
    @BindView(R.id.loadingLayout)
    LoadingLayout mLoadingLayout;
    Unbinder unbinder;

    public static final String ARGUMENT_KEY_CATEGORY = "argument_key_category";
    private CategoryBean mCategoryParent;
    ProductCountSetter mProductCountSetter;
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
        mCategoryParent = (CategoryBean) getArguments().getSerializable(ARGUMENT_KEY_CATEGORY);
        mFragmentBaseComponent.productListFragmentComponent(new ActivityModule(getActivity())).inject(this);
        mLoadingLayout.setStatusLoading();
        mProductListPresenter.attachView(this);
        mProductListPresenter.loadProductsByCategoryParent(mCategoryParent.getCategoryParent());
        getRxBusObservable().subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object object) {
//                用于接收刷新商品数量的事件
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showProducts(List<ProductBean> products) {
        mLoadingLayout.onSuccess(products.size(),"");
        ArrayList<String> categoryList = new ArrayList<>();
        for(CategoryChildBean categoryChildBean : mCategoryParent.getCategoryChildBeans()){
            categoryList.add(categoryChildBean.getName());
        }
        mLinkageListContainer.init(mCategoryParent.getCategoryParent(),products,categoryList,mProductCountSetter);
    }

    @Override
    public void showProductsEmpty() {
        mLoadingLayout.onSuccess(0,"没有商品数据");
    }

    @Override
    public void showError() {

    }

    @Override
    public void showCategorys(CategoryResponse categoryResponse) {

    }

    @Override
    public void showCategorysError() {

    }
    public void setProductCountSetter(ProductCountSetter productCountSetter) {
        mProductCountSetter = productCountSetter;
    }
}

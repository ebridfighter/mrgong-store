package uk.co.ribot.androidboilerplate.ui.presenter;

import android.annotation.SuppressLint;

import com.raizlabs.android.dbflow.structure.BaseModel;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.AsyncProcessor;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.database.CategoryBean;
import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.PlaceOrderMvpView;
import uk.co.ribot.androidboilerplate.ui.view_interface.PlaceOrderProductListImproveMvpView;
import uk.co.ribot.androidboilerplate.util.RxUtil;
import uk.co.ribot.androidboilerplate.util.ToastUtil;

public class PlaceOrderProductListImprovePresenter extends BasePresenter<PlaceOrderProductListImproveMvpView> {
    private DataManager mDataManager;
    private Disposable mLoadProductsDisposable;
    private Disposable mLoadCategorysDisposable;

    @Inject
    public PlaceOrderProductListImprovePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public boolean canSeePrice() {
        UserInfoResponse userInfoResponse = mDataManager.loadUser();
        if (userInfoResponse != null) {
            return userInfoResponse.isCanSeePrice();
        }
        return false;
    }

    @Override
    public void attachView(PlaceOrderProductListImproveMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.dispose(mLoadProductsDisposable);
        RxUtil.dispose(mLoadCategorysDisposable);
    }

    @SuppressLint("CheckResult")
    public void loadProductListAndCategoryList() {
        List<ProductBean> productBeans = new ArrayList<>();
        List<CategoryBean> categoryBeans = new ArrayList<>();

        Single.merge(mDataManager.loadProducts(), mDataManager.loadCategorys()).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Consumer<List<? extends BaseModel>>() {

            @Override
            public void accept(List<? extends BaseModel> baseModels) {
                boolean isProductBeanClass;
                if (baseModels.size() == 0) {
                    isProductBeanClass = true;
                } else {
                    isProductBeanClass = baseModels.get(0).getClass() == ProductBean.class;
                }
                if (isProductBeanClass) {
                    productBeans.clear();
                    productBeans.addAll((List<ProductBean>) baseModels);
                } else {
                    categoryBeans.clear();
                    categoryBeans.addAll((List<CategoryBean>) baseModels);
                }
                if (productBeans.size() > 0 && categoryBeans.size() > 0) {
//                    getMvpView().showProductsAndCategorys(productBeans, categoryBeans);
                }
            }

        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
//                getMvpView().showProductsAndCategorysError();
            }
        });
    }


    public void loadProducts() {
        checkViewAttached();
        RxUtil.dispose(mLoadProductsDisposable);
        mDataManager.loadProducts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<ProductBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mLoadProductsDisposable = d;
                    }

                    @Override
                    public void onSuccess(List<ProductBean> products) {
//                        if (products.isEmpty()) {
//                            getMvpView().showProductsEmpty();
//                        } else {
//                            getMvpView().showProducts(products);
//                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the products.");
//                        getMvpView().showProductsError();
                    }
                });
    }

    public void loadCategorys() {
        checkViewAttached();
        RxUtil.dispose(mLoadCategorysDisposable);
        mDataManager.loadCategorys()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<CategoryBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mLoadCategorysDisposable = d;
                    }

                    @Override
                    public void onSuccess(List<CategoryBean> categoryBeanList) {
                        if (categoryBeanList.isEmpty()) {
                            getMvpView().showCategorysEmpty();
                        } else {
                            getMvpView().showCategorys(categoryBeanList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the categorys.");
                        getMvpView().showCategorysError();
                    }
                });
    }

}

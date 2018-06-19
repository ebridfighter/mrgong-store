package uk.co.ribot.androidboilerplate.ui.presenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx.Subscriber;
import rx.Subscription;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.CategoryResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.PlaceOrderProductListMvpView;
import uk.co.ribot.androidboilerplate.util.RxUtil;

/**
 * Created by mike on 2017/11/16.
 */

public class PlaceOrderProductListPresenter extends BasePresenter<PlaceOrderProductListMvpView> {
    private final DataManager mDataManager;
    private Subscription mLoadProductsSubscription;
    private Subscription mCategorySubscription;

    @Inject
    public PlaceOrderProductListPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(PlaceOrderProductListMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mLoadProductsSubscription != null) mLoadProductsSubscription.unsubscribe();
        if (mCategorySubscription != null) mCategorySubscription.unsubscribe();
    }

    public void getCategorys() {
        checkViewAttached();
        mDataManager.getCategorys()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<CategoryResponse>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(CategoryResponse categoryResponse) {
                getMvpView().showCategorys(categoryResponse);
            }
        });
    }

    public void loadProducts() {
        checkViewAttached();
        RxUtil.unsubscribe(mLoadProductsSubscription);
        mLoadProductsSubscription = mDataManager.loadProducts()
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribeOn(rx.schedulers.Schedulers.io())
                .subscribe(new Subscriber<List<ProductListResponse.Product>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(List<ProductListResponse.Product> products) {
                        if (products.isEmpty()) {
                            getMvpView().showProductsEmpty();
                        } else {
                            getMvpView().showProducts(products);
                        }
                    }
                });
    }


}

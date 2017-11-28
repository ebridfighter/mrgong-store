package uk.co.ribot.androidboilerplate.ui.presenter;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.CategoryResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ProductListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.MakeInventoryDetailMvpView;
import uk.co.ribot.androidboilerplate.util.RxUtil;

/**
 * Created by mike on 2017/11/28.
 */
public class MakeInventoryDetailPresenter extends BasePresenter<MakeInventoryDetailMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    private Subscription mCategorySubscription;


    @Inject
    public MakeInventoryDetailPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MakeInventoryDetailMvpView mvpView) {
        super.attachView(mvpView);
    }

    public UserInfoResponse loadUser(){
        return mDataManager.loadUser();
    }

    public void getCategorys() {
        checkViewAttached();
        RxUtil.unsubscribe(mCategorySubscription);
        mCategorySubscription = mDataManager.getCategorys()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<CategoryResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CategoryResponse categoryResponse) {
                        getMvpView().showCategorys(categoryResponse);
                    }
                });
    }
    public void loadProducts() {
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.loadProducts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<ProductListResponse.Product>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the products.");
                        getMvpView().showProductsError(e.toString());
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

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
        if (mCategorySubscription != null) mCategorySubscription.unsubscribe();
    }
}

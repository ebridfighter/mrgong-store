package uk.co.ribot.androidboilerplate.ui.presenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.database.Product;
import uk.co.ribot.androidboilerplate.data.model.net.response.CategoryResponse;
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
        RxUtil.unsubscribe(mSubscription);
        mDataManager.loadProducts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<Product>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Product> products) {
                        if (products.isEmpty()) {
                            getMvpView().showProductsEmpty();
                        } else {
                            getMvpView().showProducts(products);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the products.");
                        getMvpView().showProductsError(e.toString());
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

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
import uk.co.ribot.androidboilerplate.data.model.database.ProductBean;
import uk.co.ribot.androidboilerplate.data.model.net.response.CategoryResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.ProductListMvpView;
import uk.co.ribot.androidboilerplate.util.RxUtil;

/**
 * Created by mike on 2017/10/31.
 */

public class ProductListPresenter extends BasePresenter<ProductListMvpView> {
    private final DataManager mDataManager;
    private Disposable mCategoryDisposable;

    @Inject
    public ProductListPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(ProductListMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.dispose(mCategoryDisposable);

    }

    public UserInfoResponse loadUser() {
        return mDataManager.loadUser();
    }


    public void loadProductsByCategoryParent(String categoryParent) {
        checkViewAttached();
        RxUtil.dispose(mCategoryDisposable);
        mDataManager.loadProductsByCategoryParent(categoryParent)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<ProductBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCategoryDisposable = d;
                    }

                    @Override
                    public void onSuccess(List<ProductBean> products) {
                        if (products.isEmpty()) {
                            getMvpView().showProductsEmpty();
                        } else {
                            getMvpView().showProducts(products);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showError();
                    }
                });

    }
}

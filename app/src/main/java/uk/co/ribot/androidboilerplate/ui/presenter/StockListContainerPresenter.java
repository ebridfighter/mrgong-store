package uk.co.ribot.androidboilerplate.ui.presenter;

import android.util.Log;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.CategoryResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.StockListContainerMvpView;

/**
 * Created by Dong on 2017/11/8.
 */

public class StockListContainerPresenter extends BasePresenter<StockListContainerMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public StockListContainerPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(StockListContainerMvpView mvpView) {
        super.attachView(mvpView);
        if (mDataManager.isLogin()) {//已登录，查类别
            mDataManager.getCategorys()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<CategoryResponse>() {
                        @Override
                        public void accept(CategoryResponse categoryResponse) throws Exception {
                            getMvpView().showCategories(categoryResponse.getCategoryList());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.d("haha", throwable.getMessage());
                        }
                    });
        } else {
            //todo:没有登录，用假数据
        }
    }
}

package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.CategoryResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.ProductListMvpView;
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
        if(mDataManager.isLogin() && false){//已登录，查类别
            mDataManager.getCategory()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<CategoryResponse>() {
                        @Override
                        public void call(CategoryResponse categoryResponse) {
                            getMvpView().showCategories(categoryResponse.getCategoryList());
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                        }
                    });
        }else{
            //todo:没有登录，用假数据
        }
    }
}

package uk.co.ribot.androidboilerplate.ui.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.DashBoardResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.EmptyResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.FinishReturnResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.HomePageBannerResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.ReturnOrderListResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.HomePageMvpView;
import uk.co.ribot.androidboilerplate.util.RxUtil;

/**
 * Created by mike on 2017/11/1.
 */

public class HomePagePresenter extends BasePresenter<HomePageMvpView> {
    private final DataManager mDataManager;
    private Subscription mOrderSubscription;
    private Subscription mOrderPollingSubscription;
    private Subscription mReturnOrderPollingSubscription;
    private Subscription mReturnOrderSubscription;
    private Subscription mHomePageBannerSubscription;
    private Subscription mDashBoardSubscription;
    private Subscription mCancelOrderSubscription;
    private Subscription mFinishReturnOrderSubscription;

    @Inject
    public HomePagePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(HomePageMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void pollingOrders(){
        checkViewAttached();
        RxUtil.unsubscribe(mOrderPollingSubscription);
        mOrderPollingSubscription =  Observable.interval(0,5, TimeUnit.SECONDS).flatMap(new Func1<Long, Observable<OrderListResponse>>() {
            @Override
            public Observable<OrderListResponse> call(Long aLong) {
                return mDataManager.syncOrders()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io());
            }
        }).takeUntil(new Func1<OrderListResponse, Boolean>() {
            @Override
            public Boolean call(OrderListResponse orderListResponse) {
                return !getMvpView().isVisiable();
            }
        }).subscribe(new Subscriber<OrderListResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "There was an error loading the orders.");
                getMvpView().showOrdersError();
            }

            @Override
            public void onNext(OrderListResponse orderListResponse) {
                if (orderListResponse.getList().isEmpty()) {
                    getMvpView().showOrdersEmpty();
                } else {
                    getMvpView().showOrders(orderListResponse.getList());
                }
            }
        });
    }

    public void pollingReturnOrders(){
        checkViewAttached();
        RxUtil.unsubscribe(mReturnOrderPollingSubscription);
        mReturnOrderPollingSubscription =  Observable.interval(0,5, TimeUnit.SECONDS).flatMap(new Func1<Long, Observable<ReturnOrderListResponse>>() {
            @Override
            public Observable<ReturnOrderListResponse> call(Long aLong) {
                return mDataManager.syncReturnOrders()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io());
            }
        }).takeUntil(new Func1<ReturnOrderListResponse, Boolean>() {
            @Override
            public Boolean call(ReturnOrderListResponse returnOrderListResponse) {
                return !getMvpView().isVisiable();
            }
        }).subscribe(new Subscriber<ReturnOrderListResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "There was an error loading the ReturnOrders.");
                getMvpView().showReturnOrdersError();
            }

            @Override
            public void onNext(ReturnOrderListResponse returnOrderListResponse) {
                if (returnOrderListResponse.getList().isEmpty()) {
                    getMvpView().showReturnOrdersEmpty();
                } else {
                    getMvpView().showReturnOrders(returnOrderListResponse.getList());
                }
            }
        });
    }


    public void syncOrders() {
        checkViewAttached();
        RxUtil.unsubscribe(mOrderSubscription);
        mOrderSubscription = mDataManager.syncOrders()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<OrderListResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showOrdersError();
                    }

                    @Override
                    public void onNext(OrderListResponse orderListResponse) {
                        if (orderListResponse.getList().isEmpty()) {
                            getMvpView().showOrdersEmpty();
                        } else {
                            getMvpView().showOrders(orderListResponse.getList());
                        }
                    }
                });
    }

    public void syncReturnOrders() {
        checkViewAttached();
        RxUtil.unsubscribe(mReturnOrderSubscription);
        mReturnOrderSubscription = mDataManager.syncReturnOrders()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ReturnOrderListResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showReturnOrdersError();
                    }

                    @Override
                    public void onNext(ReturnOrderListResponse returnOrderListResponse) {
                        if (returnOrderListResponse.getList().isEmpty()) {
                            getMvpView().showReturnOrdersEmpty();
                        } else {
                            getMvpView().showReturnOrders(returnOrderListResponse.getList());
                        }
                    }
                });
    }

    public void getHomePageBanner(String tag) {
        checkViewAttached();
        RxUtil.unsubscribe(mHomePageBannerSubscription);
        mHomePageBannerSubscription = mDataManager.getHomePageBanner(tag)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).map(new Func1<HomePageBannerResponse, List<String>>() {
                    @Override
                    public List<String> call(HomePageBannerResponse homePageBannerResponse) {
                        List<HomePageBannerResponse.PostResponse> imageResponses = homePageBannerResponse.getPost_list();
                        List<String> imageUrls = new ArrayList<String>();
                        for (HomePageBannerResponse.PostResponse postResponse : imageResponses) {
                            imageUrls.add(postResponse.getCover_url());
                        }
                        return imageUrls;
                    }
                })
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the HomePageBanner.");
                        getMvpView().showHomePageBannerError();
                    }

                    @Override
                    public void onNext(List<String> imageUrls) {
                        getMvpView().showHomePageBanner(imageUrls);
                    }
                });
    }

    public void getDashBoard() {
        checkViewAttached();
        RxUtil.unsubscribe(mDashBoardSubscription);
        mDashBoardSubscription = mDataManager.getDashboard()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DashBoardResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the DashBoardResponse.");
                        getMvpView().showDashBoardError();
                    }

                    @Override
                    public void onNext(DashBoardResponse dashBoardResponse) {
                        UserInfoResponse userInfoResponse = mDataManager.getUserInfo();
                        if (userInfoResponse != null && userInfoResponse.isCanSeePrice()){
                            getMvpView().showDashBoard(dashBoardResponse);
                        }else{
                            getMvpView().showDashBoardWithoutPrice(dashBoardResponse);
                        }
                    }
                });
    }

    public void cancelOrder(int orderId){
        checkViewAttached();
        RxUtil.unsubscribe(mCancelOrderSubscription);
        mCancelOrderSubscription = mDataManager.changeOrderState(orderId,"cancel")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<EmptyResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error cancel order.");
                        getMvpView().cancleOrderError();
                    }

                    @Override
                    public void onNext(EmptyResponse emptyResponse) {
                        getMvpView().cancelOrderSuccess();
                    }
                });
    }

    public void finishOrder(int returnOrderId){
        checkViewAttached();
        RxUtil.unsubscribe(mFinishReturnOrderSubscription);
        mFinishReturnOrderSubscription = mDataManager.finishReturnOrder(returnOrderId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<FinishReturnResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error cancel order.");
                        getMvpView().finishReturnOrderError();
                    }

                    @Override
                    public void onNext(FinishReturnResponse finishReturnResponse) {
                        getMvpView().finishReturnOrderSuccess(finishReturnResponse);
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mOrderSubscription != null) mOrderSubscription.unsubscribe();
        if (mReturnOrderSubscription != null) mReturnOrderSubscription.unsubscribe();
        if (mHomePageBannerSubscription != null) mHomePageBannerSubscription.unsubscribe();
        if (mDashBoardSubscription != null) mDashBoardSubscription.unsubscribe();
        if (mCancelOrderSubscription != null) mCancelOrderSubscription.unsubscribe();
        if (mFinishReturnOrderSubscription != null) mFinishReturnOrderSubscription.unsubscribe();
        if (mOrderPollingSubscription != null) mOrderPollingSubscription.unsubscribe();
        if (mReturnOrderPollingSubscription != null) mReturnOrderPollingSubscription.unsubscribe();
    }
}

package uk.co.ribot.androidboilerplate.ui.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;
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

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

/**
 * Created by mike on 2017/11/1.
 */

public class HomePagePresenter extends BasePresenter<HomePageMvpView> {
    private final DataManager mDataManager;
    private Disposable mOrderDisposable;
    private Disposable mOrderPollingDisposable;
    private Disposable mReturnOrderPollingDisposable;
    private Disposable mReturnOrderDisposable;
    private Disposable mHomePageBannerDisposable;
    private Disposable mDashBoardDisposable;
    private Disposable mCancelOrderDisposable;
    private Disposable mFinishReturnOrderDisposable;

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
        mDataManager.syncOrders()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<OrderListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mOrderPollingDisposable = d;
                    }

                    @Override
                    public void onNext(OrderListResponse orderListResponse) {
                        if (orderListResponse.getList().isEmpty()) {
                            getMvpView().showOrdersEmpty();
                        } else {
                            getMvpView().showOrders(orderListResponse.getList());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the orders.");
                        getMvpView().showOrdersError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void pollingReturnOrders(){
        checkViewAttached();
        mDataManager.syncReturnOrders()
                .observeOn(mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<ReturnOrderListResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                mReturnOrderPollingDisposable = d;
            }

            @Override
            public void onNext(ReturnOrderListResponse returnOrderListResponse) {
                if (returnOrderListResponse.getList().isEmpty()) {
                    getMvpView().showReturnOrdersEmpty();
                } else {
                    getMvpView().showReturnOrders(returnOrderListResponse.getList());
                }
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "There was an error loading the ReturnOrders.");
                getMvpView().showReturnOrdersError();
            }

            @Override
            public void onComplete() {

            }
        });



    }


    public void syncOrders() {
        checkViewAttached();
        mDataManager.syncOrders()
                .observeOn(mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<OrderListResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                mOrderDisposable = d;
            }

            @Override
            public void onNext(OrderListResponse orderListResponse) {
                if (orderListResponse.getList().isEmpty()) {
                    getMvpView().showOrdersEmpty();
                } else {
                    getMvpView().showOrders(orderListResponse.getList());
                }
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "There was an error loading the ribots.");
                getMvpView().showOrdersError();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void syncReturnOrders() {
        checkViewAttached();
        mDataManager.syncReturnOrders()
                .observeOn(mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<ReturnOrderListResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                mReturnOrderDisposable = d;
            }

            @Override
            public void onNext(ReturnOrderListResponse returnOrderListResponse) {
                if (returnOrderListResponse.getList().isEmpty()) {
                    getMvpView().showReturnOrdersEmpty();
                } else {
                    getMvpView().showReturnOrders(returnOrderListResponse.getList());
                }
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "There was an error loading the ribots.");
                getMvpView().showReturnOrdersError();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getHomePageBanner(String tag) {
        checkViewAttached();
       mDataManager.getHomePageBanner(tag)
                .observeOn(mainThread())
                .subscribeOn(Schedulers.io()).map(new Function<HomePageBannerResponse, List<String>>() {
           @Override
           public List<String> apply(HomePageBannerResponse homePageBannerResponse) throws Exception {
               List<HomePageBannerResponse.PostResponse> imageResponses = homePageBannerResponse.getPost_list();
               List<String> imageUrls = new ArrayList<String>();
               for (HomePageBannerResponse.PostResponse postResponse : imageResponses) {
                   imageUrls.add(postResponse.getCover_url());
               }
               return imageUrls;
           }}).subscribe(new Observer<List<String>>() {
           @Override
           public void onSubscribe(Disposable d) {
               mHomePageBannerDisposable = d;
           }

           @Override
           public void onNext(List<String> imageUrls) {
               getMvpView().showHomePageBanner(imageUrls);
           }

           @Override
           public void onError(Throwable e) {
               Timber.e(e, "There was an error loading the HomePageBanner.");
               getMvpView().showHomePageBannerError();
           }

           @Override
           public void onComplete() {

           }
       });
    }

    public void getDashBoard() {
        checkViewAttached();
        mDataManager.getDashboard()
                .observeOn(mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<DashBoardResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDashBoardDisposable = d;
                    }

                    @Override
                    public void onNext(DashBoardResponse dashBoardResponse) {
                        UserInfoResponse userInfoResponse = mDataManager.loadUser();
                        if (userInfoResponse != null && userInfoResponse.isCanSeePrice()){
                            getMvpView().showDashBoard(dashBoardResponse);
                        }else{
                            getMvpView().showDashBoardWithoutPrice(dashBoardResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the DashBoardResponse.");
                        getMvpView().showDashBoardError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void cancelOrder(int orderId){
        checkViewAttached();
        mDataManager.changeOrderState(orderId,"cancel")
                .observeOn(mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCancelOrderDisposable = d;
            }

            @Override
            public void onNext(EmptyResponse value) {
                getMvpView().cancelOrderSuccess();
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "There was an error cancel order.");
                getMvpView().cancelOrderError();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void finishOrder(int returnOrderId){
        checkViewAttached();
        mDataManager.finishReturnOrder(returnOrderId)
                .observeOn(mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<FinishReturnResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mFinishReturnOrderDisposable = d;
                    }

                    @Override
                    public void onNext(FinishReturnResponse finishReturnResponse) {
                        getMvpView().finishReturnOrderSuccess(finishReturnResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error cancel order.");
                        getMvpView().finishReturnOrderError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mOrderDisposable != null) mOrderDisposable.dispose();
        if (mReturnOrderDisposable != null) mReturnOrderDisposable.dispose();
        if (mHomePageBannerDisposable != null) mHomePageBannerDisposable.dispose();
        if (mDashBoardDisposable != null) mDashBoardDisposable.dispose();
        if (mCancelOrderDisposable != null) mCancelOrderDisposable.dispose();
        if (mFinishReturnOrderDisposable != null) mFinishReturnOrderDisposable.dispose();
        if (mOrderPollingDisposable != null) mOrderPollingDisposable.dispose();
        if (mReturnOrderPollingDisposable != null) mReturnOrderPollingDisposable.dispose();
    }
}

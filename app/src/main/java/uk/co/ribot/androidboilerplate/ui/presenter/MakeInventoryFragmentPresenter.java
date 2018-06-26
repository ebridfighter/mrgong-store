package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.EmptyResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.InventoryResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.MakeInventoryFragmentMvpView;

/**
 * Created by mike on 2017/11/28.
 */
public class MakeInventoryFragmentPresenter extends BasePresenter<MakeInventoryFragmentMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    private Subscription mCancleMakeInventorySubscription;


    @Inject
    public MakeInventoryFragmentPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MakeInventoryFragmentMvpView mvpView) {
        super.attachView(mvpView);
    }

    public UserInfoResponse loadUser() {
        return mDataManager.loadUser();
    }

    public void getInventoryList(int page, int pageNum, int type) {
        checkViewAttached();
        mDataManager.getInventoryList(page, pageNum, type).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<InventoryResponse>() {

            @Override
            public void onError(Throwable e) {
                getMvpView().showInventoryListError(e.toString());
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(InventoryResponse inventoryResponse) {
                if (inventoryResponse.getList().isEmpty()) {
                    getMvpView().showInventoryListEmpty();
                } else {
                    getMvpView().showInventoryList(inventoryResponse);
                }
            }
        });
    }

    public void cancelMakeInventory(int id, String state) {
        checkViewAttached();
       mDataManager.cancelMakeInventory(id, state).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<EmptyResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        getMvpView().cancelMakeInventoryError(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EmptyResponse emptyResponse) {
                        getMvpView().cancelMakeInventory();
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
        if (mCancleMakeInventorySubscription != null)
            mCancleMakeInventorySubscription.unsubscribe();
    }
}

package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.EmptyResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.UserInfoMvpView;

/**
 * Created by mike on 2017/11/27.
 */
public class UserInfoPresenter extends BasePresenter<UserInfoMvpView> {

    private final DataManager mDataManager;
    private Subscription mLogoutSubscription;


    @Inject
    public UserInfoPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(UserInfoMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void logout() {
        checkViewAttached();
        mDataManager.logout()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<EmptyResponse>() {

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the DashBoardResponse.");
                        getMvpView().logoutError();
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EmptyResponse emptyResponse) {
                        getMvpView().logout();
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mLogoutSubscription != null) mLogoutSubscription.unsubscribe();
    }
}

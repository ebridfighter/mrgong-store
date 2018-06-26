package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.MessageResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.MessageMvpView;

/**
 * Created by leo on 17-11-2.
 */

public class MessagePresenter extends BasePresenter<MessageMvpView> {
    private final DataManager mDataManager;
    private Subscription mMessageSubscription;

    @Inject
    public MessagePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }


    public void getMessages() {
        checkViewAttached();
        mDataManager.getMessages()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<MessageResponse>() {

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showMessagesError();
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MessageResponse messageResponse) {
                        if (messageResponse.getOrder().isEmpty()) {
                            getMvpView().showMessagesEmpty();
                        } else {
                            getMvpView().showMessage(messageResponse.getOrder());
                        }
                    }
                });
    }

    public boolean isCanSeePrice() {
        UserInfoResponse userInfoResponse = mDataManager.loadUser();
        if (userInfoResponse != null) {
            return userInfoResponse.isCanSeePrice();
        }
        return false;
    }
}

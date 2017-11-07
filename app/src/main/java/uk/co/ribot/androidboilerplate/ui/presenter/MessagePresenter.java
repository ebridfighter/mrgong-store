package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.MessageResponse;
import uk.co.ribot.androidboilerplate.data.model.net.response.UserInfoResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.MessageMvpView;
import uk.co.ribot.androidboilerplate.util.RxUtil;

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
        RxUtil.unsubscribe(mMessageSubscription);
        mMessageSubscription = mDataManager.getMessages()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<MessageResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showMessagesError();
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

    public boolean isCanSeePrice(){
        UserInfoResponse userInfoResponse = mDataManager.getUserInfo();
        if (userInfoResponse != null){
            return userInfoResponse.isCanSeePrice();
        }
        return  false;
    }
}

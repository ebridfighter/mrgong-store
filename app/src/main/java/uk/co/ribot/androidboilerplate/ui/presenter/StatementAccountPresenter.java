package uk.co.ribot.androidboilerplate.ui.presenter;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.net.response.StatementAccountListResponse;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.StatementAccountMvpView;

/**
 * Created by mike on 2017/11/28.
 */
public class StatementAccountPresenter extends BasePresenter<StatementAccountMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;


    @Inject
    public StatementAccountPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(StatementAccountMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void getStatementAccountList() {
        checkViewAttached();
        mDataManager.getStatementAccountList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<StatementAccountListResponse>() {

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "网络错误");
                        getMvpView().showStatementAccountListError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StatementAccountListResponse statementAccountListResponse) {
                        if (statementAccountListResponse.getBankStatement().isEmpty()){
                            getMvpView().showStatementAccountListEmpty();
                        }else{
                            getMvpView().showStatementAccountList(statementAccountListResponse);
                        }
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }
}

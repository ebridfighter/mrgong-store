package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import uk.co.ribot.androidboilerplate.injection.component.LauncherActivityComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.presenter.LauncherPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.LauncherMvpView;


/**
 * Created by mike on 2017/11/29.
 */
public class LauncherActivity extends BaseActivity implements LauncherMvpView {
    @Inject
    LauncherPresenter mLauncherPresenter;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LauncherActivity.class);
        return intent;
    }

    public static final int COUNT_DOWN_SECONDS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_launcher);
//        hideTitleBar();
        LauncherActivityComponent activityComponent = configPersistentComponent.launcherActivityComponent(new ActivityModule(this));
        activityComponent.inject(this);
        mLauncherPresenter.attachView(this);
        mLauncherPresenter.timingBegins(COUNT_DOWN_SECONDS);
        mLauncherPresenter.getServerDate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLauncherPresenter.detachView();
    }

    @Override
    public void countDownFinish() {
//        if (SPUtils.firstLaunch(this)) {
//            startActivity(new Intent(this, NavigationActivity.class));
//        } else {
            startActivity(LoginActivity.getStartIntent(getActivityContext()));
//        }
        finish();
    }

    @Override
    public void onReceiveServerTime(long serverTime) {

    }
}

package uk.co.ribot.androidboilerplate.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.runwise.commomlibrary.view.LoadingDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import rx.Subscriber;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.data.model.event.LogOutEvent;
import uk.co.ribot.androidboilerplate.injection.component.ConfigPersistentComponent;
import uk.co.ribot.androidboilerplate.injection.component.DaggerConfigPersistentComponent;
import uk.co.ribot.androidboilerplate.injection.component.ExampleActivityComponent;
import uk.co.ribot.androidboilerplate.ui.activity.LoginActivity;
import uk.co.ribot.androidboilerplate.util.ActivityUtil;
import uk.co.ribot.androidboilerplate.util.ToastUtil;

/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();

    private ExampleActivityComponent mActivityComponent;
    private long mActivityId;
    protected ConfigPersistentComponent configPersistentComponent;
    protected View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtil.getInstance().addActivity(this);
        // Create the ExampleActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();
        if (!sComponentsMap.containsKey(mActivityId)) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(BoilerplateApplication.get(this).getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = sComponentsMap.get(mActivityId);
        }

        BoilerplateApplication.get(this).getComponent().eventBus().observable().subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object object) {
                //收到了登出事件
                if (LogOutEvent.class.isInstance(object)) {
                    BoilerplateApplication.get(getActivityContext()).getComponent().preferencesHelper().clear();
                    ActivityUtil.getInstance().finishAll();
                    startActivity(LoginActivity.getStartIntent(getActivityContext()));
                }
            }
        });
        mLoadingDialog = new LoadingDialog(getActivityContext());
    }
    public void setContentView(int layoutId){
        super.setContentView(layoutId);
        mRootView =  ((ViewGroup) getActivityContext().findViewById(android.R.id.content));
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    protected View getLayout(int layoutId){
       return  getLayoutInflater().inflate(layoutId,null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mActivityId);
            sComponentsMap.remove(mActivityId);
        }
        ActivityUtil.getInstance().removeActivity(this);
        super.onDestroy();
    }

    protected Activity getActivityContext() {
        return BaseActivity.this;
    }

    protected void toast(String text) {
        ToastUtil.show(getActivityContext(), text);
    }

    protected void toast(int textId) {
        ToastUtil.show(getActivityContext(), getString(textId));
    }

    LoadingDialog mLoadingDialog;

    protected void showLoadingDialog() {
        mLoadingDialog.show();
    }

    protected void dismissLoadingDialog() {
        mLoadingDialog.dismiss();
    }


}

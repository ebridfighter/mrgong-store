package uk.co.ribot.androidboilerplate.ui.base;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.runwise.commomlibrary.view.LoadingDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.event.LogOutEvent;
import uk.co.ribot.androidboilerplate.injection.component.ConfigPersistentComponent;
import uk.co.ribot.androidboilerplate.injection.component.DaggerConfigPersistentComponent;
import uk.co.ribot.androidboilerplate.injection.component.ExampleActivityComponent;
import uk.co.ribot.androidboilerplate.ui.activity.LoginActivity;
import uk.co.ribot.androidboilerplate.util.ActivityUtil;
import uk.co.ribot.androidboilerplate.util.ToastUtil;
import uk.co.ribot.androidboilerplate.view.RunwiseDialog;

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
    @Inject
    protected RunwiseDialog mDialog;

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

    ViewHolder mViewHolder;

    public void setContentView(int layoutId) {
        LinearLayout parentView = (LinearLayout) getLayout(R.layout.layout_base);
        setContentView(parentView);

        mViewHolder = new ViewHolder(parentView);
        parentView.addView(getLayout(layoutId));
        mRootView = getActivityContext().findViewById(android.R.id.content);
    }

   protected int getTitleBarHeight(){
        return mViewHolder.mFlTitle.getHeight();
    }

    protected void setTitleRightText(String text){
        mViewHolder.mTvTitleRight.setText(text);
    }

    protected void hideTitleRightText(){
        mViewHolder.mTvTitleRight.setVisibility(View.GONE);
    }

    protected void hideTitleBar() {
        mViewHolder.mFlTitle.setVisibility(View.GONE);
    }

    public void showBackBtn() {
        mViewHolder.mIvTitileLeft.setImageResource(R.drawable.back_btn);
        mViewHolder.mIvTitileLeft.setVisibility(View.VISIBLE);
        mViewHolder.mIvTitileLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setTitle(int textId) {
        mViewHolder.mTvTitle.setVisibility(View.VISIBLE);
        mViewHolder.mTvTitle.setText(textId);
    }

    public void setTitle(String text) {
        mViewHolder.mTvTitle.setVisibility(View.VISIBLE);
        mViewHolder.mTvTitle.setText(text);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    protected View getLayout(int layoutId) {
        return getLayoutInflater().inflate(layoutId, null);
    }

    protected int getResIdByDrawableName(String name) {
        ApplicationInfo appInfo = getApplicationInfo();
        int resID = getResources().getIdentifier(name, "drawable", appInfo.packageName);
        return resID;
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

    public void showDialog(String title, String message, RunwiseDialog.DialogListener dialogListener) {
        mDialog.setTitle(title);
        mDialog.setMessage(message);
        mDialog.setMessageGravity();
        mDialog.setRightBtnListener("确认", dialogListener);
        mDialog.show();
    }

    public void showDialog(String title, String message, String confirmText, String cancelText,RunwiseDialog.DialogListener dialogListener) {
        mDialog.setTitle(title);
        mDialog.setMessage(message);
        mDialog.setMessageGravity();
        if (TextUtils.isEmpty(cancelText)){
            mDialog.setModel(RunwiseDialog.RIGHT);
        }
        mDialog.setRightBtnListener(confirmText, dialogListener);
        mDialog.show();
    }
    public void showNoCancelDialog(String title, String message, String confirmText,String cancelText,RunwiseDialog.DialogListener dialogListener) {
        mDialog.setTitle(title);
        mDialog.setMessage(message);
        mDialog.setMessageGravity();
        if (TextUtils.isEmpty(cancelText)){
            mDialog.setModel(RunwiseDialog.RIGHT);
        }
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setRightBtnListener(confirmText, dialogListener);
        mDialog.show();
    }

    static class ViewHolder {
        @BindView(R.id.iv_titile_left)
        ImageView mIvTitileLeft;
        @BindView(R.id.tv_titile_left)
        TextView mTvTitileLeft;
        @BindView(R.id.ll_left)
        LinearLayout mLlLeft;
        @BindView(R.id.iv_title_right2)
        ImageView mIvTitleRight2;
        @BindView(R.id.iv_title_right)
        ImageView mIvTitleRight;
        @BindView(R.id.tv_title_right)
        TextView mTvTitleRight;
        @BindView(R.id.ll_right)
        LinearLayout mLlRight;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.iv_title)
        ImageView mIvTitle;
        @BindView(R.id.ll_mid)
        LinearLayout mLlMid;
        @BindView(R.id.fl_title)
        FrameLayout mFlTitle;
        @BindView(R.id.ll_root)
        LinearLayout mLlRoot;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.iv_titile_left, R.id.iv_title_right2, R.id.iv_title_right, R.id.iv_title})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.iv_titile_left:
                    break;
                case R.id.iv_title_right2:
                    break;
                case R.id.iv_title_right:
                    break;
                case R.id.iv_title:
                    break;
            }
        }
    }


//
}

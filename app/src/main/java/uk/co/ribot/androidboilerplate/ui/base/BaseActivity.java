package uk.co.ribot.androidboilerplate.ui.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.runwise.commomlibrary.swipetoloadlayout.OnLoadMoreListener;
import com.runwise.commomlibrary.swipetoloadlayout.OnRefreshListener;
import com.runwise.commomlibrary.view.LoadingDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import rx.Observable;
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
import uk.co.ribot.androidboilerplate.util.RxEventBus;
import uk.co.ribot.androidboilerplate.util.ToastUtil;
import uk.co.ribot.androidboilerplate.view.RunwiseDialog;

/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public class BaseActivity extends AppCompatActivity implements BGASwipeBackHelper.Delegate,OnRefreshListener, OnLoadMoreListener {
    protected BGASwipeBackHelper mSwipeBackHelper;
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
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        View contentView = getLayout(layoutId);
        parentView.addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRootView = getActivityContext().findViewById(android.R.id.content);
    }

    protected int getTitleBarHeight() {
        return mViewHolder.mFlTitle.getHeight();
    }

    protected void setTitleRightText(String text, View.OnClickListener onClickListener) {
        mViewHolder.mTvTitleRight.setText(text);
        mViewHolder.mTvTitleRight.setOnClickListener(onClickListener);
    }

    protected void setTitleRightText(String text) {
        mViewHolder.mTvTitleRight.setText(text);
    }

    protected void hideTitleRightText() {
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

    public void showLeftBtn(int drawableId, View.OnClickListener onClickListener) {
        mViewHolder.mIvTitileLeft.setImageResource(drawableId);
        mViewHolder.mIvTitileLeft.setVisibility(View.VISIBLE);
        mViewHolder.mIvTitileLeft.setOnClickListener(onClickListener);
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

    protected void showLoadingDialog(String text) {
        mLoadingDialog.setMsg(text);
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

    public void showDialog(String title, String message, String confirmText, String cancelText, RunwiseDialog.DialogListener dialogListener) {
        mDialog.setTitle(title);
        mDialog.setMessage(message);
        mDialog.setMessageGravity();
        if (TextUtils.isEmpty(cancelText)) {
            mDialog.setModel(RunwiseDialog.RIGHT);
        }
        mDialog.setRightBtnListener(confirmText, dialogListener);
        mDialog.show();
    }

    public void showNoCancelDialog(String title, String message, String confirmText, String cancelText, RunwiseDialog.DialogListener dialogListener) {
        mDialog.setTitle(title);
        mDialog.setMessage(message);
        mDialog.setMessageGravity();
        if (TextUtils.isEmpty(cancelText)) {
            mDialog.setModel(RunwiseDialog.RIGHT);
        }
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setRightBtnListener(confirmText, dialogListener);
        mDialog.show();
    }

    protected RxEventBus getRxBus() {
        return BoilerplateApplication.get(getActivityContext()).getComponent().eventBus();
    }
    protected Observable<Object> getRxBusObservable(){
        return BoilerplateApplication.get(getActivityContext()).getComponent().eventBus().observable();
    }


    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    public void onSwipeBackLayoutSlide(float v) {

    }

    @Override
    public void onSwipeBackLayoutCancel() {

    }

    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

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

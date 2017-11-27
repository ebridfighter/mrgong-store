package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.runwise.commomlibrary.view.CustomBottomDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.event.LogOutEvent;
import uk.co.ribot.androidboilerplate.injection.component.UserInfoActivityComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.presenter.UserInfoPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.UserInfoMvpView;


/**
 * Created by mike on 2017/11/27.
 */
public class UserInfoActivity extends BaseActivity implements UserInfoMvpView {
    @Inject
    UserInfoPresenter mUserInfoPresenter;
    @BindView(R.id.sdv_fead_photo)
    SimpleDraweeView mSdvFeadPhoto;
    @BindView(R.id.iv_more)
    ImageView mIvMore;
    @BindView(R.id.rl_head_photo)
    RelativeLayout mRlHeadPhoto;
    @BindView(R.id.tv_nick)
    TextView mTvNick;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.rl_user_name)
    RelativeLayout mRlUserName;
    @BindView(R.id.tv_account_text)
    TextView mTvAccountText;
    @BindView(R.id.tv_account)
    TextView mTvAccount;
    @BindView(R.id.rl_account)
    RelativeLayout mRlAccount;
    @BindView(R.id.tv_area_tag)
    TextView mTvAreaTag;
    @BindView(R.id.tv_area)
    TextView mTvArea;
    @BindView(R.id.rl_area)
    RelativeLayout mRlArea;
    @BindView(R.id.tv_store_tag)
    TextView mTvStoreTag;
    @BindView(R.id.tv_store)
    TextView mTvStore;
    @BindView(R.id.rl_store)
    RelativeLayout mRlStore;
    @BindView(R.id.tv_phone_tag)
    TextView mTvPhoneTag;
    @BindView(R.id.iv_phone_more)
    ImageView mIvPhoneMore;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.rl_phone)
    RelativeLayout mRlPhone;
    @BindView(R.id.tv_password_tag)
    TextView mTvPasswordTag;
    @BindView(R.id.iv_password_more)
    ImageView mIvPasswordMore;
    @BindView(R.id.tv_password)
    TextView mTvPassword;
    @BindView(R.id.rl_password)
    RelativeLayout mRlPassword;
    @BindView(R.id.tv_exit)
    TextView mTvExit;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        setTitle(R.string.title_person_info);
        showBackBtn();
        UserInfoActivityComponent activityComponent = configPersistentComponent.userInfoActivityComponent(new ActivityModule(this));
        activityComponent.inject(this);
        mUserInfoPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserInfoPresenter.detachView();
    }

    @OnClick({R.id.rl_head_photo, R.id.rl_phone, R.id.rl_password, R.id.tv_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_head_photo:
                break;
            case R.id.rl_phone:
                break;
            case R.id.rl_password:
                break;
            case R.id.tv_exit:
                showLogoutDialog();
                break;
        }
    }

    @Override
    public void logoutError() {
        dismissLoadingDialog();
        toast(R.string.toast_logout_fail);
    }

    @Override
    public void logout() {
        dismissLoadingDialog();
        getRxBus().post(new LogOutEvent());
    }
    private void showLogoutDialog() {
        final CustomBottomDialog mLogoutDialog = new CustomBottomDialog(this);
        ArrayMap<Integer, String> menus = new ArrayMap<>();
        menus.put(0, getString(R.string.menu_logout));
        mLogoutDialog.addItemViews(menus);
        mLogoutDialog.setOnBottomDialogClick(view -> {
            switch (view.getId()) {
                case 0:
                    showLoadingDialog();
                    mUserInfoPresenter.logout();
                    break;
            }
            mLogoutDialog.dismiss();
        });
        mLogoutDialog.show();
    }
}

package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.HostResponse;
import uk.co.ribot.androidboilerplate.injection.component.LoginActivityComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.presenter.LoginPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.LoginMvpView;
import uk.co.ribot.androidboilerplate.view.ClearEditText;

import static uk.co.ribot.androidboilerplate.R.id.teacher_reg_phone;

public class LoginActivity extends BaseActivity implements LoginMvpView {

    @BindView(R.id.closeBtn)
    ImageView mCloseBtn;
    @BindView(teacher_reg_phone)
    ClearEditText mTeacherRegPhone;
    @BindView(R.id.login_pop_btn)
    ImageView mLoginPopBtn;
    @BindView(R.id.topLayout)
    LinearLayout mTopLayout;
    @BindView(R.id.teacher_reg_password)
    EditText mTeacherRegPassword;
    @BindView(R.id.teacher_reg_password_see)
    CheckBox mTeacherRegPasswordSee;
    @BindView(R.id.remPassword)
    CheckBox mRemPassword;
    @BindView(R.id.login_find)
    TextView mLoginFind;
    @BindView(R.id.login_btn)
    Button mLoginBtn;
    @BindView(R.id.login_register)
    TextView mLoginRegister;
    @BindView(R.id.root_layout)
    LinearLayout mRootLayout;

    @Inject
    LoginPresenter mLoginPresenter;
    @BindView(R.id.cet_company)
    ClearEditText mCetCompany;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        hideTitleBar();
        LoginActivityComponent activityComponent = configPersistentComponent.loginActivityComponent(new ActivityModule(this));
        activityComponent.inject(this);
        mLoginPresenter.attachView(this);
        if (mLoginPresenter.isLogin()) {
            finish();
            startActivity(MainActivity.getStartIntent(getActivityContext()));
        }
    }

    @OnClick(R.id.login_btn)
    public void onViewClicked() {
        showLoadingDialog("登录中...");
        mLoginPresenter.getHost(mCetCompany.getText().toString());
    }

    @Override
    public void onSuccess() {
        dismissLoadingDialog();
        toast("登录成功");
        startActivity(MainActivity.getStartIntent(LoginActivity.this));
    }

    @Override
    public void showError(String error) {
        dismissLoadingDialog();
        toast(error);
    }

    @Override
    public void loginConflict() {
        toast("登录冲突");
        dismissLoadingDialog();
    }

    @Override
    public void getHostSuccess(HostResponse hostResponse) {
        mLoginPresenter.saveHost(hostResponse.getHost());
        mLoginPresenter.saveDataBase(hostResponse.getDbName());
        mLoginPresenter.login(mTeacherRegPhone.getText().toString().trim(), mTeacherRegPassword.getText().toString().trim());
    }

    @Override
    public void getHostError(String error) {
        toast(error);
        dismissLoadingDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.detachView();
    }
}

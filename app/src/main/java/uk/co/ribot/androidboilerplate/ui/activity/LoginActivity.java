package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.nio.channels.Selector;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.database.UserBean;
import uk.co.ribot.androidboilerplate.injection.component.LoginActivityComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.tools.LoginPopWindowUtil;
import uk.co.ribot.androidboilerplate.ui.adapter.LoginUserListAdapter;
import uk.co.ribot.androidboilerplate.ui.adapter.base.BaseAdapter;
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
    LoginPopWindowUtil mLoginPopWindowUtil;
    List<UserBean> mUserBeanList;

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
        mLoginPopWindowUtil = new LoginPopWindowUtil(getActivityContext());
        mLoginPresenter.attachView(this);
        if (mLoginPresenter.isLogin()) {
            finish();
            startActivity(MainActivity.getStartIntent(getActivityContext()));
        }
        mLoginPresenter.loadUserList();
    }

    @OnClick({R.id.login_btn, R.id.login_pop_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                showLoadingDialog("登录中...");
                mLoginPresenter.login(mCetCompany.getText().toString(), mTeacherRegPhone.getText().toString().trim(), mTeacherRegPassword.getText().toString().trim());
                break;
            case R.id.login_pop_btn:
                    //没多个历史帐号时，不需要有下拉箭头
                    if (mUserBeanList != null && !mUserBeanList.isEmpty() && mUserBeanList.size() > 1) {
                        mLoginPopWindowUtil.showPop(mTopLayout, mLoginPopBtn);
                    }
                break;
        }


    }

    @Override
    public void onSuccess() {
        dismissLoadingDialog();
        toast("登录成功");
        startActivity(MainActivity.getStartIntent(LoginActivity.this));
//        ARouter.getInstance().build("/test/InventoryActivity").navigation();
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
    public void getHostError(String error) {
        if (TextUtils.isEmpty(error)) {
            toast(R.string.toast_company_no_exist);
        } else {
            toast(error);
        }
        dismissLoadingDialog();
    }

    @Override
    public void loadUserListSuccess(List<UserBean> userBeanList) {
        mUserBeanList = userBeanList;
        if (mUserBeanList == null ||mUserBeanList.size() == 0){
            mLoginPopBtn.setVisibility(View.INVISIBLE);
            return;
        }
        UserBean userBean = userBeanList.get(0);
        setCurrentUser(userBean);

       LoginUserListAdapter loginUserListAdapter = new LoginUserListAdapter(userBeanList);
        mLoginPopWindowUtil.setAdapter(loginUserListAdapter);
        mLoginPopWindowUtil.addOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                设置点击的用户作为当前用户
                UserBean userBean = userBeanList.get(position);
                setCurrentUser(userBean);
                mLoginPopWindowUtil.hidePop();
            }
        });
        mLoginPopWindowUtil.addOnChildItemClickListener(new BaseAdapter.OnChildItemClickListener() {
            @Override
            public void onItemClick(View childView, int position) {
                if (childView.getId() == R.id.deleteImg){
//                    删除数据库中的用户
                    mLoginPresenter.deleteUser(userBeanList.get(position));

                }
            }
        });
    }

    public void setCurrentUser(UserBean currentUser){
        mTeacherRegPhone.setText(currentUser.getUserName());
        mCetCompany.setText(currentUser.getCompanyName());
        mTeacherRegPassword.setText(currentUser.getPassword());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.detachView();
    }


}

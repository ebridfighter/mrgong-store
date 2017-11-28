package uk.co.ribot.androidboilerplate.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.runwise.commomlibrary.swipetoloadlayout.OnLoadMoreListener;
import com.runwise.commomlibrary.swipetoloadlayout.OnRefreshListener;
import com.runwise.commomlibrary.view.LoadingDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.injection.component.frament.DaggerFragmentBaseComponent;
import uk.co.ribot.androidboilerplate.injection.component.frament.FragmentBaseComponent;
import uk.co.ribot.androidboilerplate.util.RxEventBus;
import uk.co.ribot.androidboilerplate.view.RunwiseDialog;

/**
 * Created by mike on 2017/10/31.
 */

public class BaseFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {

    protected FragmentBaseComponent mFragmentBaseComponent;
    @Inject
    protected RunwiseDialog mDialog;

    ViewGroup mRootView;
    LoadingDialog mLoadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return null;
    }

    ViewHolder mViewHolder;

    protected View warpTitleView(View contentView) {
        mRootView = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.layout_base, null);
        mViewHolder = new ViewHolder(mRootView);
        mRootView.addView(contentView);
        return mRootView;
    }

    public void showBackBtn() {
        mViewHolder.mIvTitileLeft.setImageResource(R.drawable.back_btn);
        mViewHolder.mIvTitileLeft.setVisibility(View.VISIBLE);
        mViewHolder.mIvTitileLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    protected void setTitle(int textId) {
        mViewHolder.mTvTitle.setVisibility(View.VISIBLE);
        mViewHolder.mTvTitle.setText(textId);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFragmentBaseComponent = DaggerFragmentBaseComponent.builder().
                applicationComponent(BoilerplateApplication.get(getActivity()).getComponent())
                .build();
        mLoadingDialog = new LoadingDialog(getActivity());
    }

    protected View getLayout(int layoutId) {
        return LayoutInflater.from(getActivity()).inflate(layoutId, null);
    }

    protected void toast(int textId) {
        Toast.makeText(getActivity(), getString(textId), Toast.LENGTH_SHORT).show();
    }

    protected void toast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    protected boolean isTextViewEmpty(TextView textView) {
        return TextUtils.isEmpty(textView.getText().toString());
    }

    public void showDialog(String title, String message, RunwiseDialog.DialogListener dialogListener) {
        mDialog.setTitle(title);
        mDialog.setMessage(message);
        mDialog.setMessageGravity();
        mDialog.setRightBtnListener("确认", dialogListener);
        mDialog.show();
    }

    public void showDialog(String title, String message, String confirmText, RunwiseDialog.DialogListener dialogListener) {
        mDialog.setTitle(title);
        mDialog.setMessage(message);
        mDialog.setMessageGravity();
        mDialog.setRightBtnListener(confirmText, dialogListener);
        mDialog.show();
    }

    protected void showLoadingDialog(String text) {
        mLoadingDialog.setMsg(text);
        mLoadingDialog.show();
    }

    protected void showLoadingDialog() {
        mLoadingDialog.show();
    }

    protected void dismissLoadingDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    protected Observable<Object> getRxBusObservable() {
        return BoilerplateApplication.get(getActivity()).getComponent().eventBus().observable();
    }

    protected RxEventBus getRxBus() {
        return BoilerplateApplication.get(getActivity()).getComponent().eventBus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

}

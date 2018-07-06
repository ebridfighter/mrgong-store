package uk.co.ribot.androidboilerplate.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.LastBuyResponse;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.activity.IntelligentPlaceOrderActivity;
import uk.co.ribot.androidboilerplate.ui.activity.PlaceOrderProductListImproveActivity;
import uk.co.ribot.androidboilerplate.ui.activity.SelfHelpPlaceOrderActivity;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.PlaceOrderPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.PlaceOrderMvpView;
import uk.co.ribot.androidboilerplate.view.SystemUpgradeLayout;

/**
 * Created by mike on 2017/10/31.
 * 下单fragment
 */

public class PlaceOrderFragment extends BaseFragment implements PlaceOrderMvpView {

    @BindView(R.id.tv_self_help)
    TextView mTvSelfHelp;
    @BindView(R.id.layout_system_upgrade_notice)
    SystemUpgradeLayout mLayoutSystemUpgradeNotice;
    @BindView(R.id.tv_last_buy)
    TextView mTvLastBuy;
    @BindView(R.id.tv_yg)
    TextView mTvYg;
    @BindView(R.id.tv_day_week)
    TextView mTvDayWeek;
    @BindView(R.id.tv_line)
    TextView mTvLine;
    @BindView(R.id.tv_m)
    TextView mTvM;
    @BindView(R.id.et_money)
    EditText mEtMoney;
    @BindView(R.id.tv_safe)
    TextView mTvSafe;
    @BindView(R.id.tv_safe_value)
    TextView mTvSafeValue;
    @BindView(R.id.btn_sure)
    Button mBtnSure;
    Unbinder unbinder;
    @Inject
    PlaceOrderPresenter mPlaceOrderPresenter;
    private OptionsPickerView mOptionsPickerView;
    private List<String> mSafeArr = new ArrayList<>();
    private int mSelectedIndex = 109;                              //最近一次所选,默认在+10上

    @Inject
    public PlaceOrderFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_place_order, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFragmentBaseComponent.placeOrderFragmentComponent(new ActivityModule(getActivity())).inject(this);
        mPlaceOrderPresenter.attachView(this);
        mTvLastBuy.setVisibility(mPlaceOrderPresenter.canSeePrice() ? View.VISIBLE : View.GONE);
        mPlaceOrderPresenter.getLastBuy();
        initSafeArr();
        mEtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBtnSure.setEnabled(s.length() > 0);
                float alpha = s.length() > 0 ? 1.0F : 0.4F;
                mBtnSure.setAlpha(alpha);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mPlaceOrderPresenter.detachView();
    }

    @OnClick({R.id.tv_self_help, R.id.tv_day_week, R.id.tv_safe, R.id.tv_safe_value, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_self_help:
                startActivity(PlaceOrderProductListImproveActivity.getStartIntent(getActivity()));
                break;
            case R.id.tv_day_week:
                break;
            case R.id.tv_safe:
                break;
            case R.id.tv_safe_value:
                showSafeValueDialog();
                break;
            case R.id.btn_sure:
                if (checkInput()) {
                    startActivity(IntelligentPlaceOrderActivity.getStartIntent(getActivity(), Double.parseDouble(mEtMoney.getText().toString())
                            , Double.parseDouble(mSafeArr.get(mSelectedIndex))));
                }
                break;
        }
    }

    private void initSafeArr() {
        for (int i = -99; i <= 99; i++) {
            String str = i < 0 ? String.valueOf(i) : ("+" + i);
            mSafeArr.add(str);
        }
    }

    private void showSafeValueDialog() {
        if (mOptionsPickerView == null) {
            mOptionsPickerView = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {

                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    if (mSafeArr.size() > options1) {
                        mSelectedIndex = options1;
                        String selectStr = mSafeArr.get(options1);
                        mTvSafeValue.setText(selectStr + " %");
                    }
                }
            }).setTitleText(getString(R.string.dialog_title_safe_factor))
                    .setTitleBgColor(Color.parseColor(getString(R.string.color_titile_safe_dialog)))
                    .setTitleSize(16)
                    .setSubCalSize(14)
                    .setContentTextSize(23)
                    .setCancelColor(Color.parseColor(getString(R.string.color_cancle_text_safe_dialog)))
                    .setSubmitColor(Color.parseColor(getString(R.string.color_submit_text_safe_dialog)))
                    .build();
            mOptionsPickerView.setPicker(mSafeArr);
        }
        mOptionsPickerView.setSelectOptions(mSelectedIndex);
        mOptionsPickerView.show(true);
    }

    boolean checkInput() {
        if (isTextViewEmpty(mEtMoney)) {
            toast(R.string.toast_estimated_turnover_empty);
            return false;
        }
        if (isTextViewEmpty(mTvSafeValue)) {
            toast(R.string.toast_safety_factor_empty);
            return false;
        }
        return true;
    }

    @Override
    public void showLastOrderAmount(LastBuyResponse lastBuyResponse) {
        double amount = lastBuyResponse.getAmout();
        DecimalFormat df = new DecimalFormat("#.#");
        mTvLastBuy.setText("上次采购额 ¥" + df.format(amount));
    }

    @Override
    public void showLastOrderAmountError() {

    }
}

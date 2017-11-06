package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.view.SystemUpgradeLayout;

/**
 * Created by mike on 2017/10/31.
 * 下单fragment
 */

public class PlaceOrderFragment extends BaseFragment {


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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_place_irder, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_self_help, R.id.tv_day_week, R.id.tv_safe, R.id.tv_safe_value, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_self_help:
                break;
            case R.id.tv_day_week:
                break;
            case R.id.tv_safe:
                break;
            case R.id.tv_safe_value:
                break;
            case R.id.btn_sure:
                break;
        }
    }
}

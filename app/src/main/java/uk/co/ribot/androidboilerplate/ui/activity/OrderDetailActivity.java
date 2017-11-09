package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.ui.adapter.FragmentAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.fragment.OrderProductFragment;

public class OrderDetailActivity extends BaseActivity {

    public static final String INTENT_KEY_ORDER_ID = "intent_key_order_id";
    @BindView(R.id.tl_category)
    TabLayout mTlCategory;
    @BindView(R.id.vp)
    ViewPager mVp;

    List<Fragment> mFragmentList;
    List<String> mTitleList;

    public static Intent getStartIntent(Context context, int orderId) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(INTENT_KEY_ORDER_ID, orderId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);

        setUpFragmentList();
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mTitleList, mFragmentList);
        mVp.setAdapter(fragmentAdapter);
        mVp.setOffscreenPageLimit(mFragmentList.size());
        mTlCategory.setupWithViewPager(mVp);
    }

    private void setUpFragmentList() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new OrderProductFragment());
        mFragmentList.add(new OrderProductFragment());
        mFragmentList.add(new OrderProductFragment());
        mFragmentList.add(new OrderProductFragment());
        mFragmentList.add(new OrderProductFragment());

        mTitleList = new ArrayList<>();
        mTitleList.add(getString(R.string.fragment_home_page));
        mTitleList.add(getString(R.string.fragment_palce_order));
        mTitleList.add(getString(R.string.fragment_stock));
        mTitleList.add(getString(R.string.fragment_message));
        mTitleList.add(getString(R.string.fragment_more));
    }

}

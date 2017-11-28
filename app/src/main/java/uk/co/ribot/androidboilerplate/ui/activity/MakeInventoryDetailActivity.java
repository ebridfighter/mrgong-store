package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import github.chenupt.dragtoplayout.DragTopLayout;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.InventoryResponse;
import uk.co.ribot.androidboilerplate.injection.component.MakeInventoryDetailActivityComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.presenter.MakeInventoryDetailPresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.MakeInventoryDetailMvpView;


/**
 * Created by mike on 2017/11/28.
 */
public class MakeInventoryDetailActivity extends BaseActivity implements MakeInventoryDetailMvpView {
    @Inject
    MakeInventoryDetailPresenter mMakeInventoryDetailPresenter;
    @BindView(R.id.tv_1)
    TextView mTv1;
    @BindView(R.id.tv_2)
    TextView mTv2;
    @BindView(R.id.tv_3)
    TextView mTv3;
    @BindView(R.id.tv_4)
    TextView mTv4;
    @BindView(R.id.tv_5)
    TextView mTv5;
    @BindView(R.id.ll_top_view)
    LinearLayout mLlTopView;
    @BindView(R.id.vp)
    ViewPager mVp;
    @BindView(R.id.drag_content_view)
    LinearLayout mDragContentView;
    @BindView(R.id.drag_layout)
    DragTopLayout mDragLayout;

    public static final String INTENT_KEY_INVENTORY_RESPONSE_LISTBEAN = "intent_key_inventory_response_listbean";

    public static Intent getStartIntent(Context context, InventoryResponse.ListBean listBean) {
        Intent intent = new Intent(context, MakeInventoryDetailActivity.class);
        intent.putExtra(INTENT_KEY_INVENTORY_RESPONSE_LISTBEAN, listBean);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_inventory_detail);
        ButterKnife.bind(this);
        MakeInventoryDetailActivityComponent activityComponent = configPersistentComponent.makeInventoryDetailActivityComponent(new ActivityModule(this));
        activityComponent.inject(this);
        mMakeInventoryDetailPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMakeInventoryDetailPresenter.detachView();
    }
}

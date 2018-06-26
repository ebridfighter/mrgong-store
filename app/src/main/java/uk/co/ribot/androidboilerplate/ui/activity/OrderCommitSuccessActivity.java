package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.OrderListResponse;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.OrderCommitAdapter;
import uk.co.ribot.androidboilerplate.ui.adapter.base.BaseAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;
import uk.co.ribot.androidboilerplate.ui.presenter.CommonPresenter;

public class OrderCommitSuccessActivity extends BaseActivity implements MvpView {
    @Inject
    OrderCommitAdapter mOrderCommitAdapter;
    @BindView(R.id.rv_order_commit_sucess)
    RecyclerView mRvOrderCommitSucess;
    @Inject
    CommonPresenter mCommonPresenter;
    public static final String INTENT_KEY_ORDERS = "intent_key_orders";
    public static final String INTENT_KEY_TYPE = "intent_key_type";
    private static final int REQ_ACT_UPLOAD = 0x1234;
    private List<OrderListResponse.ListBean> mListOrders;

    public static Intent getStartIntent(Context context, int type, ArrayList<OrderListResponse.ListBean> listOrders) {
        Intent intent = new Intent(context, OrderCommitSuccessActivity.class);
        intent.putExtra(INTENT_KEY_TYPE, type);
        intent.putExtra(INTENT_KEY_ORDERS, listOrders);
        return intent;
    }

    private int mType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_commit_success);
        ButterKnife.bind(this);
        showBackBtn();
        configPersistentComponent.orderCommitSuccessActivityComponent(new ActivityModule(this)).inject(this);
        mType = getIntent().getIntExtra(INTENT_KEY_TYPE, -1);
        mListOrders = (List<OrderListResponse.ListBean>) getIntent().getSerializableExtra(INTENT_KEY_ORDERS);
        setTitle(mType == 0 ? getString(R.string.title_modify_order_success) : getString(R.string.title_place_order_success));
        mCommonPresenter.attachView(this);
        mOrderCommitAdapter.setCanSeePrice(mCommonPresenter.canSeePrice());
        mOrderCommitAdapter.setType(mType);
        if (mListOrders.get(0).getOrderSettleName().contains("单次结算")){
            mOrderCommitAdapter.setShowUploadButton(true);
        }else{
            mOrderCommitAdapter.setShowUploadButton(false);
        }
        if (mListOrders.get(0).getOrderSettleName().contains("先付款后收货")&&mListOrders.get(0).getOrderSettleName().contains("单次结算")){
            mOrderCommitAdapter.setShowUploadButton(true);
        }

        mOrderCommitAdapter.setData(mListOrders);
        mOrderCommitAdapter.setOnChildItemClickListener(new BaseAdapter.OnChildItemClickListener() {
            @Override
            public void onItemClick(View childView, int position) {
//                Intent uIntent = new Intent(getActivityContext(), UploadPayedPicActivity.class);
//                uIntent.putExtra("orderid", bean.getOrderID());
//                uIntent.putExtra("ordername", bean.getName());
//                uIntent.putExtra("hasattachment", type == 0);
//                startActivityForResult(uIntent, REQ_ACT_UPLOAD);
                if (childView.getId() == R.id.tv_order_action){
                    startActivity(OrderDetailActivity.getStartIntent(getActivityContext(), mListOrders.get(position).getOrderID()));
                }
            }
        });
        mRvOrderCommitSucess.setLayoutManager(new LinearLayoutManager(this));
        mRvOrderCommitSucess.setAdapter(mOrderCommitAdapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_ACT_UPLOAD && resultCode==200 && data.getBooleanExtra("upload_success",false)){
            finish();
        }
    }
}

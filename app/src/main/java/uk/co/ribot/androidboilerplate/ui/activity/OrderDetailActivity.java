package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.runwise.commomlibrary.swipetoloadlayout.RefreshRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;

public class OrderDetailActivity extends BaseActivity {

    public static final String INTENT_KEY_ORDER_ID = "intent_key_order_id";
    @BindView(R.id.rrv_product)
    RefreshRecyclerView mRrvProduct;

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
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("test");
        stringArrayList.add("test");
        stringArrayList.add("test");
        stringArrayList.add("test");
        mRrvProduct.setAdapter(new CommonAdapter<String>(getActivityContext(),R.layout.item_ribot,stringArrayList) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.text_name, s);
            }
            @Override
            public void onViewHolderCreated(ViewHolder holder, View itemView)
            {
                super.onViewHolderCreated(holder, itemView);
                //AutoUtil.autoSize(itemView)
            }
        });
    }
}

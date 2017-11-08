package uk.co.ribot.androidboilerplate.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.MessageResponse;
import uk.co.ribot.androidboilerplate.tools.TimeUtils;
import uk.co.ribot.androidboilerplate.tools.UserUtils;

/**
 * Created by leo on 17-11-3.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.RibotViewHolder> {

    private List<MessageResponse.OrderBean> mRibots;

    public void setCanSeePrice(boolean mcanSeePrice) {
        isCanSeePrice = mcanSeePrice;
    }

    private boolean isCanSeePrice;

    @Inject
    public MessageAdapter() {
        mRibots = new ArrayList<>();
    }
    public void setRibots(List<MessageResponse.OrderBean> ribots) {
        mRibots = ribots;
    }


    @Override
    public RibotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new RibotViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RibotViewHolder holder, int position) {
        MessageResponse.OrderBean ribot = mRibots.get(position);
        boolean normalOrder;
        if(ribot.getName().startsWith("RO")) {
            normalOrder = false;
        }
        else{
            normalOrder = true;
        }
        holder.mNameTextView.setText(ribot.getName());
        holder.mDateTextView.setText(TimeUtils.getTimeStamps3(ribot.getCreate_date()));
        UserUtils.setOrderStatus(ribot.getState(),holder.mStateTextView,holder.mIconImageView,normalOrder);
        if(isCanSeePrice) {
            holder.mContextTextView.setText("共"+ribot.getAmount()+"件商品,¥"+ ribot.getAmount_total());
        }
        else {
            holder.mContextTextView.setText("共"+ribot.getAmount()+"件商品");
        }

        if(ribot.getLast_message() != null && !TextUtils.isEmpty(ribot.getLast_message().getBody())) {
            holder.mMsgTextView.setVisibility(View.VISIBLE);
            holder.mMsgTextView.setText(ribot.getLast_message().getBody());
            if(!ribot.getLast_message().isSeen()) {
                holder.mHexColorView.setVisibility(View.VISIBLE);
            }
            else {
                holder.mHexColorView.setVisibility(View.GONE);
            }
        }
        else {
            holder.mMsgTextView.setVisibility(View.GONE);
            holder.mHexColorView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mRibots.size();
    }

    class RibotViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chatUnRead)
        View mHexColorView;
        @BindView(R.id.chatName)
        TextView mNameTextView;
        @BindView(R.id.chatIcon)
        ImageView mIconImageView;
        @BindView(R.id.chatMsg)
        TextView mMsgTextView;

        @BindView(R.id.chatContext)
        TextView mContextTextView;

        @BindView(R.id.chatStatus)
        TextView mStateTextView;
        @BindView(R.id.chatTime)
        TextView mDateTextView;


        public RibotViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

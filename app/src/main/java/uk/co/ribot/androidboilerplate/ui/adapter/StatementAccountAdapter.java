package uk.co.ribot.androidboilerplate.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.StatementAccountListResponse;
import uk.co.ribot.androidboilerplate.tools.TimeUtils;
import uk.co.ribot.androidboilerplate.tools.UserUtils;
import uk.co.ribot.androidboilerplate.ui.adapter.base.BaseAdapter;

/**
 * Created by mike on 2017/11/28.
 */

public class StatementAccountAdapter extends BaseAdapter<StatementAccountAdapter.ViewHolder> {

    ArrayList<StatementAccountListResponse.BankStatementBean> mBankStatementBeans = new ArrayList<>();

    @Inject
    public StatementAccountAdapter() {
    }

    public void setData(List<StatementAccountListResponse.BankStatementBean> bankStatementBeans) {
        mBankStatementBeans.clear();
        mBankStatementBeans.addAll(bankStatementBeans);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayout(parent.getContext(), R.layout.item_statement_account);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StatementAccountListResponse.BankStatementBean bankStatementBean = mBankStatementBeans.get(position);
        holder.mTvDate.setText(TimeUtils.getTimeStamps4(bankStatementBean.getBeginDate()) + "至" + TimeUtils.getTimeStamps4(bankStatementBean.getEndDate()));
        holder.mTvName.setText(bankStatementBean.getName());
        if(!TimeUtils.isTimeInner(bankStatementBean.getBeginDate(),bankStatementBean.getEndDate())) {
            bankStatementBean.setTimeLater(true);
            holder.mTvStatus.setVisibility(View.VISIBLE);
        }
        else {
            bankStatementBean.setTimeLater(false);
            holder.mTvStatus.setVisibility(View.GONE);
        }
        holder.mTvMoney.setText("￥"+ UserUtils.formatPrice(bankStatementBean.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView mTvDate;
        @BindView(R.id.tv_status)
        TextView mTvStatus;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_money)
        TextView mTvMoney;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

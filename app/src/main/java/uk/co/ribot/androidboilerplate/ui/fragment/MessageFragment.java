package uk.co.ribot.androidboilerplate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.net.response.MessageResponse;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.adapter.MessageAdapter;
import uk.co.ribot.androidboilerplate.ui.base.BaseFragment;
import uk.co.ribot.androidboilerplate.ui.presenter.MessagePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.MessageMvpView;

/**
 * Created by mike on 2017/10/31.
 * 消息Frgament
 */

public class MessageFragment extends BaseFragment implements MessageMvpView {

    @Inject
    MessagePresenter mMessagePresenter;
    @Inject
    MessageAdapter mMessageAdapter;

    Unbinder unbinder;
    @BindView(R.id.rv_message)
    RecyclerView rvMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_message, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MessageFragmentComponent messageFragmentComponent = mFragmentBaseComponent.
                messageFragmentComponent(new ActivityModule(getActivity()));
        messageFragmentComponent.inject(this);
        rvMessage.setAdapter(mMessageAdapter);
        rvMessage.setLayoutManager(new LinearLayoutManager(getActivity()));


        mMessagePresenter.attachView(this);
        mMessagePresenter.getMessages();
        mMessageAdapter.setCanSeePrice(mMessagePresenter.isCanSeePrice());

    }

    @Override
    public void showMessage(List<MessageResponse.OrderBean> message) {
        mMessageAdapter.setRibots(message);
        mMessageAdapter.notifyDataSetChanged();

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showMessagesEmpty() {

    }

    @Override
    public void showMessagesError() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

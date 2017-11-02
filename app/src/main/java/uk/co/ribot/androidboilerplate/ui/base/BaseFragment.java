package uk.co.ribot.androidboilerplate.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.injection.component.DaggerFragmentBaseComponent;
import uk.co.ribot.androidboilerplate.injection.component.FragmentBaseComponent;

/**
 * Created by mike on 2017/10/31.
 */

public class BaseFragment extends Fragment {

    protected FragmentBaseComponent mFragmentBaseComponent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.navigation_bar,container);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFragmentBaseComponent = DaggerFragmentBaseComponent.builder().
                applicationComponent(BoilerplateApplication.get(getActivity()).getComponent())
                .build();
    }

    protected View getLayout(int layoutId){
       return LayoutInflater.from(getActivity()).inflate(layoutId,null);
    }

    protected void toast(int textId){
        Toast.makeText(getActivity(), getString(textId), Toast.LENGTH_SHORT).show();
    }
}

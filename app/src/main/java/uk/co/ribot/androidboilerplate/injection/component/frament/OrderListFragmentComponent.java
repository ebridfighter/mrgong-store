package uk.co.ribot.androidboilerplate.injection.component.frament;


import dagger.Subcomponent;
import uk.co.ribot.androidboilerplate.injection.PerActivity;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.fragment.OrderListFragment;


/**
 * Created by mike on 2017/11/22.
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface OrderListFragmentComponent {
    void inject(OrderListFragment fragment);
}

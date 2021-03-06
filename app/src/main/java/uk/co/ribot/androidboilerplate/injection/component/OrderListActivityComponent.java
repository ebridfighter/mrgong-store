package uk.co.ribot.androidboilerplate.injection.component;


import dagger.Subcomponent;
import uk.co.ribot.androidboilerplate.injection.PerActivity;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.activity.OrderListActivity;


/**
 * Created by mike on 2017/11/22.
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface OrderListActivityComponent {
    void inject(OrderListActivity activity);
}

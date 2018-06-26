package uk.co.ribot.androidboilerplate.injection.component;

import dagger.Subcomponent;
import uk.co.ribot.androidboilerplate.injection.PerActivity;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.activity.PriceListActivity;

/**
 * Created by mike on 2017/10/31.
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ProductListActivityComponent {
    void inject(PriceListActivity productListActivity);
}

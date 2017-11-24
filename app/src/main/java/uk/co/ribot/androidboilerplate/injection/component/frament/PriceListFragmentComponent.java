package uk.co.ribot.androidboilerplate.injection.component.frament;


import dagger.Subcomponent;
import uk.co.ribot.androidboilerplate.injection.PerActivity;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;

import uk.co.ribot.androidboilerplate.ui.fragment.PriceListFragment;
import dagger.Component;


/**
 * Created by mike on 2017/11/24.
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface PriceListFragmentComponent {
    void inject(PriceListFragment fragment);
}

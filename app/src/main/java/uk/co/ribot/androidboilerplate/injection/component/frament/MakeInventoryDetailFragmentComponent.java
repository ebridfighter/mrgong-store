package uk.co.ribot.androidboilerplate.injection.component.frament;


import dagger.Subcomponent;
import uk.co.ribot.androidboilerplate.injection.PerActivity;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;

import uk.co.ribot.androidboilerplate.ui.fragment.MakeInventoryDetailFragment;
import dagger.Component;


/**
 * Created by mike on 2017/11/28.
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface MakeInventoryDetailFragmentComponent {
    void inject(MakeInventoryDetailFragment fragment);
}

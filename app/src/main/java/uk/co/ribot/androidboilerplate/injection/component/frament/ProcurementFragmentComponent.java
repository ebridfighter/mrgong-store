package uk.co.ribot.androidboilerplate.injection.component.frament;


import dagger.Subcomponent;
import uk.co.ribot.androidboilerplate.injection.PerActivity;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;

import uk.co.ribot.androidboilerplate.ui.fragment.ProcurementFragment;
import dagger.Component;


/**
 * Created by mike on 2018/1/4.
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ProcurementFragmentComponent {
    void inject(ProcurementFragment fragment);
}

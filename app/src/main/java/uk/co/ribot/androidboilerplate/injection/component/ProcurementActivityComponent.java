package uk.co.ribot.androidboilerplate.injection.component;


import dagger.Subcomponent;
import uk.co.ribot.androidboilerplate.injection.PerActivity;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.activity.ProcurementActivity;


/**
 * Created by mike on 2018/1/4.
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ProcurementActivityComponent {
    void inject(ProcurementActivity activity);
}

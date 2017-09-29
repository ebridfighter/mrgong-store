package uk.co.ribot.androidboilerplate.injection.component;

import dagger.Subcomponent;
import uk.co.ribot.androidboilerplate.injection.PerActivity;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.activity.MainActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface MainActivityComponent {

    void inject(MainActivity mainActivity);

}

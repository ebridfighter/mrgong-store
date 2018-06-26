package uk.co.ribot.androidboilerplate.injection.component;

import dagger.Subcomponent;
import uk.co.ribot.androidboilerplate.injection.PerActivity;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.activity.LoginActivity;

/**
 * Created by mike on 2017/9/28.
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface LoginActivityComponent {
    void inject(LoginActivity loginActivity);
}

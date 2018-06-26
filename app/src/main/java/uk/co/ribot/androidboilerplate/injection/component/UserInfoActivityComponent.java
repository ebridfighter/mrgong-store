package uk.co.ribot.androidboilerplate.injection.component;


import dagger.Subcomponent;
import uk.co.ribot.androidboilerplate.injection.PerActivity;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.activity.UserInfoActivity;


/**
 * Created by mike on 2017/11/27.
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface UserInfoActivityComponent {
    void inject(UserInfoActivity activity);
}

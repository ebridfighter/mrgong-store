package uk.co.ribot.androidboilerplate.injection.component.frament;

import dagger.Subcomponent;
import uk.co.ribot.androidboilerplate.injection.PerActivity;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.fragment.HomePageFragment;

/**
 * Created by mike on 2017/11/1.
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface HomePageFragmentComponent {
    void inject(HomePageFragment homePageFragment);
}

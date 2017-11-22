package uk.co.ribot.androidboilerplate.injection.component.frament;

import dagger.Subcomponent;
import uk.co.ribot.androidboilerplate.injection.PerActivity;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.fragment.MessageFragment;

/**
 * Created by leo on 17-11-2.
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface MessageFragmentComponent {
    void inject(MessageFragment messageFragment);
}

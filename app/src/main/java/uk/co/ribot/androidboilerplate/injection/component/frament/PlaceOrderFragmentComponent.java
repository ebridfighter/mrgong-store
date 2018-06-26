package uk.co.ribot.androidboilerplate.injection.component.frament;

import dagger.Subcomponent;
import uk.co.ribot.androidboilerplate.injection.PerActivity;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.fragment.PlaceOrderFragment;

/**
 * Created by mike on 2017/11/6.
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface PlaceOrderFragmentComponent {
    void inject(PlaceOrderFragment placeOrderFragment);
}

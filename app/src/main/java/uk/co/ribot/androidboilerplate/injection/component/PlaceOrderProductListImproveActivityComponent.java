package uk.co.ribot.androidboilerplate.injection.component;

import dagger.Subcomponent;
import uk.co.ribot.androidboilerplate.injection.PerActivity;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.activity.ExampleActivity;
import uk.co.ribot.androidboilerplate.ui.activity.PlaceOrderProductListImproveActivity;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface PlaceOrderProductListImproveActivityComponent {
    void inject(PlaceOrderProductListImproveActivity placeOrderProductListImproveActivity);
}

package uk.co.ribot.androidboilerplate.injection.component.frament;

import dagger.Component;
import uk.co.ribot.androidboilerplate.injection.ConfigPersistent;
import uk.co.ribot.androidboilerplate.injection.component.ApplicationComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;

/**
 * Created by mike on 2017/11/1.
 */
@ConfigPersistent
@Component(dependencies = ApplicationComponent.class)
public interface FragmentBaseComponent {
    HomePageFragmentComponent homePageFragmentComponent(ActivityModule activityModule);
    MoreFragmentComponent moreFragmentComponent(ActivityModule activityModule);
    PlaceOrderFragmentComponent placeOrderFragmentComponent(ActivityModule activityModule);
}
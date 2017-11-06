package uk.co.ribot.androidboilerplate.injection.component;

import dagger.Component;
import uk.co.ribot.androidboilerplate.injection.ConfigPersistent;
import uk.co.ribot.androidboilerplate.injection.component.frament.HomePageFragmentComponent;
import uk.co.ribot.androidboilerplate.injection.component.frament.MoreFragmentComponent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.fragment.MessageFragmentComponent;

/**
 * Created by mike on 2017/11/1.
 */
@ConfigPersistent
@Component(dependencies = ApplicationComponent.class)
public interface FragmentBaseComponent {
    HomePageFragmentComponent homePageFragmentComponent(ActivityModule activityModule);
    MoreFragmentComponent moreFragmentComponent(ActivityModule activityModule);
    MessageFragmentComponent messageFragmentComponent(ActivityModule activityModule);
}

package uk.co.ribot.androidboilerplate.injection.component;

import dagger.Component;
import uk.co.ribot.androidboilerplate.data.remote.RibotsService;
import uk.co.ribot.androidboilerplate.injection.module.PreferenceModule;

/**
 * Created by mike on 2017/9/28.
 */
@Component(modules = PreferenceModule.class)
public interface PreferenceComponent {
    void inject(RibotsService.Creator creator);
}

package uk.co.ribot.androidboilerplate.injection.module;

import dagger.Module;
import dagger.Provides;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;

/**
 * Created by mike on 2017/9/28.
 */
@Module
public class PreferenceModule {
    private PreferencesHelper mPreferencesHelper;

    public PreferenceModule(PreferencesHelper preferencesHelper) {
        mPreferencesHelper = preferencesHelper;
    }

    @Provides
    PreferencesHelper providePreferencesHelper() {
        return mPreferencesHelper;
    }

}

package uk.co.ribot.androidboilerplate.injection.component;

import dagger.Component;
import uk.co.ribot.androidboilerplate.injection.ConfigPersistent;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;

/**
 * A dagger component that will live during the lifecycle of an Activity but it won't
 * be destroy during configuration changes. Check {@link BaseActivity} to see how this components
 * survives configuration changes.
 * Use the {@link ConfigPersistent} scope to annotate dependencies that need to survive
 * configuration changes (for example Presenters).
 */
@ConfigPersistent
@Component(dependencies = ApplicationComponent.class)
public interface ConfigPersistentComponent {

    ExampleActivityComponent exampleActivityComponent(ActivityModule activityModule);
    LoginActivityComponent loginActivityComponent(ActivityModule activityModule);
    MainActivityComponent mainActivityComponent(ActivityModule activityModule);
    ProductListActivityComponent productListActivityComponent(ActivityModule activityModule);
    IntelligentPlaceOrderActivityComponent intelligentPlaceOrderActivityComponent(ActivityModule activityModule);
    OrderDetailActivityComponent orderDetailActivityComponent(ActivityModule activityModule);
    OrderCommitSuccessActivityComponent orderCommitSuccessActivityComponent(ActivityModule activityModule);
    SelfHelpPlaceOrderActivityComponent selfHelpPlaceOrderActivityComponent(ActivityModule activityModule);
    PlaceOrderProductListCompoent placeOrderProductListCompoent(ActivityModule activityModule);
    OrderListActivityComponent orderListActivityComponent(ActivityModule activityModule);
    ReturnOrderListActivityComponent returnOrderListActivityComponent(ActivityModule activityModule);
    UserInfoActivityComponent userInfoActivityComponent(ActivityModule activityModule);
    StatementAccountActivityComponent statementAccountActivityComponent(ActivityModule activityModule);
    MakeInventoryActivityComponent makeInventoryActivityComponent(ActivityModule activityModule);
    MakeInventoryDetailActivityComponent makeInventoryDetailActivityComponent(ActivityModule activityModule);
    LauncherActivityComponent launcherActivityComponent(ActivityModule activityModule);
}
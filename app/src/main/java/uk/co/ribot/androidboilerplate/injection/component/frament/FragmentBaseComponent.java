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
    MessageFragmentComponent messageFragmentComponent(ActivityModule activityModule);
    PlaceOrderFragmentComponent placeOrderFragmentComponent(ActivityModule activityModule);
    StockListContainerFragmentComponent stockListContainerFragmentComponent(ActivityModule activityModule);
    StockListFragmentComponent stockListFragmentComponent(ActivityModule activityModule);
    OrderProductFragmentComponent orderProductFragmentComponent(ActivityModule activityModule);
    ProductListFragmentComponent productListFragmentComponent(ActivityModule activityModule);
    OrderListFragmentComponent orderListFragmentComponent(ActivityModule activityModule);
    ReturnOrderListFragmentComponent returnOrderListFragmentComponent(ActivityModule activityModule);
    PriceListFragmentComponent priceListFragmentComponent(ActivityModule activityModule);
    MakeInventoryFragmentComponent makeInventoryFragmentComponent(ActivityModule activityModule);
    MakeInventoryDetailFragmentComponent makeInventoryDetailFragmentComponent(ActivityModule activityModule);
    ProcurementFragmentComponent procurementFragmentComponent(ActivityModule activityModule);
}

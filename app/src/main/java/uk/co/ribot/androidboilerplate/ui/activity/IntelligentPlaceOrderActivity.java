package uk.co.ribot.androidboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.injection.module.ActivityModule;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;

public class IntelligentPlaceOrderActivity extends BaseActivity {

    public static final String INTENT_KEY_ESTIMATED_TURNOVER = "intent_key_estimated_turnover";
    public static final String INTENT_KEY_SAFETY_FACTOR = "intent_key_safety_factor";

    public static Intent getStartIntent(Context context,double estimatedTurnover, double safetyFactor){
        Intent intent = new Intent(context,IntelligentPlaceOrderActivity.class);
        intent.putExtra(INTENT_KEY_ESTIMATED_TURNOVER,estimatedTurnover);
        intent.putExtra(INTENT_KEY_SAFETY_FACTOR,safetyFactor);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intelligent_place_order);
        configPersistentComponent.intelligentPlaceOrderActivityComponent(new ActivityModule(getActivityContext()))
                .inject(this);
    }
}

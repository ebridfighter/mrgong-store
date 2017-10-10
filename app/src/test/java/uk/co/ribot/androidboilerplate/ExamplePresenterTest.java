package uk.co.ribot.androidboilerplate;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.Ribot;
import uk.co.ribot.androidboilerplate.test.common.TestDataFactory;
import uk.co.ribot.androidboilerplate.ui.presenter.ExamplePresenter;
import uk.co.ribot.androidboilerplate.ui.view_interface.ExampleMvpView;
import uk.co.ribot.androidboilerplate.util.RxSchedulersOverrideRule;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExamplePresenterTest {

    @Mock
    ExampleMvpView mMockMainMvpView;
    @Mock DataManager mMockDataManager;
    private ExamplePresenter mExamplePresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        mExamplePresenter = new ExamplePresenter(mMockDataManager);
        mExamplePresenter.attachView(mMockMainMvpView);
    }

    @After
    public void tearDown() {
        mExamplePresenter.detachView();
    }

    @Test
    public void loadRibotsReturnsRibots() {
        List<Ribot> ribots = TestDataFactory.makeListRibots(10);
        when(mMockDataManager.getRibots())
                .thenReturn(Observable.just(ribots));

        mExamplePresenter.loadRibots();
        verify(mMockMainMvpView).showRibots(ribots);
        verify(mMockMainMvpView, never()).showRibotsEmpty();
        verify(mMockMainMvpView, never()).showError();
    }

    @Test
    public void loadRibotsReturnsEmptyList() {
        when(mMockDataManager.getRibots())
                .thenReturn(Observable.just(Collections.<Ribot>emptyList()));

        mExamplePresenter.loadRibots();
        verify(mMockMainMvpView).showRibotsEmpty();
        verify(mMockMainMvpView, never()).showRibots(ArgumentMatchers.<Ribot>anyList());
        verify(mMockMainMvpView, never()).showError();
    }

    @Test
    public void loadRibotsFails() {
        when(mMockDataManager.getRibots())
                .thenReturn(Observable.<List<Ribot>>error(new RuntimeException()));

        mExamplePresenter.loadRibots();
        verify(mMockMainMvpView).showError();
        verify(mMockMainMvpView, never()).showRibotsEmpty();
        verify(mMockMainMvpView, never()).showRibots(ArgumentMatchers.<Ribot>anyList());
    }

}

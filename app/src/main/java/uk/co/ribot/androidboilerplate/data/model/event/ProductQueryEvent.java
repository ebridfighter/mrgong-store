package uk.co.ribot.androidboilerplate.data.model.event;

/**
 * Created by libin on 2017/7/6.
 */

public class ProductQueryEvent {
    private String searchWord;

    public ProductQueryEvent(String searchWord) {
        this.searchWord = searchWord;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }
}

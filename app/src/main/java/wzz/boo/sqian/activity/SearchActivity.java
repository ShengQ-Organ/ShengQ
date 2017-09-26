package wzz.boo.sqian.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import wzz.boo.sqian.R;
import wzz.boo.sqian.base.BaseActivity;

/**
 * Created by zy
 * Date：2017/5/19
 * Time：上午11:29
 */
public class SearchActivity extends BaseActivity {

    private SearchBox search;

    //private PullToRefreshListView mListView;

    @Override
    public int getChildView() {
        return R.layout.activity_search;
    }

    @Override
    protected void findViews() {
        search = findAndCastView(R.id.activity_search_searchbox);
        //mListView = findAndCastView(R.id.activity_search_listView);
    }

    @Override
    public void setViews(Bundle savedInstanceState) {
        search.enableVoiceRecognition(this);
        search.setMenuListener(new SearchBox.MenuListener() {
            @Override
            public void onMenuClick() {

            }

        });
        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                //Use this to tint the screen
            }

            @Override
            public void onSearchClosed() {
                //Use this to un-tint the screen
            }

            @Override
            public void onSearchTermChanged(String term) {
                //React to the search term changing
                //Called after it has updated results
            }

            @Override
            public void onSearch(String searchTerm) {

            }

            @Override
            public void onResultClick(SearchResult result) {
                //React to a result being clicked
            }

            @Override
            public void onSearchCleared() {
                //Called when the clear button is clicked
            }

        });
        search.setOverflowMenu(R.menu.overflow_menu);
        search.setOverflowMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.test_menu_item:
//                        Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
//                        return true;
//                }
                return false;
            }
        });
    }

    @Override
    public void registerListeners() {

    }

    @Override
    public void doOtherEvents() {

    }
}

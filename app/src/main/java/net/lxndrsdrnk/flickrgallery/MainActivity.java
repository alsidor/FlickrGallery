package net.lxndrsdrnk.flickrgallery;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import net.lxndrsdrnk.flickrgallery.api.Photo;
import net.lxndrsdrnk.flickrgallery.list.PhotosFragment;
import net.lxndrsdrnk.flickrgallery.polling.PollingHelper;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, PhotosFragment.OnListFragmentInteractionListener {

    public static final String LIST_FRAGMENT_TAG = "LIST_FRAGMENT_TAG";
    public static final String EXTRA_REFRESH_DATA = "EXTRA_REFRESH_DATA";

    @Inject
    SharedPreferences sharedPreferences;

    PollingHelper pollingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((FGApplication) getApplication()).component().inject(this);

        pollingHelper = new PollingHelper(this, sharedPreferences);

        setContentView(R.layout.activity_main);



        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                boolean hasBack = getSupportFragmentManager().getBackStackEntryCount() >= 1;
                getSupportActionBar().setDisplayHomeAsUpEnabled(hasBack);
                getSupportActionBar().setDisplayShowHomeEnabled(hasBack);
            }
        });

        if(null == savedInstanceState) {

            final String searchValue = sharedPreferences.getString(SettingsKeys.CURRENT_SEARCH_VALUE, null);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, PhotosFragment.newInstance(searchValue), LIST_FRAGMENT_TAG)
                    .commit();

        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().hasExtra(MainActivity.EXTRA_REFRESH_DATA)){
            refreshPhotosFragment();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnQueryTextListener(this);


        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        final String searchValue = sharedPreferences.getString(SettingsKeys.CURRENT_SEARCH_VALUE, null);
        final boolean hasSearchValue = !TextUtils.isEmpty(searchValue);

        menu.findItem(R.id.menu_clear_search).setVisible(hasSearchValue);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        if( hasSearchValue ){
            searchView.setIconified(false);
            searchView.setQuery(searchValue, false);
            searchView.clearFocus();
        }else{
            searchView.setIconified(true);
            searchView.setQuery(null, false);
        }

        final boolean isPollingEnabled = pollingHelper.isPollingEnabled();
        menu.findItem(R.id.menu_start_poling).setVisible(!isPollingEnabled && hasSearchValue);
        menu.findItem(R.id.menu_stop_polling).setVisible(isPollingEnabled);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_clear_search:
                onClearSearch();
                break;
            case R.id.menu_start_poling:
                pollingHelper.startPolling();
                break;
            case R.id.menu_stop_polling:
                pollingHelper.stopPolling();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }

    @Override
    public void onPhotoSelected(Photo photo) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, PhotoInfoFragment.newInstance(photo.getPhotoPageUrl()))
                .addToBackStack(null)
                .commit();
    }

    public void onClearSearch(){
        setNewSearchValue(null);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        setNewSearchValue(query);
        return true;
    }

    public void setNewSearchValue(String searchValue){

        sharedPreferences.edit().putString(SettingsKeys.CURRENT_SEARCH_VALUE, searchValue).apply();

        if(TextUtils.isEmpty(searchValue)){
            pollingHelper.stopPolling();
        }

        invalidateOptionsMenu();

        setSearchValueToFragments(searchValue);
    }

    public void setSearchValueToFragments(String searchValue){
        PhotosFragment photosFragment = (PhotosFragment) getSupportFragmentManager().findFragmentByTag(LIST_FRAGMENT_TAG);
        if( photosFragment != null ){
            photosFragment.setSearchValue(searchValue);
        }
    }

    public void refreshPhotosFragment(){
        PhotosFragment photosFragment = (PhotosFragment) getSupportFragmentManager().findFragmentByTag(LIST_FRAGMENT_TAG);
        if( photosFragment != null ){
            photosFragment.refresh();
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }
}

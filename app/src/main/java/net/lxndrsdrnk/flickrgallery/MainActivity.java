package net.lxndrsdrnk.flickrgallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.lxndrsdrnk.flickrgallery.api.Photo;
import net.lxndrsdrnk.flickrgallery.list.PhotosFragment;

public class MainActivity extends AppCompatActivity implements PhotosFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((FGApplication) getApplication()).component().inject(this);

        setContentView(R.layout.activity_main);

        if(null == savedInstanceState) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, PhotosFragment.newInstance(3))
                    .commit();
        }

    }

    @Override
    public void onListFragmentInteraction(Photo photo) {

    }
}

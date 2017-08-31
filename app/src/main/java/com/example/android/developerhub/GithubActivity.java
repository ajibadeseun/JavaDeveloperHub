package com.example.android.developerhub;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GithubActivity extends AppCompatActivity implements LoaderCallbacks<List<Github>> {

    private static final String LOG_TAG = GithubAdapter.class.getName();

    private static final int GITHUB_LOADER_ID = 1;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;


    /**
     * URL for github data from the Github API dataset
     */
    private static final String GITHUB_REQUEST_URL =
            "https://api.github.com/search/users?q=language:java+location:lagos\\";

    /**
     * Adapter for the list of github data
     */
    private GithubAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView githubListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        githubListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of github data as input
        mAdapter = new GithubAdapter(this, new ArrayList<Github>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        githubListView.setAdapter(mAdapter);

        githubListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Github github = mAdapter.getItem(position);
                String userName = github.username;
                String imageString = github.imageUrl;
                String userLinkString = github.profileUrl;
                Intent intent = new Intent(GithubActivity.this, ProfileActivity.class);
                Bundle i = new Bundle();
                intent.putExtra("imageString", imageString);
                intent.putExtra("profileUri", userLinkString);
                intent.putExtra("userName", userName);
                intent.putExtras(i);
                startActivity(intent);

            }
        });

        // Get a reference to the LoaderManager , in order to interact with the loaders
        LoaderManager loaderManager = getLoaderManager();

        Log.i(LOG_TAG, "TEST: calling initLoader()...");
        loaderManager.initLoader(GITHUB_LOADER_ID, null, this);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(GITHUB_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText("No Internet connection");
        }
    }

    @Override
    public Loader<List<Github>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST: onCreateLoader() called ...");
        // Create a new loader for the given URL
        return new GithubLoader(this, GITHUB_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Github>> loader, List<Github> data) {
        Log.i(LOG_TAG, "TEST: onLoadFinished() called....");
        View loadingIndicator = findViewById(R.id.loading_spinner);

        loadingIndicator.setVisibility(View.GONE);

        // Clear the adapter of previous github data
        mAdapter.clear();

        // Set empty state text to display "No Github information found."
        mEmptyStateTextView.setText("No Github information found.");


        // If there is a valid list of {@link Github}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Github>> loader) {

        Log.i(LOG_TAG, "TEST: onLoaderReset() called...");
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}

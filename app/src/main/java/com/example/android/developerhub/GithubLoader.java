package com.example.android.developerhub;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by DAMILOLA on 8/31/2017.
 */

public class GithubLoader extends AsyncTaskLoader<List<Github>> {


    /** Tag for log messages */
    private static final String LOG_TAG = GithubLoader.class.getName();

    /** Query URL */
    private String mUrl;
    /**
     * Constructs a new {@link GithubLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */

    public GithubLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    /**
     * This is on a background thread.
     */

    @Override
    public List<Github> loadInBackground() {
        Log.i(LOG_TAG,"TEST: loadInBackground() method called...");
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of gitList.
        List<Github> gitList = QueryUtils.fetchGithubData(mUrl);
        return gitList;
    }


    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG,"TEST: onStartLoading() method called...");
        forceLoad();
    }
}

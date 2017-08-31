package com.example.android.developerhub;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAMILOLA on 8/31/2017.
 */

public final class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Github API dataset and return a list of {@link Github} objects.
     */


    public static List<Github> fetchGithubData(String requestUrl) {
        Log.i(LOG_TAG,"TEST: fetchGithubData called...");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Github> githubResp = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return githubResp;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Github} objects that has been built up from
     * parsing a JSON response.
     */
    /**
     * Return a list of {@link Github} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Github> extractFeatureFromJson(String githubJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(githubJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding github data to
        List<Github>  github = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(githubJSON);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of items .
            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");

            // For each object in the itemsArray, create an {@link Github} object
            for (int i = 0; i < itemsArray.length(); i++) {

                // Get a single githubData at position i within the list of items
                JSONObject currentItemObject = itemsArray.getJSONObject(i);



                // Extract the value for the key called "login"
                String githubName = currentItemObject.getString("login");

                // Extract the value for the key called "avatar_url"
                String profileImageUrl = currentItemObject.getString("avatar_url");

                // Extract the value for the key called "html_url"
                String profileUrl = currentItemObject.getString("html_url");



                // Create a new {@link Github} object with the githubName, profileImageUrl, time,
                // and url from the JSON response.
                Github githubData = new Github(githubName, profileImageUrl,profileUrl);

                // Add the new {@link Github} to the list of github accounts.
                github.add(githubData);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the github JSON results", e);
        }

        // Return the list of earthquakes
        return github;
    }

}

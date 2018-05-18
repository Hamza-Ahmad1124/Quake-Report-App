/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks <ArrayList<Earthquake>>
{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    
    private static final String USGS_REQUEST_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&limit=10";
    
    public DataAdapter adapter = null ;
    
    private TextView empty_textview ;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
    
        empty_textview = (TextView) findViewById(R.id.empty);
        
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        
        if (isConnected == true)
        {
            getLoaderManager().initLoader(1 , null , this);
        }
        
        else
        {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
            progressBar.setVisibility(View.GONE);
            empty_textview.setText("No Internet Connection");
        }
        
        
        
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        
        earthquakeListView.setEmptyView(empty_textview);
        
        // Create a new {@link ArrayAdapter} of earthquakes

        adapter = new DataAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
    
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick (AdapterView<?> adapterView, View view, int i, long l)
            {
                Earthquake currentEQ = adapter.getItem(i);
    
                Uri webpage = Uri.parse(currentEQ.getUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
    
                if (webIntent.resolveActivity(getPackageManager()) != null)
                {
                    startActivity(webIntent);
                }
            }
        });
    }
    
    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader (int i, Bundle bundle)
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
    
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
    
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
    
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);
    
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }
    
    @Override
    public void onLoadFinished (Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> earthquakes)
    {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.GONE);
    
        empty_textview.setText("No Earthquakes Found");
        
        // Clear the adapter of previous earthquake data
        adapter.clear();
    
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !(earthquakes.isEmpty()))
        {
            adapter.addAll(earthquakes);
        }
    }
    
    @Override
    public void onLoaderReset (Loader<ArrayList<Earthquake>> loader)
    {
        adapter.clear();
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

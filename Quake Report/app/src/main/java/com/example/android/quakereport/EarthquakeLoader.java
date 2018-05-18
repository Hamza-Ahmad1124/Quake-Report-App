package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Sherlock on 8/12/2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader <ArrayList<Earthquake>>
{
    private String requestURL = "";
    
    public EarthquakeLoader(Context context, String url)
    {
        super(context);
        requestURL = url;
    }
    
    @Override
    protected void onStartLoading()
    {
        forceLoad();
    }
    
    @Override
    public ArrayList<Earthquake> loadInBackground ()
    {
        ArrayList<Earthquake> earthquakes = QueryUtils.fetchEarthquakesData(requestURL);
        return earthquakes ;
    }
}

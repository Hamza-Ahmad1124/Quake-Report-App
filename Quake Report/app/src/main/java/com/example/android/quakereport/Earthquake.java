package com.example.android.quakereport;

/**
 * Created by Sherlock on 8/5/2017.
 */

public class Earthquake
{
    private double magnitude ;
    private String city;
    private long timeInMilliSeconds;
    private String url ;

    public Earthquake (double mMagnitude, String mCity, long mtimeInMilliSeconds, String mUrl)
    {
        magnitude = mMagnitude;
        city = mCity ;
        timeInMilliSeconds = mtimeInMilliSeconds ;
        url = mUrl ;
    }

    public double getMagnitude()
    {
        return magnitude;
    }

    public String getCity()
    {
        return city;
    }

    public long getTimeInMilliSeconds()
    {
        return timeInMilliSeconds;
    }
    
    public String getUrl ()
    {
        return url ;
    }
}
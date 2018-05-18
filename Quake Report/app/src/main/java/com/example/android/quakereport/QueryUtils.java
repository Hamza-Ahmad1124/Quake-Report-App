package com.example.android.quakereport;

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
import java.util.ArrayList;

/**
 * Created by Sherlock on 8/7/2017.
 */

public class QueryUtils
{
    
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils ()
    {}
    
    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    
    public static ArrayList<Earthquake> fetchEarthquakesData(String url)
    {
        URL requestURL = createURL(url);
        String jsonResponse = "" ;
    
        try
        {
            jsonResponse = makeHttpRequest(requestURL);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return extractEarthquakes(jsonResponse);
    }
    
    private static URL createURL(String requestURL)
    {
        URL url = null;
        
        try
        {
            url = new URL(requestURL);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        
        return url;
    }
    
    private static String makeHttpRequest (URL url) throws IOException
    {
        String jsonResponse = "";
        
        if (url == null)
        {
            return jsonResponse ;
        }
        
        InputStream stream = null;
        HttpURLConnection urlConnection = null;
    
        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            
            if (urlConnection.getResponseCode() == 200)
            {
                stream = urlConnection.getInputStream();
                jsonResponse = readFromStream(stream);
            }
            else
            {
                Log.i("QueryUtils.java" , "Response Code not 200");
            }
        }
        
        catch (IOException e)
        {
            e.printStackTrace();
        }

        finally
        {
            if(urlConnection != null)
            {
                urlConnection.disconnect();
            }
            
            if(stream != null)
            {
                // function must handle java.io.IOException here
                stream.close();
            }
        }
        
        return jsonResponse;
    }
    
    private static String readFromStream (InputStream stream) throws IOException
    {
        StringBuilder output = new StringBuilder();
        
        if (stream != null)
        {
            InputStreamReader streamReader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            
            String line = bufferedReader.readLine();
            
            while (line != null)
            {
                output.append(line);
                line = bufferedReader.readLine();
            }
            
            return output.toString();
        }
    
        return "";
    }
    
    public static ArrayList<Earthquake> extractEarthquakes (String jsonResponse)
    {
        if (TextUtils.isEmpty(jsonResponse))
        {
            return null;
        }
        
        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try
        {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray featuresArray = root.getJSONArray("features");
            
            for (int i = 0 ; i < featuresArray.length() ; i++)
            {
                JSONObject earthquake = featuresArray.getJSONObject(i);
                JSONObject properties = earthquake.getJSONObject("properties");
                
                earthquakes.add(new Earthquake(properties.getDouble("mag"), properties.getString("place"), properties.getLong("time"), properties.getString("url")));
            }
        }
        
        catch (JSONException e)
        {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        
        // Return the list of earthquakes
        return earthquakes;
    }
}

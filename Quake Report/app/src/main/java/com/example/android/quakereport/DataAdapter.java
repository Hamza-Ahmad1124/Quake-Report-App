package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.drawable;
import static android.R.attr.format;

/**
 * Created by Sherlock on 8/5/2017.
 */

public class DataAdapter extends ArrayAdapter <Earthquake>
{
    public DataAdapter (Context context, ArrayList<Earthquake> objects)
    {
        super(context, 0, objects);
    }
    
    @NonNull
    @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItemView = convertView;
    
        if(listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
    
        Earthquake currentInfo = getItem(position);
    
        TextView magnitude = (TextView) listItemView.findViewById(R.id.magnitude);
        magnitude.setText(formatMagnitude(currentInfo.getMagnitude()));
    
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();
    
        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor((int)currentInfo.getMagnitude());
    
        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);
        
        String[] locationValues = formatLocation(currentInfo.getCity());
    
        TextView locationOffset = (TextView) listItemView.findViewById(R.id.location_offset);
        locationOffset.setText(locationValues[0]);
        
        TextView location = (TextView) listItemView.findViewById(R.id.primary_location);
        location.setText(locationValues[1]);
    
        Date dateObject = new Date(currentInfo.getTimeInMilliSeconds());
        
        TextView date = (TextView) listItemView.findViewById(R.id.date);
        date.setText(formatDate(dateObject));
    
        TextView time = (TextView) listItemView.findViewById(R.id.time);
        time.setText(formatTime(dateObject));
    
        return listItemView;
    }
    
    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate (Date dateObject)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
    
    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime (Date dateObject)
    {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
    
    private String[] formatLocation (String location)
    {
        String[] values = new String[2];
        
        if(location.contains(" of "))
        {
            String[] pieces = location.split("of ");
            values[1] = pieces[1];
            values[0] = pieces[0] + "of";
        }
        
        else
        {
            values[0] = "Near the";
            values[1] = location;
        }
        
        return values;
    }
    
    private String formatMagnitude (double magnitude)
    {
        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(magnitude);
    }
    
    private int getMagnitudeColor (int magnitude)
    {
        int magnitudeColorResourceId;
        
        switch (magnitude)
        {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
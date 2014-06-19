package com.codepath.gridimagesearch.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ImageResult implements Serializable
{
    private String fullUrl;
    private String thumbUrl;

    public ImageResult(JSONObject json)
    {
	try
	{
	    fullUrl = json.getString("url");
	    thumbUrl = json.getString("tbUrl");
	}
	catch (JSONException e)
	{
	    Log.d("ERROR", "Parse JSON Exception : "  +  e.getMessage());
	    e.printStackTrace();
	}
    }

    public static ArrayList<ImageResult> fromJSONArray(JSONArray jsonArray)
    {
	ArrayList<ImageResult> imageList = new ArrayList<ImageResult>();

	try
	{
	    for (int i = 0; i < jsonArray.length(); i++)
	    {
		imageList.add(new ImageResult(jsonArray.getJSONObject(i)));
	    }
	}
	catch (JSONException e)
	{
	    Log.d("ERROR", "Parse JSON Exception : "  +  e.getMessage());
	    e.printStackTrace();
	}
	return imageList;
    }

    public String getFullUrl()
    {
	return fullUrl;
    }

    public void setFullUrl(String fullUrl)
    {
	this.fullUrl = fullUrl;
    }

    public String getThumbUrl()
    {
	return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl)
    {
	this.thumbUrl = thumbUrl;
    }

}

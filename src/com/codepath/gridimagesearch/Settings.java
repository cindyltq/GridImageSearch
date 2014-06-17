package com.codepath.gridimagesearch;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Settings implements Parcelable, Serializable 
{
    private static final long serialVersionUID = -4302146816674044716L;
    
    private String size ="";
    private String color ="";
    private String type ="";
    private String site ="";

    public Settings () {
	
    }
    
    public Settings(Parcel source)
    {
	this.size = source.readString();
	this.color = source.readString();
	this.type = source.readString();
	this.site = source.readString();
    }
    
    

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getSite()
    {
        return site;
    }

    public void setSite(String site)
    {
        this.site = site;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
	dest.writeString(size);
	dest.writeString(color);
	dest.writeString(type);
	dest.writeString(site);

    }    

    @Override
    public int describeContents()
    {
	// TODO Auto-generated method stub
	return 0;
    }

    public static final Parcelable.Creator<Settings> CREATOR = new Parcelable.Creator<Settings>()
    {
	@Override
	public Settings createFromParcel(Parcel source)
	{
	    return new Settings(source);
	}

	@Override
	public Settings[] newArray(int size)
	{
	    return new Settings[size];
	}
    };

}

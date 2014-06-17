package com.codepath.gridimagesearch;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.loopj.android.image.SmartImageView;

public class ImageArrayAdapter extends ArrayAdapter<ImageResult>
{
    private static class ViewHolder
    {
	SmartImageView itemImageView;
    }

    public ImageArrayAdapter(Context context, List<ImageResult> objects)
    {
	super(context, R.layout.image_item_view, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
	ViewHolder viewHolder;
	ImageResult imageResult = (ImageResult) getItem(position);
	
	if (convertView == null)
	{
	    LayoutInflater inflater = LayoutInflater.from(getContext());
	    convertView = inflater.inflate(R.layout.image_item_view, parent, false);
	    
	    viewHolder = new ViewHolder();
	    viewHolder.itemImageView = (SmartImageView) convertView;
	    
	    convertView.setTag(viewHolder);
	}
	else
	{
	    viewHolder = (ViewHolder) convertView.getTag();
	    viewHolder.itemImageView.setImageBitmap(null);
	}

    	viewHolder.itemImageView.setImageUrl(imageResult.getThumbUrl());

	return convertView;

    }

}

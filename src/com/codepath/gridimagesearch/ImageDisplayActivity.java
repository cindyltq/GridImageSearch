package com.codepath.gridimagesearch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.codepath.gridimagesearch.TouchImageView.OnTouchImageViewListener;

public class ImageDisplayActivity extends Activity
{
    TouchImageView imageView;
    private static final ScaleType[] scaleTypes = { ScaleType.CENTER, ScaleType.CENTER_CROP, ScaleType.CENTER_INSIDE, ScaleType.FIT_XY    };
    private int index = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_image_display);

	Intent intent = getIntent();
	ImageResult result = (ImageResult) intent.getSerializableExtra(SearchActivity.IMAGE_RESULT_KEY);

	imageView = (TouchImageView) findViewById(R.id.ivResult);
	imageView.setImageUrl(result.getFullUrl());

	setListnerToView();
    }
    
    private void setListnerToView()
    {
	imageView.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			index = ++index % scaleTypes.length;
			ScaleType currScaleType = scaleTypes[index];
			imageView.setScaleType(currScaleType);
			Toast.makeText(ImageDisplayActivity.this, "ScaleType: " + currScaleType, Toast.LENGTH_SHORT).show();
		}
	});
    }

    // Inflate ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	getMenuInflater().inflate(R.menu.menu_share, menu);
	return true;
    }

    // Respond to ActionBar icon click
    public boolean onOptionsItemSelected(MenuItem item)
    {
	switch (item.getItemId())
	{
	case R.id.miShare:
	    shareImage();
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    // Respond to Share icon click
    private void shareImage()
    {
	// Uri uri = Uri.fromFile(new File(getFilesDir(), "foo.jpg"));

	Uri bmpUri = getLocalBitmapUri(imageView);
	if (bmpUri != null)
	{
	    // Construct a ShareIntent with link to image
	    Intent shareIntent = new Intent();
	    shareIntent.setAction(Intent.ACTION_SEND);
	    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
	    shareIntent.setType("image/*");

	    // Launch sharing dialog for image
	    startActivity(Intent.createChooser(shareIntent, "Share Image"));
	}
	else
	{
	    Log.d("ERROR", "Error sharing the image.");
	    Toast.makeText(this, "Problem with sharing this image.", Toast.LENGTH_SHORT);
	}
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView)
    {
	// Extract Bitmap from ImageView drawable
	Drawable drawable = imageView.getDrawable();
	Bitmap bmp = null;

	if (drawable instanceof BitmapDrawable)
	    bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
	else
	    return null;

	// Store image to default external storage directory
	Uri bmpUri = null;
	try
	{
	    File file = new File(Environment.getExternalStoragePublicDirectory(
		    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");

	    file.getParentFile().mkdirs();
	    FileOutputStream out = new FileOutputStream(file);
	    bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
	    out.close();
	    bmpUri = Uri.fromFile(file);
	}
	catch (IOException e)
	{
	    Log.d("ERROR", "Error saving image to file storage  : " + e.getMessage());
	    e.printStackTrace();
	}

	return bmpUri;
    }

}

package com.codepath.gridimagesearch.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.adapter.ImageArrayAdapter;
import com.codepath.gridimagesearch.helper.CommonUtil;
import com.codepath.gridimagesearch.helper.EndlessScrollListener;
import com.codepath.gridimagesearch.helper.FileUtil;
import com.codepath.gridimagesearch.helper.GoogleImageRestClient;
import com.codepath.gridimagesearch.model.ImageResult;
import com.codepath.gridimagesearch.model.Settings;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity// SherlockFragmentActivity 
{
     private GridView gvImages;
    private MenuItem miShare;
    private MenuItem miSetting;
    private MenuItem miSearch;
    ArrayList<ImageResult> imageResultArray = new ArrayList<ImageResult>();
    private static String RESPONSE_DATA = "responseData";
    private static String RESULTS = "results";
    public static String IMAGE_RESULT_KEY = "IMAGE_RESULT_KEY";
    public static int REQUEST_CODE = 90;
    ImageArrayAdapter imageAdapter;  
    Settings setting;
    String queryText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_search);

	miShare = (MenuItem) findViewById(R.id.miShare);
	miSetting = (MenuItem) findViewById(R.id.miSetting);
	gvImages = (GridView) findViewById(R.id.gvImages);

	imageAdapter = new ImageArrayAdapter(this, imageResultArray);
	gvImages.setAdapter(imageAdapter);

	gvImages.setOnItemClickListener(getOnItemClickListener());
	gvImages.setOnScrollListener(getOnScrollListener());
    }

    // Inflate ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	getMenuInflater().inflate(R.menu.menu_search, menu);	
	miSearch = (MenuItem) menu.findItem(R.id.miSearch);
	SearchView searchView = (SearchView) miSearch.getActionView();
	searchView.setOnQueryTextListener(new OnQueryTextListener()
                                    	{
                                    	    @Override
                                    	    public boolean onQueryTextSubmit(String query)
                                    	    {
                                    		queryText = query;
                                    		doSearch();
                                    		return true;
                                    	    }
                                    
                                    	    @Override
                                    	    public boolean onQueryTextChange(String newText)
                                    	    {
                                    		return false;
                                    	    }
                                    	});
	return super.onCreateOptionsMenu(menu);
    }

    // Respond to ActionBar icon click
    public boolean onOptionsItemSelected(MenuItem item)
    {
	switch (item.getItemId())
	{
	case R.id.miSetting:
	    configureQuery();
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}

    }
    
    private void doSearch()
    {
	if (!CommonUtil.isNetworkConnected(this))
	{
	    Toast.makeText(this, "Internet connection is unavailable.", Toast.LENGTH_SHORT);
	}
	else
	{	   
	    if (queryText != null && queryText.length() > 0)
	    {
		imageResultArray.clear();

		GoogleImageRestClient.get(buildRelativeUrl(0, queryText), getResponseHandler());
	    }
	    else
		Toast.makeText(this, "Please enter what you would like to search.", Toast.LENGTH_SHORT);
	}
    }

    // Private helper for building URL
    private String buildRelativeUrl(int start, String imageName)
    {
	StringBuilder result = new StringBuilder()
		.append("start=").append(start)
		.append("&q=").append(Uri.encode(imageName));

	// Load saved setting object from file storage
	if (setting == null)
	    setting = FileUtil.loadObject();

	if (setting != null)
	{
	    result.append("&imgtype=").append(setting.getType())
		    .append("&imgcolor=").append(setting.getColor())
		    .append("&imgsz=").append(setting.getSize())
		    .append("&as_sitesearch=").append(setting.getSite());
	}

	return result.toString();
    }

    // Respond to Search action that fires Restful API call
    private JsonHttpResponseHandler getResponseHandler()
    {
	return new JsonHttpResponseHandler()
	{
	    @Override
	    public void onSuccess(JSONObject response)
	    {
		try
		{
		    JSONArray resultArray = response.getJSONObject(RESPONSE_DATA).getJSONArray(RESULTS);

		    // If this is from a new search, imageAdapter has been cleared out.
		    // If this is from scrolling action, new items are appended to the adapter
		    imageAdapter.addAll(ImageResult.fromJSONArray(resultArray));

		    Log.d("DEBUG", "adapter size = " + imageAdapter.getCount());
		}
		catch (JSONException e)
		{
		    Log.d("ERROR", "Parse JSON Exception : " + e.getMessage());
		    e.printStackTrace();
		}
	    }

	    @Override
	    public void onFailure(java.lang.Throwable e, org.json.JSONObject errorResponse)
	    {
		Log.d("ERROR", "REST call failure : " + e.getMessage());
		e.printStackTrace();
	    }

	};
    }

    // Respond to image item click
    private OnItemClickListener getOnItemClickListener()
    {
	return new OnItemClickListener()
	{
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	    {
		Intent intent = new Intent(getApplicationContext(), ImageDisplayActivity.class);
		intent.putExtra(IMAGE_RESULT_KEY, imageResultArray.get(position));
		startActivity(intent);
	    }
	};
    }

    // Respond to image grid scrolling
    private EndlessScrollListener getOnScrollListener()
    {
	return new EndlessScrollListener()
	{
	    @Override
	    public void onLoadMore(int page, int totalItemsCount)
	    {
		// Triggered only when new data needs to be appended to the list
		GoogleImageRestClient.get(buildRelativeUrl(totalItemsCount, queryText), getResponseHandler());

	    }
	};
    }

    // Respond to Setting icon click
    private void configureQuery()
    {
	Intent intent = new Intent(this, SettingActivity.class);
	startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	// REQUEST_CODE is defined above
	if (resultCode == RESULT_OK && requestCode == REQUEST_CODE)
	{
	    setting = (Settings) (data.getExtras().getParcelable("setting"));

	    // save the settings object
	    FileUtil.saveObject(setting);
	}
    }

}

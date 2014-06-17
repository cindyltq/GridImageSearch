package com.codepath.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class SettingActivity extends Activity
{
    private Spinner spColor;
    private Spinner spSize;
    private Spinner spType;
    private EditText etSite;
    boolean isSettingChanged;
    Settings setting;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_setting);

	spColor = (Spinner) findViewById(R.id.spColor);
	spSize = (Spinner) findViewById(R.id.spSize);
	spType = (Spinner) findViewById(R.id.spType);
	etSite = (EditText) findViewById(R.id.etSite);

	spColor.setOnItemSelectedListener(getOnItemSelectedListener());
	spType.setOnItemSelectedListener(getOnItemSelectedListener());
	spSize.setOnItemSelectedListener(getOnItemSelectedListener());
		
	setting = FileUtil.loadObject();
	populateOldSettings();
    }
    
    private void populateOldSettings()
    {
	if(setting == null)
	    return;
	
	setSpinnerToValue(spColor, setting.getColor());
	setSpinnerToValue(spSize, setting.getSize());
	setSpinnerToValue(spType, setting.getType());
	etSite.setText(setting.getSite());
    }

    public void onSaveAction(View view)
    {
	setting = new Settings();
	setting.setColor(spColor.getSelectedItem().toString());
	setting.setSize(spSize.getSelectedItem().toString());
	setting.setType(spType.getSelectedItem().toString());
	setting.setSite(etSite.getText().toString());

	// Prepare data intent to returnsetting
	Intent returnResultIntent = new Intent();
	returnResultIntent.putExtra("setting",(Parcelable)setting);	
	setResult(RESULT_OK, returnResultIntent); 
	finish(); // closes the activity, pass data to parent
    }

    private OnItemSelectedListener getOnItemSelectedListener()
    {
	return new OnItemSelectedListener()
	{
	    @Override
	    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	    {
		//String value = parent.getItemAtPosition(position).toString();
		String value =((Spinner) parent).getSelectedItem().toString();
		setSpinnerToValue(((Spinner) parent), value);
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> parent)
	    {
		// TODO Auto-generated method stub

	    }

	};
    }

    public void setSpinnerToValue(Spinner spinner, String value)
    {
	int index = 0;
	SpinnerAdapter adapter = spinner.getAdapter();
	for (int i = 0; i < adapter.getCount(); i++)
	{
	    if (adapter.getItem(i).equals(value))
	    {
		index = i;
	    }
	}
	spinner.setSelection(index);
    }


}

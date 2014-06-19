package com.codepath.gridimagesearch.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.codepath.gridimagesearch.model.Settings;

import android.util.Log;

public class FileUtil
{
    public static final String FILE_NAME = "/sdcard/image_search_setting.ser";
    
   // public static final String FILE_NAME = "C://Temp/image_search_setting.ser";

    public static void saveObject(Settings setting)
    {
	try
	{
	    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(FILE_NAME)));
	    oos.writeObject(setting);
	    oos.flush();
	    oos.close();
	}
	catch (Exception ex)
	{
	    Log.v("Serialization Save Error : ", ex.getMessage());
	    ex.printStackTrace();
	}
    }

    public static Settings loadObject()
    {
	try
	{
	    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(FILE_NAME)));
	    Settings setting = (Settings) ois.readObject();
	    return setting;
	}
	catch (Exception ex)
	{
	    Log.v("Serialization Read Error : ", ex.getMessage());
	    ex.printStackTrace();
	}
	return null;
    }
    
    public static void main(String[] args)
    {
	Settings setting = new Settings();
	setting.setColor("red");
	setting.setSize("small");
	setting.setType("type");
	setting.setSite("yahoo.com");
	FileUtil.saveObject(setting);
	setting = (Settings)FileUtil.loadObject();
	System.out.print(setting);
    }
}

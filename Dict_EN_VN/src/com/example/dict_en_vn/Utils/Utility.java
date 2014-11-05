package com.example.dict_en_vn.Utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Utility {
	public static void disableKeyboard(Context context, EditText editText) {
		try {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
			editText.clearFocus();
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		
	}
	public static void savePreferences(Activity context, String key, String value) {
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		 editor.putString(key,value);
		 editor.commit();
	}

	public static String getContentResultItem(String content) {
		try {
			String tagName = "";
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();

			xpp.setInput(new StringReader(content));
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
//					System.out.println("Start document");
				} else if (eventType == XmlPullParser.END_DOCUMENT) {
//					System.out.println("End document");
				} else if (eventType == XmlPullParser.START_TAG) {
					tagName = xpp.getName();
//					System.out.println("Start tag " + xpp.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
//					System.out.println("End tag " + xpp.getName());
				} else if (eventType == XmlPullParser.TEXT) {
//					System.out.println("Text " + xpp.getText());
					if ("li".equals(tagName)) {
						return ("<i>"+xpp.getText()+"</i>");
					}
				}
				eventType = xpp.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
//	public static boolean externalMemoryAvailable() {
//        return android.os.Environment.getExternalStorageState().equals(
//                android.os.Environment.MEDIA_MOUNTED);
//    }

//    public static String getAvailableInternalMemorySize() {
//        File path = Environment.getDataDirectory();
//        StatFs stat = new StatFs(path.getPath());
//        long blockSize = stat.getBlockSize();
//        long availableBlocks = stat.getAvailableBlocks();
//        return formatSize(availableBlocks * blockSize);
//    }
//
//    public static String getTotalInternalMemorySize() {
//        File path = Environment.getDataDirectory();
//        StatFs stat = new StatFs(path.getPath());
//        long blockSize = stat.getBlockSize();
//        long totalBlocks = stat.getBlockCount();
//        return formatSize(totalBlocks * blockSize);
//    }
    public static long getAvailableExternalMemorySize() {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return (availableBlocks * blockSize);
    }
    
//    public static String getTotalExternalMemorySize() {
//        if (externalMemoryAvailable()) {
//            File path = Environment.getExternalStorageDirectory();
//            StatFs stat = new StatFs(path.getPath());
//            long blockSize = stat.getBlockSize();
//            long totalBlocks = stat.getBlockCount();
//            return formatSize(totalBlocks * blockSize);
//        } else {
//            return ERROR;
//        }
//    }

//    public static String formatSize(long size) {
//        String suffix = null;
//
//        if (size >= 1024) {
//            suffix = "KB";
//            size /= 1024;
//            if (size >= 1024) {
//                suffix = "MB";
//                size /= 1024;
//            }
//        }
//
//        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
//
//        int commaOffset = resultBuffer.length() - 3;
//        while (commaOffset > 0) {
//            resultBuffer.insert(commaOffset, ',');
//            commaOffset -= 3;
//        }
//
//        if (suffix != null) resultBuffer.append(suffix);
//        return resultBuffer.toString();
//    }
}

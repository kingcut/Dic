package com.example.dict_en_vn.task;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;

import com.example.dict_en_vn.ConfigLib;
import com.example.dict_en_vn.R;

@SuppressLint("NewApi")
public class UnzipTask extends AsyncTask<String, Integer, Boolean>{
	Context mContext;
	String mZipName;
	private Dialog mDialog;
	private SeekBar mSeekBar;
	private static final double SIZE_UNZIP = 205684913;
	public UnzipTask(Context context,String zipName) {
		mContext = context;
		mZipName = zipName;
		View view = (LayoutInflater.from(mContext).inflate(R.layout.dialog_unzip, null));
		mSeekBar = (SeekBar) view.findViewById(R.id.main_unzip_seekbar);
		mSeekBar.setEnabled(false);
		mDialog = new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(false);
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDialog.show();
	}
	@Override
	protected Boolean doInBackground(String... arg0) {
		return unpackZip();
	}
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result) {
//			File file = new File(ConfigLib.SDCARD_FOLDER +ConfigLib.ASSETS_DATA+ "/" + ConfigLib.ASSETS_DATA+ ConfigLib.DATABASE_TYPE_FILE);
//			if (file.exists()) {
//				file.renameTo(new File(ConfigLib.SDCARD_FOLDER +ConfigLib.ASSETS_DATA+ "/" + ConfigLib.DIC_NAME+ ConfigLib.DATABASE_TYPE_FILE));
//			}
			mDialog.dismiss();
		}else {
			openDialogFullStorage();
		}
	}
	@Override
	protected void onCancelled() {
		super.onCancelled();
		 if (mDialog != null) {
    		 mDialog.dismiss();
		}
	}
	private boolean unpackZip()
	{       
	     InputStream is;
	     ZipInputStream zis;
	     double fileSize = 0;
	     try 
	     {
	         is = mContext.getResources().getAssets().open(ConfigLib.ASSETS_DATA+ConfigLib.DATABASE_TYPE_FILE);
//	    	 is = new FileInputStream(ConfigLib.SDCARD_FOLDER + "anh_viet.zip");
	         zis = new ZipInputStream(new BufferedInputStream(is));          
	         ZipEntry ze;
	         byte[] buffer = new byte[1024];
	         int count;
	         while ((ze = zis.getNextEntry()) != null) 
	         {
	             // Need to create directories if not exists, or
	             // it will generate an Exception...
	             if (ze.isDirectory()) {
		                File fmd = new File(ConfigLib.SDCARD_FOLDER+ ze.getName());
		                fmd.mkdirs();
		                continue;
	             }  
	             FileOutputStream fout = new FileOutputStream(ConfigLib.SDCARD_FOLDER+ ze.getName());
	             // cteni zipu a zapis
	             while ((count = zis.read(buffer)) != -1) 
	             {
	                 fout.write(buffer, 0, count); 
	                 fileSize += count;
	                 mSeekBar.setProgress((int)(fileSize* 100 /SIZE_UNZIP));
//	                 System.out.println("buffer==="+ze.getCompressedSize()+ "==="+fileSize + "=====count=="+count+"==="+(int)(fileSize* 100 / SIZE_UNZIP));
	             }

	             fout.close();               
	             zis.closeEntry();
	         }

	         zis.close();
	     } 
	     catch(IOException e)
	     {	
	    	 if (mDialog != null) {
	    		 mDialog.dismiss();
			}
	         e.printStackTrace();
	         return false;
	     }

	    return true;
	}
	private void openDialogFullStorage() {
		AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
		alert.setMessage(mContext.getResources().getString(R.string.msg_unzip_unsuccesfull)); 
		alert.setPositiveButton(mContext.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = alert.create();
		alertDialog.show();
	}

}

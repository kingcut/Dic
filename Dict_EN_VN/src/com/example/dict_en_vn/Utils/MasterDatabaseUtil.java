package com.example.dict_en_vn.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.dict_en_vn.ConstantKey;

public final class MasterDatabaseUtil {

	private static final String PREF_KEY_MASTER_DB_VERSION="KeyMasterDBVersion";
	private static final String SHARED_PREFERENCE_NAME = "MasterDBPref";
    ////////////////////////////////////////////////////////////////////////////
    // staticフィールド
    ////////////////////////////////////////////////////////////////////////////
	private static final String NAME_DATA_FROM_ASSET = "DicData.db";
    /** DBファイルはデフォルト位置に存在する */
    public static final int DB_PATH_DEFAULT = 0;
    /** DBファイルは代替位置に存在する */
    public static final int DB_PATH_SUB = 1;
    /** DBファイルは存在しない */
    public static final int DB_PATH_ERROR = -1;

    public static final String MASTER_DB_VERSION  = "20130522";

    /** assetsのマスタDBファイルのパス */
//    private static String assetsDbMasterDbPath = "db/townwork_master_20130522.db";
    /** フォルダパス以下のDBファイル名 */
    private static final String DB_FILE = "/" + ConstantKey.DATABASE_NAME;

    public static final String APP_DELAULT = "/files";
    public static final String SQLITE_DELAULT = "/databases";

    ////////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    ////////////////////////////////////////////////////////////////////////////

    /**
     * DBファイル配置チェック
     * ファイルがない場合、assetsからコピーを試みる
     *
     * @param context
     * @return DB_PATH_DEFAULT:Sqliteデフォルトパス
     *         DB_PATH_SUB:アプリパス直下
     *         DB_PATH_ERROR:上記のどれにも存在しない
     */
    public static boolean existDatabase(Context context) {
//        assetsDbMasterDbPath = "/test_minnano_vi_asset.db";

        String appPath = context.getApplicationContext().getFilesDir().getPath();
//        masterVersionCheck(context,appPath);
//        if (existDatabase(appPath)) {
//            return true;
////            return DB_PATH_SUB;
//        }

        String sqlitePath = appPath.substring(0, appPath.lastIndexOf(APP_DELAULT)) + SQLITE_DELAULT;
        masterVersionCheck(context, sqlitePath);
        if (existDatabase(sqlitePath)) {
            // Log.i("SL", "DBファイルは規定パスに存在します");
            return true;
//            return DB_PATH_DEFAULT;
        }
        AssetManager aManager = context.getAssets();

        copyDatabaseFromAsset(aManager, sqlitePath);
//        if (existDatabase(sqlitePath)) {
//        	saveDBVersionToPref(context, PREF_KEY_MASTER_DB_VERSION, MASTER_DB_VERSION);
////            return DB_PATH_DEFAULT;
//            return true;
//        }

//        copyDatabaseFromAsset(aManager, appPath);
//        if (existDatabase(appPath)) {
//        	saveDBVersionToPref(context, PREF_KEY_MASTER_DB_VERSION, MASTER_DB_VERSION);
//            return true;
////            return DB_PATH_SUB;
//        }

//        return DB_PATH_ERROR;
        return true;
    }
    
    /**
     * save db version to preference
     * @param context
     * @param key
     * @param value
     */
    private static void saveDBVersionToPref(Context context, String key, String value){
    	SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    	Editor editor = pref.edit();
    	editor.putString(key, value);
    	editor.commit();
    }

    /**
     * force copy database
     * @param context
     * @return
     */
    public static int forceCopy(Context context) {
        AssetManager aManager = context.getAssets();

        String appPath = context.getApplicationContext().getFilesDir().getPath();
        String sqlitePath = appPath.substring(0, appPath.lastIndexOf(APP_DELAULT)) + SQLITE_DELAULT;

        copyDatabaseFromAsset(aManager, sqlitePath);
        if (existDatabase(sqlitePath)) {
            return DB_PATH_DEFAULT;
        }

        copyDatabaseFromAsset(aManager, appPath);
        if (existDatabase(appPath)) {
            return DB_PATH_SUB;
        }

        return DB_PATH_ERROR;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param folderPath
     * @return
     */
    private static boolean existDatabase(String folderPath) {
        File databaseFile = new File(folderPath + DB_FILE);
        return databaseFile.exists();
    }

    /**
     *
     * @throws IOException
     */
    public static boolean copyDatabaseFromAsset(AssetManager aManager, String folderPath) {
        boolean success = true;
        InputStream in = null;
        OutputStream out = null;
        try {
        	File desFolder = new File(folderPath);
        	 if (desFolder.exists() || desFolder.mkdirs()) {
               in = aManager.open(NAME_DATA_FROM_ASSET);
           } else {
               return false;
           }
	          File outFile =  new File(folderPath + DB_FILE);
//        	if (Integer.parseInt(android.os.Build.VERSION.SDK) <= 8) {
//				in = aManager.open(NAME_DATA_FROM_ASSET_V8);
//				outFile = new File(ConfigLib.SDCARD_FOLDER+NAME_DATA_FROM_ASSET_V8);
//			}else {
//				in = aManager.open(NAME_DATA_FROM_ASSET);
//				outFile = new File(ConfigLib.SDCARD_FOLDER+NAME_DATA_FROM_ASSET);
//			}
          Log.i("copy data", "copy data from asset"+outFile.getPath());
          out = new FileOutputStream(outFile);
          copyFile(in, out);
          in.close();
          in = null;
          out.flush();
          out.close();
          out = null;
          return true;
        } catch(IOException e) {
        	e.printStackTrace();
            Log.e("tag", "Failed to copy asset file: " + NAME_DATA_FROM_ASSET, e);
            return false;
        }       
//        InputStream is = null;
//        try {
//            File folder = new File(folderPath);
//            if (folder.exists() || folder.mkdirs()) {
//                is = aManager.open(assetsDbMasterDbPath);
//                FileUtil.write(is, new File(folderPath + DB_FILE));
//            } else {
//                success = false;
//            }
//        } catch (IOException e) {
//            if (BuildConfig.DEBUG){
//                Log.w("copyDatabase","IOException",e);
//            }
//            success = false;
//        } catch (R2SystemException e) {
//            if (BuildConfig.DEBUG){
//                Log.w("copyDatabase","R2SystemException",e);
//            }
//            success = false;
//        } finally {
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    if (BuildConfig.DEBUG){
//                        Log.w("copyDatabase","IOException",e);
//                    }
//                }
//            }
//        }
//        return success;
    }
    private static void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
    /**
     * check master version
     * @param context
     * @param folderPath
     */
    private static boolean masterVersionCheck(Context context,String folderPath){
    	SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
    	String masterVerson = pref.getString(PREF_KEY_MASTER_DB_VERSION, null);
    	File databaseFile = new File(folderPath + DB_FILE);
    	if(masterVerson == null){
			if (databaseFile.exists()) {
				return databaseFile.delete();
			}
    		return false;
    	}
    	if(masterVerson.compareTo(MASTER_DB_VERSION) >= 0){
    		return false;
    	}
    	return databaseFile.delete();
    }

    private MasterDatabaseUtil() {
    }
}

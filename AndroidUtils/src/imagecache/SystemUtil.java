package imagecache;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * 
 * @author Shixiong Zhu(zsxwing@gmail.com)
 * 
 */
public class SystemUtil {

	/**
	 * 为了保证在各种系统中使用�?��的字符编码，在用到字符编码的地方统一使用这个函数�?
	 * 
	 * @return
	 */
	public static String getDefaultEncoding() {
		return "UTF-8";
	}

	public static String encodeURL(String str) {
		try {
			return URLEncoder.encode(str, getDefaultEncoding()).replaceAll("\\+", "%20").replaceAll("\\*", "%2A");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取APP的当前版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			System.out.println("version:" + packageInfo.versionName);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "Unknown";
		}
	}

	/**
	 * �?��当前的网络是否可用�?
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkEnable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		return info != null && info.isAvailable();
		// return true;
	}

	/**
	 * �?��SD卡是否可用�?
	 * 
	 * @return 当SD卡不存在，或者没有mount的时候，返回false�?
	 */
	public static boolean hasSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	public static void makeAppDir() {
		String basePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		File root = new File(basePath + "/ChangyanDemo");
		root.mkdir();
		File audio = new File(basePath + "/ChangyanDemo/audio");
		audio.mkdir();
	}

	public static String getAudioPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + "/ChangyanDemo/audio/";
	}

	public static SharedPreferences getSharedPreferences(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences;
	}

	public static String getIMEI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	public static void showException(Context context, Throwable e) {
		Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
	}

}

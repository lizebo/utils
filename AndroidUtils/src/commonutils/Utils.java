package commonutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.protocol.HTTP;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;

public class Utils {
	public static UrlEncodedFormEntity getPostParamters(List<NameValuePair> params)
			throws UnsupportedEncodingException {
		if (params == null || params.isEmpty()) {
			return null;
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		return entity;
	}

	public static String getGetURL(String url, List<NameValuePair> params)
			throws MalformedURLException, UnsupportedEncodingException {
		if (url == null)
			return null;
		StringBuffer surl = new StringBuffer();
		Log.d("url", url);
		surl.append(url);
		if (params != null) {
			surl.append("?");
			for (int i = 0; i < params.size(); i++) {
				String name = URLEncoder.encode(params.get(i).getName(),
						HTTP.UTF_8);
				String value = URLEncoder.encode(params.get(i).getValue(),
						HTTP.UTF_8);
				if (i == 0) {
					surl.append(name).append("=").append(value);
				} else {
					surl.append("&").append(name).append("=").append(value);
				}

			}
		}
		URL mUrl = new URL(surl.toString());
		// Log.d("---------------------", mUrl.toURI());
		return mUrl.toString();
	}

	public static String InputStream2String(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is,
				"utf_8"));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();
	}

	public static String StringToMD5(String str) throws NoSuchAlgorithmException{
		MessageDigest md5=MessageDigest.getInstance("MD5");
		md5.update(str.getBytes());
		byte[] m = md5.digest();
		return getString(m);
	}

	private static String getString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(b[i]);
		}
		return sb.toString();
	}
	
	public static int dip2px(Context context, float dpValue) {
	    final float scale = context.getResources().getDisplayMetrics().density;
	    return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
	    final float scale = context.getResources().getDisplayMetrics().density;
	    return (int) (pxValue / scale + 0.5f);
	}
	public static Bitmap drawCircleBitmap(Context context,Bitmap bitmap) {
		int targetWidth = dip2px(context, 90);
		int targetHeight = targetWidth;
		Bitmap sourceBitmap = bitmap;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);
		canvas.clipPath(path);

		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		return targetBitmap;
	}
	/**
	* 获取压缩后的图片
	* @param res
	* @param resId
	* @param reqWidth            所需图片压缩尺寸最小宽度
	* @param reqHeight           所需图片压缩尺寸最小高度
	* @return
	*/
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {
	   
	    // 首先不加载图片,仅获取图片尺寸
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    // 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
	    options.inJustDecodeBounds = true;
	    // 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
	    BitmapFactory.decodeResource(res, resId, options);

	    // 计算压缩比例,如inSampleSize=4时,图片会压缩成原图的1/4
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // 当inJustDecodeBounds设为false时,BitmapFactory.decode...就会返回图片对象了
	    options. inJustDecodeBounds = false;
	    // 利用计算的比例值获取压缩后的图片对象
	    return BitmapFactory.decodeResource(res, resId, options);
	}
}

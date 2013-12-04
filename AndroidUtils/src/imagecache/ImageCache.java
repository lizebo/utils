package imagecache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;


public class ImageCache {
	private final static String TAG = ImageCache.class.getName();

	private volatile static ImageCache imageCache = null;

	public static ImageCache getImageCache() {
		if (imageCache == null) {
			synchronized (ImageCache.class) {
				if (imageCache == null) {
					imageCache = new ImageCache();
					loadFileCache();
				}
			}
		}
		return imageCache;
	}

	private File dir;

	private static Map<String, SoftReference<Drawable>> memoryCache = new HashMap<String, SoftReference<Drawable>>(); // url,drawable

	private static Map<String, File> diskCache = new HashMap<String, File>();

	private ImageCache() {
		try {
			dir = ImageStore.getCachedPath();
		} catch (StorageException e) {
			e.printStackTrace();
			
		}
	}

	public synchronized Drawable get(String url) {
		Drawable drawable = getFromMemory(url);
		if (drawable == null) {
			drawable = getFromDisk(url);
		}
		return drawable;
	}

	private Drawable getFromMemory(String url) {
		if (memoryCache.containsKey(url)) {
			SoftReference<Drawable> drawable = memoryCache.get(url);
			if (drawable != null) {
				Drawable d = drawable.get();
				if (d != null) {
					return d;
				}
			}
		}
		return null;
	}

	private Drawable getFromDisk(String url) {
		String fileName = Url2Filename(url);
		if (fileName == null) {
			return null;
		}
		if (diskCache.containsKey(fileName)) {
			try {
				InputStream input = new FileInputStream(diskCache.get(fileName));
				try {
					Drawable drawable = Drawable.createFromStream(input, url);
					if (drawable != null) {
						memoryCache.put(url, new SoftReference<Drawable>(
								drawable));
					}
					return drawable;
				} finally {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, e.getMessage(), e);
			}
		}
		return null;
	}

	private void putToMemory(String url, Drawable drawable) {
		memoryCache.put(url, new SoftReference<Drawable>(drawable));
	}

	public static String Url2Filename(String url) {
		byte[] byteMD5 = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");

			md5.update(url.getBytes());
			byteMD5 = md5.digest();
		} catch (NoSuchAlgorithmException e) {
			// TODO
		}
		return new BigInteger(byteMD5).toString(16);
	}

	private void putToDisk(String url, Drawable drawable) throws IOException {
		String fileName = Url2Filename(url);
		if (fileName == null) {
			throw new IOException("文件保存失败");
		}
		File file = new File(dir, fileName);
		OutputStream output = new FileOutputStream(file);
		try {
			((BitmapDrawable) drawable).getBitmap().compress(
					CompressFormat.PNG, 100, output);
			output.flush();
			diskCache.put(fileName, file);
		} finally {
			output.close();
		}
	}

	public static void loadFileCache() {
		File img;
		try {
			img = ImageStore.getCachedPath();
			if (img.exists()) {
				if (img.isDirectory()) {
					for (File file : img.listFiles()) {
						diskCache.put(file.getName(), file);
					}
				}
			}
		} catch (StorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public synchronized void put(String url, Drawable drawable) {
		putToMemory(url, drawable);
		try {
			putToDisk(url, drawable);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void clearImageCache() {
		File img;
		//clear cached
		try {
			img = ImageStore.getCachedPath();
			if (img.exists()) {
				if (img.isDirectory()) {
					for (File file : img.listFiles()) {
						file.delete();
					}
				}
			}
		} catch (StorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			diskCache.clear();
		}
		//clear temp
		try {
			img = ImageStore.getTempPath();
			if (img.exists()) {
				if (img.isDirectory()) {
					for (File file : img.listFiles()) {
						file.delete();
					}
				}
			}
		} catch (StorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//clear cached/drafts
		try {
			img = ImageStore.getDraftPath();
			if (img.exists()) {
				if (img.isDirectory()) {
					for (File file : img.listFiles()) {
						file.delete();
					}
				}
			}
		} catch (StorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

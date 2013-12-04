package imagecache;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

/**
 * 
 */
public class ImageStore {

	/**
	 */
	private static final String TEMP_PATH = "temp";
	private static final String CACHED_PATH = "cached";
	private static final String DRAFT_PATH = "cached/drafts";
	private static final String IMAGE_PATH = "images";

	/**
	 * 保存自动升级�?��载的apk文件
	 */
	private static final String UPDATE_PATH = "cached/updates";

	private static final String FEED_PATH = "cached/feed";

	private static final String ROOT_PATH = "changyan";

	public static File getCachedPath() throws StorageException {
		File cache = getDirOrCreate(new File(getRoot(), CACHED_PATH));
		File nomedia = new File(cache, ".nomedia");
		if (!nomedia.exists()) {
			try {
				nomedia.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return cache;
	}

	public static File getDraftPath() throws StorageException {
		File draft = getDirOrCreate(new File(getRoot(), DRAFT_PATH));
		File nomedia = new File(draft, ".nomedia");
		if (!nomedia.exists()) {
			try {
				nomedia.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return draft;
	}

	public static File getImagePath() throws StorageException {
		File img = getDirOrCreate(new File(getRoot(), IMAGE_PATH));
		return img;
	}

	public static File getUpdatePath() throws StorageException {
		return getDirOrCreate(new File(getRoot(), UPDATE_PATH));
	}

	public static File getFeedPath() throws StorageException {
		return getDirOrCreate(new File(getRoot(), FEED_PATH));
	}

	public static void clearTemp() {
		try {
			File temp = getTempPath();
			if (temp.exists()) {
				if (temp.isDirectory()) {
					for (File file : temp.listFiles()) {
						if (file.getName().equals(".nomedia"))
							continue;
						file.delete();
					}
				} else {
					// temp.delete();
				}
			}
		} catch (Exception e) {
		}
	}

	public static File getTempPath() throws StorageException {
		File temp = getDirOrCreate(new File(getRoot(), TEMP_PATH));
		// 由于我们不希望temp里面的内容被手机自带的媒体管理软件扫描到
		// 因此�?��创建�?��.nomedia空文件来告诉系统
		File nomedia = new File(temp, ".nomedia");
		if (!nomedia.exists()) {
			try {
				nomedia.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}

	/**
	 * 优先是使用SD卡，如果不存在SD卡，就是用手机自身的存储空间�?
	 * 
	 * @return
	 * @throws StorageException
	 */
	private static File getRoot() throws StorageException {
		File root = null;
		if (SystemUtil.hasSDCard()) {
			root = new File(Environment.getExternalStorageDirectory(),
					ROOT_PATH);
		}else{
			throw new StorageException("can't find sdcard", "");
		} 
		return getDirOrCreate(root);
	}

	private static File getDirOrCreate(File dir) throws StorageException {

		if (!dir.exists()) {
			if (dir.mkdir() == false) {
				throw new StorageException("could not create dir " + dir,
						"");
			}
		} else if (!dir.isDirectory()) {
			throw new StorageException(dir + " is not a dir", "");
		}
		return dir;
	}
}
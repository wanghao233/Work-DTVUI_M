package com.changhong.tvos.dtv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AsyncImageLoader {
	private static final String DEFAULT_PNG = "/data/dtv/logo/TV.png";
	private static final String TAG = "AsyncImageLoader";
	private HashMap<String, SoftReference<Drawable>> imageCache;
	private static AsyncImageLoader asyncImageLoader;

	public static AsyncImageLoader getInstance() {
		if (null == asyncImageLoader) {
			asyncImageLoader = new AsyncImageLoader();
		}

		return asyncImageLoader;
	}

	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	public Drawable loadDrawable(final String imageUrl, final ImageCallback imageCallback) {
		if (imageCache.containsKey(imageUrl)) {
			Log.i(TAG, "loadDrawable--> 111file url in imageUrl = " + imageUrl);
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = null;
			if (null != softReference) {

				drawable = softReference.get();
				Log.i(TAG, "loadDrawable--> 222file url in drawable = " + drawable);
			} else {
				Log.i(TAG, "loadDrawable--> 333file url in softReference = " + softReference);
			}
			if (drawable != null) {
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	public void clearCache() {
		//		Set<String> keySet = imageCache.keySet();
		//		Iterator<String> iterator = keySet.iterator();
		//		while (iterator.hasNext()) {
		//			SoftReference<Drawable> softReference = imageCache.get(iterator.next());
		//			softReference.clear();
		//		}
		//		imageCache.clear();
	}

	public static Drawable loadImageFromUrl(String url) {
		Drawable d = null;
		File file = new File(url);
		Log.i(TAG, "EL--> file url in url = " + url);
		if (!file.exists()) {
			Log.i(TAG, "EL--> file url not exits DEFAULT_PNG" + DEFAULT_PNG);
			file = new File(DEFAULT_PNG);

		}

		FileInputStream i = null;

		try {
			i = new FileInputStream(file);
			d = Drawable.createFromStream(i, "src");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (i != null) {
				try {
					i.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Log.i(TAG, "d=" + d);
		return d;
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}

}

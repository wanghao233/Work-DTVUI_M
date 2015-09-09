package com.changhong.tvos.dtv.epg.normal;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class AsyncImageLoader {

	public static HashMap<String, SoftReference<Drawable>> imageCache;//2015-3-20
	private static AsyncImageLoader instance;

	private AsyncImageLoader() {
		if (imageCache == null || imageCache.size() >= 100) {//当存了100张海报时，重新取数据
			imageCache = new HashMap<String, SoftReference<Drawable>>();
		}
	}

	public synchronized static AsyncImageLoader getInstance() {
		if (instance == null) {
			instance = new AsyncImageLoader();
		}
		return instance;
	}

	public Drawable loadDrawable(final String imageUrl, final ImageCallback imageCallback) {
		//		Log.i("YangLiu", "imageCache里有它的缓冲="+imageCache.containsKey(imageUrl));
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			if (softReference != null) {
				//				Log.i("YangLiu", "取得imageCache里的缓冲");
				Drawable drawable = softReference.get();
				if (drawable != null) {
					//					Log.i("YangLiu", "显示缓冲里的图片");
					return drawable;
				}
			}
		}
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
				//				Log.i("YangLiu", "将取得的图片回传");
			}
		};
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				//				Log.i("YangLiu", "取得图片成功，图片地址为："+imageUrl+"	drawable="+drawable);
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	public static Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		Drawable d = Drawable.createFromStream(i, "src");
		return d;
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}
}

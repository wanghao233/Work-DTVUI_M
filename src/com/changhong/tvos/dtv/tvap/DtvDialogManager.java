package com.changhong.tvos.dtv.tvap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.changhong.tvos.dtv.scan.MenuScan;
import android.app.Dialog;

public class DtvDialogManager {
	private static List<Dialog> list = new ArrayList<Dialog>();

	public static void AddShowDialog(Dialog d) {
		list.add(d);
	}

	public static void RemoveDialog(Dialog d) {
		// list.remove(d);
	}

	public static void ClearDialogs() {
		Iterator<Dialog> iter = list.iterator();
		if (null != iter) {

			while (iter.hasNext()) {
				Dialog d = iter.next();

				if (d.isShowing()) {
					if (d instanceof MenuScan) {
						d.setOnDismissListener(null);
					}
					d.dismiss();
				}
			}
		}
		list.clear();
	}
}
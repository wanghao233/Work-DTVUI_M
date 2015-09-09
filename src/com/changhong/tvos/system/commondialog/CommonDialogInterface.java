package com.changhong.tvos.system.commondialog;

/**
 * 通用弹出菜单接口，定义OnTimeoutListener，用于窗口自消失后的事件侦听
 * @author jinwei.yang
 * 2011-10-27
 */
public interface CommonDialogInterface {
	interface OnTimeoutListener {
        /**
         * This method will be invoked when the dialog is time out.
         * 
         * @param dialog The dialog that was time out will be passed into the
         *            method.
         */
        public void onTimeout(CommonDialogInterface dialog);
    }
}
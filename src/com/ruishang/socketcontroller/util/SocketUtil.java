package com.ruishang.socketcontroller.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import android.content.Context;
import android.util.Log;

/**
 * Socket的帮助类
 * 
 * @author zoou.sq<br>
 * @version 2013-08-02 xu.xb 加入移动EditText光标的方法<br>
 * 
 */
public class SocketUtil {

	private static final String TAG = "SocketUtil";
	private static Socket mSocket;
	private static OutputStream mOutputStream;

	public static boolean connect(Context context) {
		boolean isSuccess = false;
		String ip = SharedPreferenceUtil.getStringValueByKey(context, ConstantSet.CONFIG_FILE_NAME,
				ConstantSet.KEY_CONFIG_IP);
		int port = SharedPreferenceUtil.getIntegerValueByKey(context, ConstantSet.CONFIG_FILE_NAME,
				ConstantSet.KEY_CONFIG_PORT);
		if (!isConnect()) {
			try {
				mSocket = new Socket(ip, port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (null != mSocket) {
			try {
				mOutputStream = mSocket.getOutputStream();
				isSuccess = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.d(TAG, ip + ":" + port + " soket连接：" + isSuccess);
		return isSuccess;
	}

	private static boolean isConnect() {
		return null != mSocket && mSocket.isConnected();
	}

	public static void sendCommand(Context context, String tag) throws IOException {
		byte[] buffer = Command.getCommand(context, tag);
		if (0 == buffer.length) {
			return;
		}
		Log.d("aaa", Arrays.toString(buffer));
		try {
			if (null != mOutputStream) {
				mOutputStream.write(buffer);
				mOutputStream.flush();
			}
		} catch (IOException e) {
			throw e;// 异常信息
		} finally {
			// close();
		}
	}

	public static void close() {
		if (null != mOutputStream) {
			try {
				mOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				mOutputStream = null;
			}
		}
		if (null != mSocket) {
			try {
				mSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				mSocket = null;
			}
		}
	}
}

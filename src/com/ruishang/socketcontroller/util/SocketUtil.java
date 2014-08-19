package com.ruishang.socketcontroller.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

/**
 * UI的帮助类
 * 
 * @author wang.xy<br>
 * @version 2013-08-02 xu.xb 加入移动EditText光标的方法<br>
 * 
 */
public class SocketUtil {

	private static Socket mSocket;
	private static OutputStream mOutputStream;

	public static boolean connect() {
		boolean isSuccess = false;
		if (!isConnect()) {
			try {
				mSocket = new Socket("192.168.0.101", 9992);
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
		Log.d("aaa", "soket连接：" + isSuccess);
		return isSuccess;
	}

	private static boolean isConnect() {
		return null != mSocket && mSocket.isConnected();
	}

	public static boolean sendCommand(byte[] buffer) {
		boolean isSuccess = false;
		try {
			mOutputStream.write(buffer, 0, buffer.length);
			mOutputStream.flush();
			close();
			isSuccess = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d("aaa", "发送数据：" + isSuccess);
		return isSuccess;
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

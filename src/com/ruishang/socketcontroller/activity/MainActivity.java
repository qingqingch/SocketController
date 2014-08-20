package com.ruishang.socketcontroller.activity;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruishang.socketcontroller.R;
import com.ruishang.socketcontroller.listener.OnPopDismissListener;
import com.ruishang.socketcontroller.util.Command;
import com.ruishang.socketcontroller.util.ConstantSet;
import com.ruishang.socketcontroller.util.LoadingUpView;
import com.ruishang.socketcontroller.util.PopWindowUtil;
import com.ruishang.socketcontroller.util.SharedPreferenceUtil;
import com.ruishang.socketcontroller.util.SocketUtil;
import com.ruishang.socketcontroller.util.StringUtil;
import com.ruishang.socketcontroller.util.UIUtil;

public class MainActivity extends Activity implements OnClickListener, OnLongClickListener {

	private static final String TAG = "MainActivity";
	private static final int SPACE_VALUE = 50;
	private static final int NUM_COLUMNS = 2;
	private static final long WAIT_TIME = 2000;
	private static final int STATUS_SEND_SUCCESS = 0;
	private static final int STATUS_SEND_FAIL = 1;
	private static final int STATUS_CONNECT_FAIL = 2;
	private int mWidth;
	private Button mBtnComputerOn;
	private Button mBtnComputerOff;
	private Button mBtnWallOn;
	private Button mBtnWallOff;
	private LinearLayout mLlSetting;
	private View mMenuView;
	private View mModifyView;
	private EditText mEdtCommand;
	private EditText mEdtIp;
	private EditText mEdtPort;
	private PopWindowUtil mPopWindowUtil;
	private PopWindowUtil mModifyPopWindowUtil;
	private Button mBtnSave;
	private Button mBtnSaveCommand;
	private long mTouchTime;
	private LoadingUpView mLoadingUpView;
	private String mCurrentTag;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			if (null != mLoadingUpView && mLoadingUpView.isShowing()) {
				mLoadingUpView.dismiss();
			}
			String info = (String) msg.obj;
			switch (what) {
				case STATUS_SEND_SUCCESS:
					if (StringUtil.isNullOrEmpty(info)) {
						info = getString(R.string.send_success);
					}
					Toast.makeText(MainActivity.this, info, Toast.LENGTH_SHORT).show();
					break;
				case STATUS_SEND_FAIL:
					if (StringUtil.isNullOrEmpty(info)) {
						info = getString(R.string.send_fail);
					}
					Toast.makeText(MainActivity.this, info, Toast.LENGTH_SHORT).show();
					break;
				case STATUS_CONNECT_FAIL:
					if (StringUtil.isNullOrEmpty(info)) {
						info = getString(R.string.connect_fail);
					}
					Toast.makeText(MainActivity.this, info, Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initVariables();
		initView();
		setListener();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
				.detectNetwork().penaltyLog().build());
	}

	private void initVariables() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mWidth = metric.widthPixels;
		mLoadingUpView = new LoadingUpView(this);
	}

	private void initView() {
		mMenuView = LayoutInflater.from(this).inflate(R.layout.view_setting_pop, null);
		mModifyView = LayoutInflater.from(this).inflate(R.layout.view_setting_command, null);

		mEdtIp = (EditText) mMenuView.findViewById(R.id.edt_ip);
		mEdtPort = (EditText) mMenuView.findViewById(R.id.edt_port);
		mBtnSave = (Button) mMenuView.findViewById(R.id.btn_save);

		mBtnSaveCommand = (Button) mModifyView.findViewById(R.id.btn_save_command);
		mEdtCommand = (EditText) mModifyView.findViewById(R.id.edt_command);

		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.app_name);
		mLlSetting = (LinearLayout) findViewById(R.id.title_with_back_title_btn_right);
		TextView tvRight = (TextView) findViewById(R.id.tv_title_with_right);
		tvRight.setText(R.string.setting);
		tvRight.setBackgroundResource(R.drawable.btn_selector);
		mBtnComputerOn = (Button) findViewById(R.id.btn_computer_on);
		mBtnComputerOff = (Button) findViewById(R.id.btn_computer_off);
		mBtnWallOn = (Button) findViewById(R.id.btn_wall_on);
		mBtnWallOff = (Button) findViewById(R.id.btn_wall_off);
		mBtnComputerOn.setTag(ConstantSet.KEY_COMMAND_COMPUTER_ON);
		mBtnComputerOff.setTag(ConstantSet.KEY_COMMAND_COMPUTER_OFF);
		mBtnWallOn.setTag(ConstantSet.KEY_COMMAND_WALL_ON);
		mBtnWallOff.setTag(ConstantSet.KEY_COMMAND_WALL_OFF);
		layoutButton(mBtnComputerOn);
		layoutButton(mBtnComputerOff);
		layoutButton(mBtnWallOn);
		layoutButton(mBtnWallOff);
	}

	private void setListener() {
		mBtnComputerOn.setOnLongClickListener(this);
		mBtnComputerOff.setOnLongClickListener(this);
		mBtnWallOn.setOnLongClickListener(this);
		mBtnWallOff.setOnLongClickListener(this);

		mBtnComputerOn.setOnClickListener(this);
		mBtnComputerOff.setOnClickListener(this);
		mBtnWallOn.setOnClickListener(this);
		mBtnWallOff.setOnClickListener(this);
		mLlSetting.setOnClickListener(this);
		mBtnSave.setOnClickListener(this);
		mBtnSaveCommand.setOnClickListener(this);
	}

	private void layoutButton(Button button) {
		LayoutParams layoutParams = button.getLayoutParams();
		layoutParams.width = (mWidth - UIUtil.dip2px(this, SPACE_VALUE) * (NUM_COLUMNS + 1)) / NUM_COLUMNS;
		layoutParams.height = layoutParams.width;
		button.setLayoutParams(layoutParams);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.btn_computer_on:
				sendCommand(ConstantSet.KEY_COMMAND_COMPUTER_ON);
				break;
			case R.id.btn_computer_off:
				sendCommand(ConstantSet.KEY_COMMAND_COMPUTER_OFF);
				break;
			case R.id.btn_wall_on:
				sendCommand(ConstantSet.KEY_COMMAND_WALL_ON);
				break;
			case R.id.btn_wall_off:
				sendCommand(ConstantSet.KEY_COMMAND_WALL_OFF);
				break;
			case R.id.btn_save:
				checkIpAndPort();
				break;
			case R.id.btn_save_command:
				saveCommand();
				break;
			case R.id.title_with_back_title_btn_right:
				showSettingPop();
				break;

			default:
				break;
		}
	}

	private void saveCommand() {
		String command = mEdtCommand.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(command)) {
			Toast.makeText(this, getString(R.string.input_command), Toast.LENGTH_SHORT).show();
			return;
		}
		SharedPreferenceUtil.saveValue(this, ConstantSet.CONFIG_FILE_NAME, mCurrentTag, command);
		mModifyPopWindowUtil.dissmiss();

	}

	private void checkIpAndPort() {
		String ip = mEdtIp.getText().toString().trim();
		String port = mEdtPort.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(ip)) {
			Toast.makeText(this, getString(R.string.input_ip), Toast.LENGTH_SHORT).show();
			return;
		}
		if (StringUtil.isNullOrEmpty(port)) {
			Toast.makeText(this, getString(R.string.input_port), Toast.LENGTH_SHORT).show();
			return;
		}
		SharedPreferenceUtil.saveValue(this, ConstantSet.CONFIG_FILE_NAME, ConstantSet.KEY_CONFIG_IP, ip);
		SharedPreferenceUtil.saveValue(this, ConstantSet.CONFIG_FILE_NAME, ConstantSet.KEY_CONFIG_PORT,
				Integer.valueOf(port));
		mPopWindowUtil.dissmiss();
	}

	private void sendCommand(final String tag) {
		if (!checkIp()) {
			showSettingPop();
			return;
		}
		if (null != mLoadingUpView && !mLoadingUpView.isShowing()) {
			mLoadingUpView.isShowing();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (SocketUtil.connect(MainActivity.this)) {
					try {
						SocketUtil.sendCommand(MainActivity.this, tag);
						sendToHandle(STATUS_SEND_SUCCESS, getString(R.string.send_success));
					} catch (IOException e) {
						sendToHandle(STATUS_SEND_FAIL, e.toString());
						e.printStackTrace();
					}
				} else {
					sendToHandle(STATUS_CONNECT_FAIL, getString(R.string.connect_fail));
				}
			}
		}).start();
	}

	private void sendToHandle(int what, String info) {
		Message msg = mHandler.obtainMessage();
		msg.what = what;
		msg.obj = info;
		mHandler.sendMessage(msg);
	}

	private boolean checkIp() {
		boolean hasIp = false;
		String ip = SharedPreferenceUtil.getStringValueByKey(this, ConstantSet.CONFIG_FILE_NAME,
				ConstantSet.KEY_CONFIG_IP);
		int port = SharedPreferenceUtil.getIntegerValueByKey(this, ConstantSet.CONFIG_FILE_NAME,
				ConstantSet.KEY_CONFIG_PORT);
		if (!StringUtil.isNullOrEmpty(ip) && SharedPreferenceUtil.INVALID_CODE != port) {
			hasIp = true;
		}
		return hasIp;
	}

	@Override
	public void onBackPressed() {
		if (null != mPopWindowUtil && mPopWindowUtil.isShowing()) {
			mPopWindowUtil.dissmiss();
			return;
		}
		long currentTime = System.currentTimeMillis();
		if ((currentTime - mTouchTime) >= WAIT_TIME) {
			Toast.makeText(this, getString(R.string.once_press_quit), Toast.LENGTH_SHORT).show();
			mTouchTime = currentTime;
			return;
		} else {
			finish();
		}
		super.onBackPressed();
	}

	@Override
	public boolean onLongClick(View view) {
		int id = view.getId();
		String tag = (String) view.getTag();
		switch (id) {
			case R.id.btn_computer_on:
			case R.id.btn_computer_off:
			case R.id.btn_wall_on:
			case R.id.btn_wall_off:
				mCurrentTag = tag;
				modifyCommand(tag);
				break;
			default:
				break;
		}
		return true;
	}

	private void modifyCommand(String tag) {
		if (null == mModifyPopWindowUtil) {
			mModifyPopWindowUtil = new PopWindowUtil(mModifyView, mLlSetting, new OnPopDismissListener() {

				@Override
				public void onDismiss() {
				}
			});
		}
		String command = SharedPreferenceUtil.getStringValueByKey(this, ConstantSet.CONFIG_FILE_NAME, tag);
		if (StringUtil.isNullOrEmpty(command)) {
			command = Command.getCommandString(MainActivity.this, tag);
		}
		mEdtCommand.setText(command);
		mEdtCommand.setSelection(mEdtCommand.length());
		mModifyPopWindowUtil.changeStatus();

	}

	private void showSettingPop() {
		if (null == mPopWindowUtil) {
			mPopWindowUtil = new PopWindowUtil(mMenuView, mLlSetting, new OnPopDismissListener() {

				@Override
				public void onDismiss() {
				}
			});
		}
		String ipString = SharedPreferenceUtil.getStringValueByKey(this, ConstantSet.CONFIG_FILE_NAME,
				ConstantSet.KEY_CONFIG_IP);
		int port = SharedPreferenceUtil.getIntegerValueByKey(this, ConstantSet.CONFIG_FILE_NAME,
				ConstantSet.KEY_CONFIG_PORT);
		String portString = "";
		if (SharedPreferenceUtil.INVALID_CODE != port) {
			portString = port + "";
		}
		mEdtPort.setText(portString);
		mEdtIp.setSelection(portString.length());

		if (StringUtil.isNullOrEmpty(ipString)) {
			mEdtIp.setText("");
		} else {
			mEdtIp.setText(ipString);
			mEdtIp.setSelection(ipString.length());
		}

		mEdtPort.setText(SharedPreferenceUtil.INVALID_CODE == port ? "" : port + "");
		mPopWindowUtil.changeStatus();
	}

}

package com.ruishang.socketcontroller.activity;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.ruishang.socketcontroller.util.PopWindowUtil;
import com.ruishang.socketcontroller.util.SharedPreferenceUtil;
import com.ruishang.socketcontroller.util.SocketUtil;
import com.ruishang.socketcontroller.util.StringUtil;
import com.ruishang.socketcontroller.util.UIUtil;

public class MainActivity extends Activity implements OnClickListener {

	private static final String TAG = "MainActivity";
	private static final int SPACE_VALUE = 50;
	private static final int NUM_COLUMNS = 2;
	private int mWidth;
	private Button mBtnComputerOn;
	private Button mBtnComputerOff;
	private Button mBtnWallOn;
	private Button mBtnWallOff;
	private LinearLayout mLlSetting;
	private View mMenuView;
	private EditText mEdtIp;
	private EditText mEdtPort;
	private PopWindowUtil mPopWindowUtil;
	private Button mBtnSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initVariables();
		initView();
		setListener();
	}

	private void showSettingPop() {
		if (null == mPopWindowUtil) {
			mPopWindowUtil = new PopWindowUtil(mMenuView, mLlSetting,
					new OnPopDismissListener() {

						@Override
						public void onDismiss() {
						}
					});
		}
		mEdtIp.setText(SharedPreferenceUtil.getStringValueByKey(this,
				ConstantSet.CONFIG_FILE_NAME, ConstantSet.KEY_CONFIG_IP));
		mEdtPort.setText(SharedPreferenceUtil.getIntegerValueByKey(this,
				ConstantSet.CONFIG_FILE_NAME, ConstantSet.KEY_CONFIG_PORT) + "");
		mPopWindowUtil.changeStatus();
	}

	private void initVariables() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mWidth = metric.widthPixels;
	}

	private void initView() {
		mMenuView = LayoutInflater.from(this).inflate(
				R.layout.view_setting_pop, null);
		mEdtIp = (EditText) mMenuView.findViewById(R.id.edt_ip);
		mEdtPort = (EditText) mMenuView.findViewById(R.id.edt_port);
		mBtnSave = (Button) mMenuView.findViewById(R.id.btn_save);
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
		layoutButton(mBtnComputerOn);
		layoutButton(mBtnComputerOff);
		layoutButton(mBtnWallOn);
		layoutButton(mBtnWallOff);
	}

	private void setListener() {
		mBtnComputerOn.setOnClickListener(this);
		mBtnComputerOff.setOnClickListener(this);
		mBtnWallOn.setOnClickListener(this);
		mBtnWallOff.setOnClickListener(this);
		mLlSetting.setOnClickListener(this);
		mBtnSave.setOnClickListener(this);
	}

	private void layoutButton(Button button) {
		LayoutParams layoutParams = button.getLayoutParams();
		layoutParams.width = (mWidth - UIUtil.dip2px(this, SPACE_VALUE)
				* (NUM_COLUMNS + 1))
				/ NUM_COLUMNS;
		layoutParams.height = layoutParams.width;
		button.setLayoutParams(layoutParams);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btn_computer_on:
			sendCommand(Command.COMMAND_COMPUTER_ON);
			break;
		case R.id.btn_computer_off:
			sendCommand(Command.COMMAND_COMPUTER_OFF);
			break;
		case R.id.btn_wall_on:
			sendCommand(Command.COMMAND_WALL_ON);
			break;
		case R.id.btn_wall_off:
			sendCommand(Command.COMMAND_WALL_OFF);
			break;
		case R.id.btn_save:
			checkIpAndPort();
			break;
		case R.id.title_with_back_title_btn_right:
			showSettingPop();
			break;

		default:
			break;
		}
	}

	private void checkIpAndPort() {
		String ip = mEdtIp.getText().toString().trim();
		String port = mEdtPort.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(ip)) {
			Toast.makeText(this, "请输入ip地址", Toast.LENGTH_LONG).show();
			return;
		}
		if (StringUtil.isNullOrEmpty(port)) {
			Toast.makeText(this, "请输入端口地址", Toast.LENGTH_LONG).show();
			return;
		}
		SharedPreferenceUtil.saveValue(this, ConstantSet.CONFIG_FILE_NAME,
				ConstantSet.KEY_CONFIG_IP, ip);
		SharedPreferenceUtil.saveValue(this, ConstantSet.CONFIG_FILE_NAME,
				ConstantSet.KEY_CONFIG_PORT, Integer.valueOf(port));
		mPopWindowUtil.dissmiss();
	}

	private void sendCommand(final byte[] command) {
		if (!checkIp()) {
			showSettingPop();
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (SocketUtil.connect(MainActivity.this)) {
					if (SocketUtil.sendCommand(command)) {
						Log.d(TAG, "发送数据成功");
					} else {
						Log.d(TAG, "发送数据失败");
					}
				} else {
					Log.d(TAG, "连接失败");
				}
			}
		}).start();
	}

	private boolean checkIp() {
		boolean hasIp = false;
		String ip = SharedPreferenceUtil.getStringValueByKey(this,
				ConstantSet.CONFIG_FILE_NAME, ConstantSet.KEY_CONFIG_IP);
		int port = SharedPreferenceUtil.getIntegerValueByKey(this,
				ConstantSet.CONFIG_FILE_NAME, ConstantSet.KEY_CONFIG_PORT);
		if (!StringUtil.isNullOrEmpty(ip)
				&& SharedPreferenceUtil.INVALID_CODE != port) {
			hasIp = true;
		}
		return hasIp;
	}
}

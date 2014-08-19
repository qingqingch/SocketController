package com.ruishang.socketcontroller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruishang.socketcontroller.R;
import com.ruishang.socketcontroller.util.Command;
import com.ruishang.socketcontroller.util.SocketUtil;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initVariables();
		initView();
		setListener();
	}

	private void initVariables() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mWidth = metric.widthPixels;
	}

	private void initView() {
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
		case R.id.title_with_back_title_btn_right:

			break;

		default:
			break;
		}
	}

	private void sendCommand(final byte[] command) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				if (SocketUtil.connect()) {
					if (SocketUtil.sendCommand(command)) {
						Log.d("aaa", "发送数据成功");
					} else {
						Log.d("aaa", "发送数据失败");
					}
				} else {
					Log.d("aaa", "连接失败");
				}
			}
		}).start();

	}
}

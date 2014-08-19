package com.ruishang.socketcontroller.util;

public class Command {

	public static final byte[] COMMAND_COMPUTER_ON = new byte[] { (byte) 0xCA,
			(byte) 0x20, (byte) 0x00, (byte) 0x18, (byte) 0x02, (byte) 0x01,
			(byte) 0x01, (byte) 0xAC, };

	public static final byte[] COMMAND_COMPUTER_OFF = new byte[] { (byte) 0xCA,
			(byte) 0x20, (byte) 0x00, (byte) 0x18, (byte) 0x02, (byte) 0x01,
			(byte) 0x00, (byte) 0xAC, };

	public static final byte[] COMMAND_WALL_ON = new byte[] { (byte) 0xCA,
			(byte) 0x20, (byte) 0x00, (byte) 0x18, (byte) 0x02, (byte) 0x02,
			(byte) 0x01, (byte) 0xAC, };

	public static final byte[] COMMAND_WALL_OFF = new byte[] { (byte) 0xCA,
			(byte) 0x20, (byte) 0x00, (byte) 0x18, (byte) 0x02, (byte) 0x02,
			(byte) 0x00, (byte) 0xAC, };
	
}

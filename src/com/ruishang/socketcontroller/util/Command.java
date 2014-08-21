package com.ruishang.socketcontroller.util;

import java.util.HashMap;

import android.content.Context;

public class Command {

	public static HashMap<String, String> defaustCommand = new HashMap<String, String>();
	static {
		defaustCommand.put(ConstantSet.KEY_COMMAND_COMPUTER_ON,
				"CA  20  00  18  02  01  01  AC");
		defaustCommand.put(ConstantSet.KEY_COMMAND_COMPUTER_OFF,
				"CA  20  00  18  02  01  00  AC");
		defaustCommand.put(ConstantSet.KEY_COMMAND_WALL_ON,
				"CA  20  00  18  02  02  01  AC");
		defaustCommand.put(ConstantSet.KEY_COMMAND_WALL_OFF,
				"CA  20  00  18  02  02  00  AC");
	}

	public static String getCommandString(Context context, String tag) {
		String command = SharedPreferenceUtil.getStringValueByKey(context,
				ConstantSet.CONFIG_FILE_NAME, tag);
		if (StringUtil.isNullOrEmpty(command)) {
			command = defaustCommand.get(tag);
		}
		return command;
	}

	public static byte[] getCommand(Context context, String tag) {
		if (StringUtil.isNullOrEmpty(tag)) {
			return new byte[] {};
		}
		String command = getCommandString(context, tag);
		return getCommand(command);
	}

	public static byte[] getCommand(String command) {
		if (StringUtil.isNullOrEmpty(command)) {
			return new byte[] {};
		}
		try {
			String[] array = command.split(" +");
			byte[] commandArray = new byte[array.length];
			for (int i = 0; i < array.length; i++) {
				commandArray[i] = (byte) Integer.parseInt(array[i], 16);
			}
			return commandArray;
		} catch (Exception e) {
			return new byte[] {};
		}

	}
}

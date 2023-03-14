package com.thinktalentindia.demo.helper;

public class DemoHelper {

	public boolean isValidString(String str) {

		return null != str && str.trim().length() > 0 ? true : false;
	}
}

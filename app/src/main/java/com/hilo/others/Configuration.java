package com.hilo.others;

import android.content.SharedPreferences;

import com.hilo.MyApplication;

public class Configuration {

	// 默认值
	private final static String cfg_name = "hiloConfiguration";
	private final static String k_defaultConnectCompanyCode = "defaultConnectCompanyCode";
	private final static String k_expiredDate = "expiredDate";
	private final static String k_logoff = "logoff";
	private final static String k_clientId = "clientId";
	private final static String k_userCode = "userCode";
	private final static String k_password = "password";
	private final static String k_serverAddress = "serverAddress";
	private final static String k_sessionKey = "sessionKey";
	private final static String k_kb_height = "kb_height";
	private final static String k_isfirstlogin = "isfirstlogin";
	private final static String k_tempCompany = "k_tempCompany";
	private final static String k_tempUserCode = "k_tempUserCode";
	private final static String k_isRememberPassword = "k_isRememberPassword";
	
	public String companyCode;
	public String expiredDate;
	public boolean logoff;
	public String clientId;
	public String userCode;
	public String password;
	public String serverAddress;
	public String sessionKey; 
	public int keyBoardHeight;
	public int deviceType;
	public String deviceName;
	public boolean isFirstLogin; // 第一次进入messageFragment2界面，显示公告通知。
	//如果是体验帐户登录需要记录上一次登录的公司帐号和用户帐号
	public String tempCompanyCode;
	public String tempUserCode;
	/**
	 * 是否记住密码
	 */
	public boolean isRememberPassword;
	private static Configuration mConfig;
	public static Configuration getConfig(){
		if(mConfig != null)return mConfig;
		mConfig = new Configuration();
		SharedPreferences pref = MyApplication.mContext.getSharedPreferences(cfg_name, 0);
		
		mConfig.companyCode = pref.getString(k_defaultConnectCompanyCode, "");
		mConfig.expiredDate = pref.getString(k_expiredDate, "");
		mConfig.logoff = pref.getBoolean(k_logoff, true);
		mConfig.clientId = pref.getString(k_clientId, "");
		mConfig.userCode = pref.getString(k_userCode, "");
		mConfig.password = pref.getString(k_password, "");
		mConfig.serverAddress = pref.getString(k_serverAddress, "");
		mConfig.sessionKey = pref.getString(k_sessionKey, "");
		mConfig.keyBoardHeight = pref.getInt(k_kb_height, 0);
		mConfig.isFirstLogin = pref.getBoolean(k_isfirstlogin, true);
		mConfig.tempCompanyCode = pref.getString(k_tempCompany, "");
		mConfig.tempUserCode = pref.getString(k_tempUserCode, "");
		mConfig.isRememberPassword = pref.getBoolean(k_isRememberPassword, true);
		return mConfig;
	}
	
	public static void saveConfig(){
		if(mConfig == null)return;
		SharedPreferences pref = MyApplication.mContext.getSharedPreferences(cfg_name, 0);
		SharedPreferences.Editor edit = pref.edit();
		edit.putString(k_defaultConnectCompanyCode, mConfig.companyCode);
		edit.putString(k_expiredDate, mConfig.expiredDate);
		edit.putBoolean(k_logoff, mConfig.logoff);
		edit.putString(k_clientId, mConfig.clientId);
		edit.putString(k_userCode, mConfig.userCode);
		edit.putString(k_password, mConfig.password);
		edit.putString(k_serverAddress, mConfig.serverAddress);
		edit.putString(k_sessionKey, mConfig.sessionKey);
		edit.putInt(k_kb_height, mConfig.keyBoardHeight);
		edit.putBoolean(k_isfirstlogin, mConfig.isFirstLogin);
		edit.putString(k_tempCompany, mConfig.tempCompanyCode);
		edit.putString(k_tempUserCode, mConfig.tempUserCode);
		edit.putBoolean(k_isRememberPassword, mConfig.isRememberPassword);
		edit.commit();
	}

	public static void setConfigNull() {
		mConfig = null;
	}
}

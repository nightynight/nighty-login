package com.brokepal.nighty.login.core.util;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Global {
	private static final Properties conf ;
	
	static{
		Properties loader = new Properties(); 
		InputStream is=null;
		try {
			ResourceLoader resourceLoader=new DefaultResourceLoader();
			is=resourceLoader.getResource("direwolf.properties").getInputStream();
			loader.load(is);
			conf=loader;
		} catch (IOException e) {
			// TODO Auto-generated catch block 
			throw new RuntimeException("读取direwolf.properties 出错!",e);
		}finally{
			IOUtils.closeQuietly(is);
		}
	}
	/**
	 * 是/否
	 */
	public static final String YES = "1";
	public static final String NO = "0";
	
	/**
	 * 获取配置
	 */
	public static String getConfig(String key) { 
		return conf.getProperty(key);
	}
	
	/**
	 * 获取管理端根路径
	 */
	public static String getAdminPath() {
		return getConfig("adminPath");
	}
	
	/**
	 * 获取前端根路径
	 */
	public static String getFrontPath() {
		return getConfig("frontPath");
	}
	
	/**
	 * 获取URL后缀
	 */
	public static String getUrlSuffix() {
		return getConfig("urlSuffix");
	}
	
}

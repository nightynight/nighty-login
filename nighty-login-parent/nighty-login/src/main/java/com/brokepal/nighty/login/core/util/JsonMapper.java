package com.brokepal.nighty.login.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.IOException;

public class JsonMapper {
	private static final ObjectMapper objMapper=new ObjectMapper();
	static{
		objMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	}
	/**
	 * 对象转换为JSON字符串
	 * @param object
	 * @return
	 */
	public static String toJsonString(Object object){ 
		try {
			return objMapper.writeValueAsString(object);
		} catch (IOException e) {
			throw new RuntimeException("json 序列化出错!",e);
		}
	}

	/**
	 * JSON字符串转换为对象
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static Object fromJsonString(String jsonString, Class<?> clazz){
		try {
			return objMapper.readValue(jsonString, clazz);
		} catch (IOException e) {
			throw new RuntimeException("json 反序列化出错!",e);
		}
	}
	
	/**
	 * 反序列化复杂Collection如List<Bean>, 先使用函數createCollectionType构造类型,然后调用本函数.
	 * @see #createCollectionType(Class, Class...)
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromJson(String jsonString, JavaType javaType) {
		 
		try {
			return (T) objMapper.readValue(jsonString, javaType);
		} catch (IOException e) {
			throw new RuntimeException("json 反序列化出错!",e);
		}
	}
	
	/**
	 * 構造泛型的Collection Type如:
	 * ArrayList<MyBean>, 则调用constructCollectionType(ArrayList.class,MyBean.class)
	 * HashMap<String,MyBean>, 则调用(HashMap.class,String.class, MyBean.class)
	 */
	public static JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return objMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}
	
	/**
	 * 輸出JSONP格式數據.
	 */
	public static String toJsonP(String functionName, Object object) {
		return toJsonString(new JSONPObject(functionName, object));
	}
}

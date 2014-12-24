package com.zp.HttpHelper.cache;

import java.util.Date;

public class CacheObject {
	private String key;
	private Object value;
	private Date date;
	
	public CacheObject(String key,Object object){
		this.date=new Date();
		this.key=key;
		this.value=object;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}

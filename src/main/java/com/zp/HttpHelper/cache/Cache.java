package com.zp.HttpHelper.cache;

public abstract class Cache {
	//缓存有效时间单位为秒，默认10小时
	protected long livetime=36000;
	
	public long getLivetime() {
		return livetime;
	}
	public void setLivetime(long livetime) {
		this.livetime = livetime;
	}
	public abstract void put(String key,Object object);
	public abstract Object get(String key);
	public abstract void remove(String key);
	public abstract void clear();
	public abstract int getCount();
	
	
}

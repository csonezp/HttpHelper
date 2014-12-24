package com.zp.HttpHelper.cache;

import java.net.URI;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zp.HttpHelper.HttpHelper;

/**
 * 单例的缓存类
 * @author 张鹏
 */
public class CacheManager extends Cache {

	private static Logger logger = Logger.getLogger(CacheManager.class);
	//用来存放cache数据
	static LinkedHashMap<String, CacheObject> cache = new LinkedHashMap<String,CacheObject>(2000, 0.75f, true) {
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(Map.Entry<String,CacheObject> eldest) {
			return size() > 2000;
		}
	};
	private static final CacheManager instance=new CacheManager();
	
	private CacheManager(){
		
	}
	public static CacheManager getInstance(){
		return instance;
	}

	
	@Override
	public void put(String key, Object object) {
		CacheObject cacheObject=new CacheObject(key,object);
		cache.put(key, cacheObject);
		logger.info("PUTKEY:"+key);
		
	}
	

	@Override
	public Object get(String key) {
		CacheObject cacheObject=cache.get(key);
		if(cacheObject!=null){
			Date now=new Date();
			long span=(now.getTime()-cacheObject.getDate().getTime())/1000;
			if(span>livetime){
				cache.remove(key);
				logger.info("OUTTIME:"+key);
				
				return null;
			}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
			else {
				logger.info("GETKEY:"+key);
				return cacheObject.getValue();
				
			}
		}
		
		return null;
	}

	@Override
	public void remove(String key) {
		cache.remove(key);
		
	}

	@Override
	public void clear() {
		cache.clear();
		
	}

	@Override
	public int getCount() {
		return cache.size();
	}
	
	public long getAliveTime(){
		return super.livetime;
	}
	
	public void setAliveTime(long second){
		super.livetime=second;
	}

}

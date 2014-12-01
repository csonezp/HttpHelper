package com.zp.HttpHelper;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class HttpHelperTest {

	@Test
	public void testGetStringStringString() {
		HttpHelper httpHelper=new HttpHelper();
		String cookie=null;
		String url="http://my.zzti.edu.cn/loginPortalUrl.portal";
		Map<String, String> map=new HashMap<String, String>();
		map.put("userName","201100834519");
		map.put("password","46988646");
		try {
			cookie=httpHelper.getCookie(url, map,null, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String geturl="http://my.zzti.edu.cn/index.portal?userId=201100834519";
		try {
			httpHelper.get(geturl, cookie, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

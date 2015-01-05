package com.zp.HttpHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.zp.HttpHelper.cache.CacheManager;

public class HttpHelper {

	private static Logger logger = Logger.getLogger(HttpHelper.class);
	private static final String CHARSET_UTF8 = "UTF-8";
	@SuppressWarnings("unused")
	private static final String CHARSET_GBK = "GBK";
	// cache开关，true则开启自身缓存
	private static CloseableHttpClient httpClient;
	private boolean cacheswitch = false;
	//懒汉式单例
	private static HttpHelper instance = new HttpHelper();
	private CacheManager cacheManager = CacheManager.getInstance();
	Header[] cookieheaders = new Header[] {};

	//私有构造函数，单例
	private HttpHelper() {
		httpClient = getCloseableHttpClient();
	}
	//缓存是否打开
	public boolean isCacheing() {
		return cacheswitch;
	}
	//打开缓存
	public void openCache() {
		cacheswitch = true;
	}
	//关闭缓存
	public void stopCache() {
		cacheswitch = false;
	}

	//对外开放的获取类的单独实例的接口
	public static HttpHelper getHelper() {
		return instance;
	}

	//设置cache保存时间，超时则刷新
	public void setCacheAlivetime(long sec) {
		if (cacheswitch) {
			cacheManager.setAliveTime(sec);
		}
	}

	/**
	 * 根据传入参数设置cookie
	 * 
	 * @param url
	 * @param paramsMap
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public void getCookie(String url, Map<String, String> paramsMap,
			String charset) throws IOException {

		if (url == null || url.isEmpty()) {
			return;
		}
		// 如果传入编码则使用传入的编码，否则utf8
		charset = (charset == null ? CHARSET_UTF8 : charset);
		// 将map转成List<NameValuePair>
		List<NameValuePair> params = getParamsList(paramsMap);
		UrlEncodedFormEntity entity = null;
		HttpPost post = null;
		CloseableHttpResponse response = null;

		try {
			entity = new UrlEncodedFormEntity(params, charset);
			post = new HttpPost(url);
			// 设置post的参数
			post.setEntity(entity);
			response = httpClient.execute(post);
			// 保存response的名称为Set-Cookie的headers
			cookieheaders = response.getHeaders("Set-Cookie");
			// cookie = set_cookie.substring(0, set_cookie.indexOf(";"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				response.close();
			}

		}
		return;

	}

	/**
	 * get方法，参数需自己构建到url中，如果需要cookie则用getcookie方法设置
	 * 
	 * @param url
	 *            地址
	 * @param charset
	 *            默认utf8，可为空
	 * @return
	 * @throws IOException
	 */
	public String get(String url, String charset) throws IOException {
		if (url == null || url.isEmpty()) {
			return null;
		}
		// 如果缓存中有，则直接取出并返回
		if (cacheswitch == true) {
			Object cacheObj = cacheManager.get(url);
			if (cacheObj != null) {
				return (String) cacheObj;
			}
		}
		
		charset = (charset == null ? CHARSET_UTF8 : charset);
		HttpGet get = new HttpGet(url);
		if (cookieheaders != null && cookieheaders.length > 0) {

			for (Header header : cookieheaders) {
				get.addHeader(header);
			}
		}
		CloseableHttpResponse response = null;
		String res = null;
		try {
			response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();

			logger.info("GET:" + url);
			res = EntityUtils.toString(entity, charset);
			// 放入缓存
			if (cacheswitch) {
				cacheManager.put(url, res);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (response != null) {
				response.close();
			}

		}
		return res;
	}

	/**
	 * 只传一个网址的get
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public String get(String url) throws IOException {
		return get(url, null);
	}

	public String post(String url, Map<String, String> map) throws IOException {
		return post(url, map, null);
	}

	/**
	 * post方法，如果需要cookie则用getcookie方法设置
	 * 
	 * @param url
	 * @param paramsMap
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public String post(String url, Map<String, String> paramsMap, String charset)
			throws IOException {
		if (url == null || url.isEmpty()) {
			return null;
		}
		List<NameValuePair> params = getParamsList(paramsMap);
		UrlEncodedFormEntity formEntity = null;
		HttpPost post = null;
		CloseableHttpResponse response = null;
		String res = null;
		try {
			charset = (charset == null ? CHARSET_UTF8 : charset);
			formEntity = new UrlEncodedFormEntity(params, charset);
			post = new HttpPost(url);

			post.setEntity(formEntity);
			if (cookieheaders != null && cookieheaders.length > 0) {

				for (Header header : cookieheaders) {
					post.addHeader(header);
				}
			}
			response = httpClient.execute(post);
			res = EntityUtils.toString(response.getEntity());

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (response != null) {
				response.close();
			}

		}
		return res;

	}

	/**
	 * 获取httpclien，关于httpclient的设置可以在这里进行
	 * 
	 * @param charset
	 * @return
	 */
	private CloseableHttpClient getCloseableHttpClient() {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		return httpclient;

	}

	public void closeClient() throws IOException {
		httpClient.close();
	}

	/**
	 * 将传入的键/值对参数转换为NameValuePair参数集
	 *
	 * @param paramsMap
	 *            参数集, 键/值对
	 * @return NameValuePair参数集
	 */
	private List<NameValuePair> getParamsList(Map<String, String> paramsMap) {
		if (paramsMap == null || paramsMap.size() == 0) {
			return new  ArrayList<NameValuePair>();
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> map : paramsMap.entrySet()) {
			params.add(new BasicNameValuePair(map.getKey(), map.getValue()));
		}
		if(params==null){
			return new  ArrayList<NameValuePair>();
		}
		return params;
	}

}

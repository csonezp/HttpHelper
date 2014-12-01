package com.zp.HttpHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

public class HttpHelper {
	private static final String CHARSET_UTF8 = "UTF-8";
	private static final String CHARSET_GBK = "GBK";

	/**
	 * 根据传入参数获取cookie
	 * 
	 * @param url
	 * @param paramsMap
	 * @param cookie
	 *            双重cookie用，可为null
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String getCookie(String url, Map<String, String> paramsMap,
			String cookie, String charset) throws IOException {
		if (url == null || url.isEmpty()) {
			return null;
		}
		charset = (charset == null ? CHARSET_UTF8 : charset);
		CloseableHttpClient httpClient = getCloseableHttpClient();
		List<NameValuePair> params = getParamsList(paramsMap);
		UrlEncodedFormEntity entity = null;
		HttpPost post = null;
		CloseableHttpResponse response = null;
		HttpEntity entity2 = null;
		String cookie2 = null;

		try {
			entity = new UrlEncodedFormEntity(params, charset);
			post = new HttpPost(url);
			if (cookie != null && !cookie.isEmpty()) {
				post.setHeader("Cookie", cookie);
			}
			post.setEntity(entity);
			response = httpClient.execute(post);
			String set_cookie = response.getFirstHeader("Set-Cookie")
					.getValue();
			cookie = set_cookie.substring(0, set_cookie.indexOf(";"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
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
			if (httpClient != null) {
				httpClient.close();
			}
		}
		return cookie;

	}

	/**
	 * get方法，参数需自己构建到url中
	 * 
	 * @param url
	 *            地址
	 * @param cookie
	 *            可为null
	 * @param charset
	 *            默认utf8，可为空
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, String cookie, String charset)
			throws IOException {
		if (url == null || url.isEmpty()) {
			return null;
		}
		CloseableHttpClient httpClient = getCloseableHttpClient();
		HttpGet get = new HttpGet(url);
		if (cookie != null && !cookie.isEmpty()) {

			get.setHeader("Cookie", cookie);
		}
		CloseableHttpResponse response = null;
		String res = null;
		try {
			response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			res = EntityUtils.toString(entity);
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
			if (httpClient != null) {
				httpClient.close();
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
	public static String get(String url) throws IOException {
		return get(url, null, null);
	}

	public static String post(String url, Map<String, String> map)
			throws IOException {
		return post(url, map, null, null);
	}

	/**
	 * post方法，cookie可为空
	 * 
	 * @param url
	 * @param paramsMap
	 * @param cookie
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, Map<String, String> paramsMap,
			String cookie, String charset) throws IOException {
		if (url == null || url.isEmpty()) {
			return null;
		}
		CloseableHttpClient httpClient = getCloseableHttpClient();
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
			if (cookie != null && !cookie.isEmpty()) {

				post.setHeader("Cookie", cookie);
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
			if (httpClient != null) {
				httpClient.close();
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
	private static CloseableHttpClient getCloseableHttpClient() {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		return httpclient;

	}

	/**
	 * 将传入的键/值对参数转换为NameValuePair参数集
	 *
	 * @param paramsMap
	 *            参数集, 键/值对
	 * @return NameValuePair参数集
	 */
	private static List<NameValuePair> getParamsList(
			Map<String, String> paramsMap) {
		if (paramsMap == null || paramsMap.size() == 0) {
			return null;
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> map : paramsMap.entrySet()) {
			params.add(new BasicNameValuePair(map.getKey(), map.getValue()));
		}
		return params;
	}

}

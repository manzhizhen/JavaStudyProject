package com.manzhizhen.httpclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientTest {
	private static HttpClient hc;
	static {
		hc = HttpClientBuilder.create().build();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("email", "xxx@gmail.com"));
		params.add(new BasicNameValuePair("pwd", "xxx"));
		params.add(new BasicNameValuePair("save_login", "1"));
		
		String url = "http://localhost:8080/JavaStudyWebProject/testServlet";

		String body = post(url, params);

		System.out.println(body);
	}

	/**
	 * Get请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static String get(String url, List<NameValuePair> params) {
		String body = null;
		try {
			// Get请求
			HttpGet httpget = new HttpGet(url);

			// 设置参数
			String str = EntityUtils.toString(new UrlEncodedFormEntity(params));

			httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));

			// 发送请求
			HttpResponse httpresponse = hc.execute(httpget);

			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();

			body = EntityUtils.toString(entity);

			if (entity != null) {
				entity.consumeContent();
			}

		} catch (ParseException e) {
			e.printStackTrace();
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();

		} catch (URISyntaxException e) {
			e.printStackTrace();

		}

		return body;
	}

	/**
	 * 
	 * // Post请求
	 * 
	 * @param url
	 * 
	 * @param params
	 * 
	 * @return
	 */

	public static String post(String url, List<NameValuePair> params) {
		String body = null;

		try {
			// Post请求
			HttpPost httppost = new HttpPost(url);

			// 设置参数
			httppost.setEntity(new UrlEncodedFormEntity(params));

			// 发送请求
			HttpResponse httpresponse = hc.execute(httppost);

			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();

			body = EntityUtils.toString(entity);
			if (entity != null) {
				entity.consumeContent();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

		} catch (ClientProtocolException e) {
			e.printStackTrace();

		} catch (ParseException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		}

		return body;

	}

}

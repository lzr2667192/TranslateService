package com.iedgeco.ryan.translate.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

import com.iedgeco.ryan.translate.config.StaticDef;

public class Translator {

	private static final String TAG = "Translator";

	private static final String ENCODING = "UTF-8";

	/**
	 * <pre>
	 * 请求标准： http://fanyi.youdao.com/openapi.do?
	 * 		keyfrom=<keyfrom>&
	 * 		key=<key>&
	 * 		type=data&
	 * 		doctype=<doctype>&
	 * 		version=1.1&
	 * 		q=要翻译的文本
	 * 版本：1.1，请求方式：get，编码方式：utf-8
	 * 主要功能：
	 * 		中英互译，同时获得有道翻译结果和有道词典结果（可能没有）
	 * 参数说明：
	 * 		type - 返回结果的类型，固定为data 
	 * 		doctype - 返回结果的数据格式，xml或json或jsonp
	 * 		version - 版本，当前最新版本为1.1 
	 * 		q - 要翻译的文本，不能超过200个字符，需要使用utf-8编码
	 * 	errorCode： 　
	 * 		0 - 正常 　
	 * 		20 - 要翻译的文本过长 　
	 * 		30 - 无法进行有效的翻译 　
	 * 		40 - 不支持的语言类型 　
	 * 		50 - 无效的key
	 * </pre>
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * */
	public static String translate(String text) throws Exception {
		String url = "http://fanyi.youdao.com/openapi.do?keyfrom=" + StaticDef.KEY_FROM
				+ "&key=" + StaticDef.KEY_FOR_TRANSLATE
				+ "&type=data"
				+ "&doctype=json"
				+ "&version=1.1"
				+ "&q=" + text;
		try{
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = client.execute(httpGet);
			InputStream is = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, ENCODING));
			StringBuffer result = new StringBuffer();
			String string = null;
			if(null != (string = reader.readLine()))
				result.append(string).append('\n');
			//TODO parse the response json string
			String translation = (String) new JSONObject(result.toString())
				.getJSONArray("translation").get(0);
			
			Log.i(TAG, "result: " + result.toString());
			return translation;
		}finally{
			//close stream here
		}
	}
}

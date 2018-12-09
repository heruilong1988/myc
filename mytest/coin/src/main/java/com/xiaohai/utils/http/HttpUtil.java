package com.xiaohai.utils.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.xiaohai.common.utils.base.StrUtil;

public class HttpUtil {
	
	/**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        String urlNameString = "";
        try {
        	param = (StrUtil.isBlank(param) ? "" : "?" + param);
            urlNameString = url + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
            
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println(String.format("发送GET请求【%s】出现异常【%s】", urlNameString, e.getMessage()));
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    
    public static String postUrl(String url, List<NameValuePair> params) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			post.setEntity(new UrlEncodedFormEntity(params));

			RequestConfig requestConfig = RequestConfig.custom()  
			        .setConnectTimeout(20000).setConnectionRequestTimeout(20000)  
			        .setSocketTimeout(20000).build();
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			post.setConfig(requestConfig);
			
			CloseableHttpResponse response = httpClient.execute(post);
			HttpEntity responseEntity = response.getEntity();
			
			return EntityUtils.toString(responseEntity);
			
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
	}
    
    /**
     * post请求，json格式
     * @param url
     * @param params
     * @return
     */
	public static String postUrl(String url, JSONObject params) {
    	DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
//	        JSONObject response = null;
        String result = "";
		try {
			StringEntity s = new StringEntity(params.toString());
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");// 发送json数据需要设置contentType
			post.setEntity(s);
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//					HttpEntity entity = res.getEntity();
				result = EntityUtils.toString(res.getEntity());// 返回json格式：
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
    
}

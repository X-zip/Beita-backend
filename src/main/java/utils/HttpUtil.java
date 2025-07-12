package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;


public class HttpUtil {
	static String pathName = "G:\\publicKey.txt";

	public static String getSerchPersion(String url, String param) {
		HttpClient httpClient = new HttpClient();

		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);

		GetMethod getMethod = new GetMethod(url);

		getMethod.getParams().setParameter("http.socket.timeout",
				Integer.valueOf(5000));

		getMethod.getParams().setParameter("http.method.retry-handler",
				new DefaultHttpMethodRetryHandler());
		String response = "";
		try {
			int statusCode = httpClient.executeMethod(getMethod);

			if (statusCode != 200) {
				System.err.println("请求出错: " + getMethod.getStatusLine());
			}

			Header[] headers = getMethod.getResponseHeaders();
			for (Header h : headers) {
				System.out
						.println(h.getName() + "------------ " + h.getValue());
			}
			byte[] responseBody = getMethod.getResponseBody();
			response = new String(responseBody, param);
			System.out.println("----------response:" + response);
		} catch (HttpException e) {
			System.out.println("请检查输入的URL!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("发生网络异常!");
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return response;
	}

	public static JSONObject doPost(String url, JSONObject json, String charset) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		post.addHeader("Content-Type", "application/json;charset=" + charset);
		JSONObject response = null;
		try {
			StringEntity s = new StringEntity(json.toString(), charset);
			s.setContentEncoding(new BasicHeader("Content-Type",
					"application/json"));
			post.setEntity(s);
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(res.getEntity());
				response = JSONObject.fromObject(result);
				System.out.println("----------response:" + response.toString());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}

	public static JSONObject doPost(String url, JSONArray json, String charset) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		JSONObject response = null;
		try {
			StringEntity s = new StringEntity(json.toString());
			s.setContentEncoding(charset);
			s.setContentType("application/json");
			post.setEntity(s);
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(res.getEntity());
				response = JSONObject.fromObject(result);
				System.out.println("----------response:" + response.toString());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}

	public static JSONObject doPost(String url, String encryptByPublicKey,
			String charset) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		JSONObject response = null;
		try {
			StringEntity s = new StringEntity(encryptByPublicKey);
			s.setContentEncoding(charset);
			s.setContentType("application/json");
			post.setEntity(s);
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(res.getEntity());
				response = JSONObject.fromObject(result);
				System.out.println("----------response:" + response.toString());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}

	public static String doPostByXML(String url, String encryptByPublicKey,
			String charset) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		String result = "";
		try {
			StringEntity s = new StringEntity(encryptByPublicKey);
			s.setContentEncoding(charset);
			s.setContentType("text/xml");
			post.setEntity(s);
			HttpResponse res = client.execute(post);
			System.out.println("doPostByXML result:"+res.toString());
			System.out.println("doPostByXML status code:"+res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == 200) {
				result = new String(EntityUtils.toString(res.getEntity())
						.getBytes("ISO-8859-1"), "UTF8");
				System.out.println("----------response:" + result);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public static String doPostByXMLWithCertificate(String url, String encryptByPublicKey,
			String charset) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
        //加载本地的证书进行https加密传输,keystorePath是证书的绝对路径
//		String keystorePath = "D:\\项目\\跑腿\\apiclient_cert.p12";
		String keystorePath = "/home/paotui/apiclient_cert.p12";
        FileInputStream instream = new FileInputStream(new File(keystorePath)); 
        String keystorePassword = "1639929923";
        try{
            //设置证书密码,keystorePassword:下载证书时的密码，默认密码是你的mchid
            keyStore.load(instream, keystorePassword.toCharArray()); 
        } finally { 
            instream.close();
        }        
        //java 主动信任证书
        //keystorePassword:下载证书时的密码，默认密码是你的mchid
        SSLContext sslcontext = SSLContexts.custom()
              .loadKeyMaterial(keyStore, keystorePassword.toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
              sslcontext, 
              SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
      //CloseableHttpClient 加载证书来访问https网站
        CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        
//		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		String result = "";
		try {
			StringEntity s = new StringEntity(encryptByPublicKey);
			s.setContentEncoding(charset);
			s.setContentType("text/xml");
			post.setEntity(s);
			HttpResponse res = client.execute(post);
			System.out.println("doPostByXML result:"+res.toString());
			System.out.println("doPostByXML status code:"+res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == 200) {
				result = new String(EntityUtils.toString(res.getEntity())
						.getBytes("ISO-8859-1"), "UTF8");
				System.out.println("----------response:" + result);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static void main(String[] arg) throws Exception {

		// 分中心数据获取接口
		 String url = "http://127.0.0.1:8080/woniu/feeds.do";
		 doPost(url, "", "UTF-8");


	}
}
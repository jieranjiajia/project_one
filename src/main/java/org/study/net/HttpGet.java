package org.study.net;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;



/**
 * 下载jqueryeasyui网站上的东习西
 * @author ou_quan_sheng
 * http://www.jeasyui.com/download/downloads/jquery-easyui-1.5.1.zip
 */
public class HttpGet {

	private String url;
	private String saveFileName;

	public HttpGet(String url, String saveFileName) {
		this.url = url;
		this.saveFileName = saveFileName;
	}
	
	

	/**
	 * 将一个文件进行分段的下载
	 * @param urlStr            远程请求的URL
	 * @param fileSaveDir       本地保存文件的地址
	 * @param poolSize          线程池的数量
	 * @throws IOException
	 */
	public void downFile(int poolSize) {
		try {
			HttpURLConnection con = getHttpURLConnection(this.url);
			//获取远程文件下载的长度
			long fileLength = con.getContentLength();
			System.out.println("要下载的文件长度:"+fileLength);
			con.disconnect();
			
			
			//每个线程下载的大小
			long every = fileLength / poolSize;
			
			for(int i = 0; i < poolSize; i++) {
				long startPosition = i * every;
				long endPosition;
				if(i == poolSize - 1) {
					endPosition = fileLength;
				} else {
					endPosition = (i+1) * every;
				}
				
				new DownFile(this.url, startPosition, endPosition).start();;
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置请求头
	 */
	public static HttpURLConnection getHttpURLConnection(String urlStr) throws IOException {
		HttpURLConnection con = (HttpURLConnection) new URL(urlStr).openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
		con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		con.setRequestProperty("Accept-Encoding", "gzip, deflate");
		con.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		con.setRequestProperty("Keep-Alive", "300");
		con.setRequestProperty("Cache-conntrol", "max-age=0");
		con.setRequestProperty("Referer", "http://www.baidu.com");
		con.setConnectTimeout(10000);
		con.setRequestProperty("Connection", "keep-alive");
		con.setRequestProperty("Accept", "*/*");
		return con;
	}
	

	
	/**
	 * 设置代理服务器
	 */
	public void setProxyServer(String proxyHost, String proxyPort) {
		System.getProperties().put("proxySet", "true");
		System.getProperties().put("proxyHost", proxyHost);
		System.getProperties().put("proxyPort", proxyPort);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSaveFileName() {
		return saveFileName;
	}

	public void setSaveFileName(String saveFileName) {
		this.saveFileName = saveFileName;
	}
	
	public static void main(String[] args) {
		HttpGet get = new HttpGet("http://www.jeasyui.com/download/downloads/jquery-easyui-1.5.1.zip","f:\\jquery-easyui-1.5.1.zip");
		get.downFile(3);
	}
	
	
}
